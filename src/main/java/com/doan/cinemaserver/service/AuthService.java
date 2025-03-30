package com.doan.cinemaserver.service;


import com.doan.cinemaserver.domain.dto.auth.LoginRequestDto;
import com.doan.cinemaserver.domain.dto.auth.LoginResponseDto;
import com.doan.cinemaserver.domain.dto.auth.TokenRefreshRequestDto;
import com.doan.cinemaserver.domain.dto.auth.TokenRefreshResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    public LoginResponseDto login(LoginRequestDto loginRequestDto);
    public String logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    );
    public TokenRefreshResponseDto refreshToken(TokenRefreshRequestDto tokenRefreshRequestDto);
}
