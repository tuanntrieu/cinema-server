package com.doan.cinemaserver.domain.dto.customer;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponseDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String gender;
    private LocalDateTime createdAt;
    private int countTickets;
    private Boolean isLocked;
}
