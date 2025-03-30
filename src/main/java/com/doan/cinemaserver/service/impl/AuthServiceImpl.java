package com.doan.cinemaserver.service.impl;

import com.doan.cinemaserver.constant.ErrorMessage;
import com.doan.cinemaserver.constant.SuccessMessage;
import com.doan.cinemaserver.domain.dto.auth.LoginRequestDto;
import com.doan.cinemaserver.domain.dto.auth.LoginResponseDto;
import com.doan.cinemaserver.domain.dto.auth.TokenRefreshRequestDto;
import com.doan.cinemaserver.domain.dto.auth.TokenRefreshResponseDto;
import com.doan.cinemaserver.domain.entity.User;
import com.doan.cinemaserver.exception.InvalidException;
import com.doan.cinemaserver.exception.NotFoundException;
import com.doan.cinemaserver.exception.UnauthorizedException;
import com.doan.cinemaserver.repository.UserRepository;
import com.doan.cinemaserver.security.UserPrincipal;
import com.doan.cinemaserver.security.jwt.JwtTokenProvider;
import com.doan.cinemaserver.service.AuthService;
import com.doan.cinemaserver.service.JwtTokenService;
import com.doan.cinemaserver.util.MessageSourceUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenService jwtTokenService;
    private final MessageSourceUtil messageSourceUtil;
    private final UserRepository userRepository;
    private final CustomUserDetailsServiceImpl customUserDetailsService;

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
    public String logout(
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
        return messageSourceUtil.getMessage(SuccessMessage.Auth.LOGOUT_SUCCESS,null);

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

}
