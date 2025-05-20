package com.doan.cinemaserver.domain.dto.food;

import com.doan.cinemaserver.domain.dto.pagination.PaginationRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FoodRequestDto extends PaginationRequestDto {
    private String name;
}
