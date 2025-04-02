package com.doan.cinemaserver.domain.dto.movietype;

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
public class MovieTypeRequestDto{
    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    private String name;
}
