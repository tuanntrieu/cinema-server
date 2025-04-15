package com.doan.cinemaserver.domain.dto.ticket;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComboTicketResponseDto {
    private String name;
    private Long price;
    private int quantity;
}
