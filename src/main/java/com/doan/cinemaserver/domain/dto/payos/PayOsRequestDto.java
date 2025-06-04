package com.doan.cinemaserver.domain.dto.payos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayOsRequestDto {
    private Integer orderCode;
    private Long amount;
    private String description;
    private String cancelUrl;
    private String returnUrl;
    private String signature;
    private Integer expiredAt;
}
