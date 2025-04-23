package com.doan.cinemaserver.service;


import com.doan.cinemaserver.domain.dto.auth.*;
import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
public interface AuthService {
    public LoginResponseDto login(LoginRequestDto loginRequestDto);
    public CommonResponseDto logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    );
    public TokenRefreshResponseDto refreshToken(TokenRefreshRequestDto tokenRefreshRequestDto );

    public CommonResponseDto register(RegisterRequestDto registerRequestDto);
    public CommonResponseDto changePassword(ChangePasswordRequestDto requestDto, HttpServletRequest request);
    public CommonResponseDto sendOtp(SendOtpRequestDto sendOtpRequestDto, HttpServletRequest request);
    public Boolean verifyOtp(VerifyOtpRequestDto verifyOtpRequestDto, HttpServletRequest request);
    public CommonResponseDto forgetPassword(ForgetPasswordDto forgetPasswordDto, HttpServletRequest request);
}
