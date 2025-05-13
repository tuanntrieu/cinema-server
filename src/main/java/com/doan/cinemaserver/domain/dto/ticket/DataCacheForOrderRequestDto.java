package com.doan.cinemaserver.domain.dto.ticket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataCacheForOrderRequestDto {
    private String vnp_TxnRef;
    private Long customerId =0L;

    private String customerName;

    private String customerEmail;

    private Long movieId=0L;

    private Long scheduleId=0L;
    private Long[] seatId;
    private ComboOrderDto[] combos;
}
