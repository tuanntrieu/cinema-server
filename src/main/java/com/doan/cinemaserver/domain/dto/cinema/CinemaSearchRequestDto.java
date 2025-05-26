package com.doan.cinemaserver.domain.dto.cinema;

import com.doan.cinemaserver.domain.dto.pagination.PaginationRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CinemaSearchRequestDto extends PaginationRequestDto {
    private String name;
}
