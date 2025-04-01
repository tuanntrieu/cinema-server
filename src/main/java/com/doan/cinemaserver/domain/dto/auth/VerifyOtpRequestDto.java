package com.doan.cinemaserver.domain.dto.auth;

import com.doan.cinemaserver.constant.CommonConstant;
import com.doan.cinemaserver.constant.ErrorMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerifyOtpRequestDto {
    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    @Pattern(regexp = CommonConstant.REGEX_EMAIL,message = ErrorMessage.INVALID_FORMAT_EMAIL)
    private String email;

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    private String otp;
}
