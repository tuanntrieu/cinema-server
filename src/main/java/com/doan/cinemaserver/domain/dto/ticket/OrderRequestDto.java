package com.doan.cinemaserver.domain.dto.ticket;


import com.doan.cinemaserver.constant.CommonConstant;
import com.doan.cinemaserver.constant.ErrorMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {

    private Long customerId =0L;
    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    private String customerName;
    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    @Pattern(regexp = CommonConstant.REGEX_EMAIL, message = ErrorMessage.INVALID_FORMAT_EMAIL)
    private String customerEmail;

    private Long movieId=0L;

    private Long scheduleId=0L;
    private Long[] seatId;
    private ComboOrderDto[] combos;
}
