package com.doan.cinemaserver.service.impl;

import com.doan.cinemaserver.constant.ErrorMessage;
import com.doan.cinemaserver.constant.RoleConstant;
import com.doan.cinemaserver.constant.SuccessMessage;
import com.doan.cinemaserver.domain.dto.auth.*;
import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.common.DataMailDto;
import com.doan.cinemaserver.domain.entity.Customer;
import com.doan.cinemaserver.domain.entity.Role;
import com.doan.cinemaserver.domain.entity.User;
import com.doan.cinemaserver.exception.DataIntegrityViolationException;
import com.doan.cinemaserver.exception.InvalidException;
import com.doan.cinemaserver.exception.NotFoundException;
import com.doan.cinemaserver.exception.UnauthorizedException;
import com.doan.cinemaserver.repository.CustomerRepository;
import com.doan.cinemaserver.repository.RoleRepository;
import com.doan.cinemaserver.repository.UserRepository;
import com.doan.cinemaserver.security.UserPrincipal;
import com.doan.cinemaserver.security.jwt.JwtTokenProvider;
import com.doan.cinemaserver.service.AuthService;
import com.doan.cinemaserver.service.JwtTokenService;
import com.doan.cinemaserver.util.MessageSourceUtil;
import com.doan.cinemaserver.util.SendMailUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenService jwtTokenService;
    private final MessageSourceUtil messageSourceUtil;
    private final UserRepository userRepository;
    private final CustomUserDetailsServiceImpl customUserDetailsService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;
    private final SendMailUtil sendMailUtil;
    private final RedisTemplate<String,Object> redisTemplate;

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            String access_token = jwtTokenProvider.generateToken(userPrincipal, Boolean.FALSE);
            String refresh_token = jwtTokenProvider.generateToken(userPrincipal, Boolean.TRUE);
            User user = userRepository.findByEmail(loginRequestDto.getUsername()).orElseThrow(
                    () -> new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_USERNAME,new String[]{loginRequestDto.getUsername()})
            );
            user.setRefreshToken(refresh_token);
            userRepository.save(user);
            return LoginResponseDto.builder()
                    .access_token(access_token)
                    .refresh_token(refresh_token)
                    .username(userPrincipal.getUsername())
                    .build();
        } catch (AuthenticationException e) {
            throw new UnauthorizedException(ErrorMessage.Auth.ERR_INCORRECT_USERNAME_PASSWORD);
        } catch (UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(ErrorMessage.ERR_EXCEPTION_GENERAL);
        }
    }

    @Override
    public CommonResponseDto logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String access_token = bearerToken.substring(7);
            jwtTokenService.saveInvalidToken(access_token);
            String username= jwtTokenProvider.extractSubjectFromJwt(access_token);
            User user = userRepository.findByEmail(username).orElseThrow(
                    () -> new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_USERNAME,new String[]{username})
            );
            jwtTokenService.saveInvalidToken(user.getRefreshToken());

        }
        SecurityContextLogoutHandler logout = new SecurityContextLogoutHandler();
        logout.logout(request, response, authentication);
        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.Auth.LOGOUT_SUCCESS,null));

    }

    @Override
    public TokenRefreshResponseDto refreshToken(TokenRefreshRequestDto tokenRefreshRequestDto) {
        String token=tokenRefreshRequestDto.getRefreshToken();
        if(StringUtils.hasText(token)&& jwtTokenProvider.validateToken(token)&& !jwtTokenService.isInvalidToken(token)){
            String username= jwtTokenProvider.extractSubjectFromJwt(token);
            UserPrincipal userPrincipal = (UserPrincipal) customUserDetailsService.loadUserByUsername(username);

            String accessToken=jwtTokenProvider.generateToken(userPrincipal, Boolean.FALSE);
            String refreshToken=jwtTokenProvider.generateToken(userPrincipal, Boolean.TRUE);

            jwtTokenService.saveInvalidToken(token);
            User user = userRepository.findByEmail(username).orElseThrow(
                    ()->new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_USERNAME,new String[]{username})
            );
            user.setRefreshToken(refreshToken);
            userRepository.save(user);

            return TokenRefreshResponseDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
        else {
            throw new InvalidException(ErrorMessage.Auth.ERR_INVALID_REFRESH_TOKEN);
        }

    }

    @Override
    @Transactional
    public CommonResponseDto register(RegisterRequestDto registerRequestDto) {
        Boolean existEmail=userRepository.existsByEmail(registerRequestDto.getEmail());
        if(existEmail){
            throw new DataIntegrityViolationException(ErrorMessage.Auth.ERR_EXIST_EMAIL);
        }
        if(!registerRequestDto.getPassword().equals(registerRequestDto.getConfirmPassword())){
            throw new DataIntegrityViolationException(ErrorMessage.INVALID_REPEAT_PASSWORD);
        }
        Role role = roleRepository.findByCode(RoleConstant.ROLE_USER).orElseThrow(
                ()->new NotFoundException(ErrorMessage.Role.ERR_NOT_FOUND_ROLE,new String[]{String.valueOf(RoleConstant.ROLE_USER)})
        );
        User user = User.builder()
                .email(registerRequestDto.getEmail())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .role(role)
                .build();
        userRepository.save(user);
        customerRepository.save(
                Customer.builder()
                        .fullName(registerRequestDto.getFullName())
                        .user(user)
                        .build()
        );

        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.Auth.REGISTER_SUCCESS,null));

    }

    @Override
    public CommonResponseDto changePassword(ChangePasswordRequestDto requestDto) {
        User user= userRepository.findByEmail(requestDto.getEmail()).orElseThrow(
                ()->new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_USERNAME,new String[]{requestDto.getEmail()})
        );

        Boolean isCorrectPassword=passwordEncoder.matches(requestDto.getOldPassword(),user.getPassword());

        if(!isCorrectPassword){
            throw new DataIntegrityViolationException(ErrorMessage.Auth.ERR_INCORRECT_PASSWORD);
        }

        if(!requestDto.getPassword().equals(requestDto.getConfirmPassword())){
            throw new DataIntegrityViolationException(ErrorMessage.INVALID_REPEAT_PASSWORD);
        }
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        userRepository.save(user);

        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.Auth.CHANGE_PASSWORD_SUCCESS,null));
    }

    @Override
    public CommonResponseDto sendOtp(SendOtpRequestDto sendOtpRequestDto) {

        if(!userRepository.existsByEmail(sendOtpRequestDto.getEmail())){
            throw new NotFoundException(ErrorMessage.Auth.ERR_NOT_EXIST_EMAIL,null);
        }

        User user= userRepository.findByEmail(sendOtpRequestDto.getEmail()).orElseThrow(
                ()->new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_USERNAME,new String[]{sendOtpRequestDto.getEmail()})
        );
        SecureRandom secureRandom = new SecureRandom();
        String randomNumber = String.format("%06d", secureRandom.nextInt(1000000));


        DataMailDto mailDto = new DataMailDto();
        mailDto.setTo(sendOtpRequestDto.getEmail());
        mailDto.setSubject("Quên mật khẩu");
        Map<String, Object> properties = new HashMap<>();
        properties.put("otp", randomNumber);

        mailDto.setProperties(properties);
        try {
            sendMailUtil.sendEmailWithHTML(mailDto, "forgetPassword.html");
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        if(redisTemplate.hasKey(user.getEmail())){
            redisTemplate.delete(user.getEmail());
        }
        redisTemplate.opsForValue().set(user.getEmail(), randomNumber, 2, TimeUnit.MINUTES);


        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.Auth.SEND_OTP_SUCCESS,null));
    }

    @Override
    public Boolean verifyOtp(VerifyOtpRequestDto verifyOtpRequestDto) {
        String email = verifyOtpRequestDto.getEmail();
        String inputOtp = verifyOtpRequestDto.getOtp();
        Boolean isKeyExists = redisTemplate.hasKey(email);
        if (Boolean.TRUE.equals(isKeyExists)) {
            String storedOtp = (String) redisTemplate.opsForValue().get(email);
            if (storedOtp != null && storedOtp.equals(inputOtp)) {
                redisTemplate.delete(email);
                return true;
            }
        }
        return false;
    }

    @Override
    public CommonResponseDto forgetPassword(ForgetPasswordDto forgetPasswordDto) {
        User user= userRepository.findByEmail(forgetPasswordDto.getEmail()).orElseThrow(
                    ()->new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_USERNAME,new String[]{forgetPasswordDto.getEmail()})
        );

        if(!forgetPasswordDto.getPassword().equals(forgetPasswordDto.getConfirmPassword())){
            throw new DataIntegrityViolationException(ErrorMessage.INVALID_REPEAT_PASSWORD);
        }
        user.setPassword(passwordEncoder.encode(forgetPasswordDto.getPassword()));
        userRepository.save(user);

        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.Auth.CHANGE_PASSWORD_SUCCESS,null));
    }

}
