package com.doan.cinemaserver.domain.dto.cinema;

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
public class CinemaRequestDto {
    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    private String cinemaName;

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    @Pattern(regexp = CommonConstant.REGEXP_PHONE_NUMBER,message = ErrorMessage.INVALID_FORMAT_PHONE)
    private String hotline;

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    private String province;

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    private String district;

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    private String ward;

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    private String detailAddress;
}
