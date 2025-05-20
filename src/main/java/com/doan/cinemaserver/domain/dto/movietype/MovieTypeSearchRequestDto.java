package com.doan.cinemaserver.domain.dto.movietype;

import com.doan.cinemaserver.domain.dto.pagination.PaginationRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieTypeSearchRequestDto extends PaginationRequestDto {
    private String name;
}
