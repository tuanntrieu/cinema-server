package com.doan.cinemaserver.domain.dto.auth;

import com.doan.cinemaserver.constant.ErrorMessage;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenRefreshRequestDto {
    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    private String refreshToken;
}
