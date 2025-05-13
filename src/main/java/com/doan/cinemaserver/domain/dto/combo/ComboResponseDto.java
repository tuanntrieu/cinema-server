package com.doan.cinemaserver.domain.dto.combo;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComboResponseDto {
    private Long id;
    private String name;
    private String description;
    private Long price;
    private String image;
}
