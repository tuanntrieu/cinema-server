package com.doan.cinemaserver.domain.dto.movie;

import com.doan.cinemaserver.constant.ErrorMessage;
import com.doan.cinemaserver.domain.dto.pagination.PaginationRequestDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieSearchRequestDto extends PaginationRequestDto {

    private Long cinemaId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateSearch;

}
