package com.doan.cinemaserver.domain.dto.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto {
    private String username;
    private String access_token;
    private String refresh_token;
}
