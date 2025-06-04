package com.doan.cinemaserver.domain.dto.payos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUrlRequestDto {
    private Long amount;
    private String cancelUrl;
    private String returnUrl;
}
