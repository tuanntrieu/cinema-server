package com.doan.cinemaserver.domain.dto.ticket;

import com.doan.cinemaserver.domain.dto.pagination.PaginationRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketRequestDto extends PaginationRequestDto {
    private Long customerId = 0L;
    private LocalDate dateOrder;
}
