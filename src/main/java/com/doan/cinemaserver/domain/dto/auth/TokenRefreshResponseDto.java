package com.doan.cinemaserver.domain.dto.auth;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenRefreshResponseDto {
    private String accessToken;
    private String refreshToken;
}

