package com.doan.cinemaserver.domain.dto.combo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComboDetailRequestDto {
    private Long foodId;
    private Integer quantity = 0;
}
