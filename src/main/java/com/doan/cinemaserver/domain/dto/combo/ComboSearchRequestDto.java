package com.doan.cinemaserver.domain.dto.combo;

import com.doan.cinemaserver.domain.dto.pagination.PaginationRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ComboSearchRequestDto extends PaginationRequestDto {
    private String name;
}
