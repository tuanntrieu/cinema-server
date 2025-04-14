package com.doan.cinemaserver.domain.dto.ticket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComboOrderDto {
    private Long comboId;
    private int quantity;
}
