package com.doan.cinemaserver.controller;

import com.doan.cinemaserver.common.RestApiV1;
import com.doan.cinemaserver.common.VsResponseUtil;
import com.doan.cinemaserver.constant.UrlConstant;
import com.doan.cinemaserver.domain.dto.auth.LoginRequestDto;
import com.doan.cinemaserver.domain.dto.auth.TokenRefreshRequestDto;
import com.doan.cinemaserver.domain.entity.User;
import com.doan.cinemaserver.repository.UserRepository;
import com.doan.cinemaserver.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestApiV1
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final RedisTemplate<String,Object>redisTemplate;
    private final AuthService authService;

    @PostMapping(UrlConstant.Auth.LOGIN)
    @Operation(summary = "API Login")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Thành công"),
            @ApiResponse(responseCode = "401", description = "Chưa xác thực"),
            @ApiResponse(responseCode = "403", description = "Truy cập bị cấm"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy")
    })
    public ResponseEntity<?>login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return VsResponseUtil.success(authService.login(loginRequestDto));
    }

    @PostMapping(UrlConstant.Auth.LOGOUT)
    @Operation(summary = "API Logout")
    public ResponseEntity<?>logout(HttpServletRequest request,
                                   HttpServletResponse response,
                                   Authentication authentication) {
        return VsResponseUtil.success(authService.logout(request,response,authentication));
    }

    @PostMapping(UrlConstant.Auth.REFRESH_TOKEN)
    @Operation(summary = "API Refresh Token")
    public ResponseEntity<?>refreshToken(@Valid @RequestBody TokenRefreshRequestDto requestDto) {
        return VsResponseUtil.success(authService.refreshToken(requestDto));
    }


    @GetMapping("/alo")
    public ResponseEntity<?> getAlo() {
        return VsResponseUtil.success(userRepository.findAll());
    }


}
