package com.doan.cinemaserver.domain.dto.pagination;

import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PaginationResponseDto<T> {
    private Long totalElements;

    private Integer totalPages;

    private Integer pageNo;

    private Integer pageSize;

    private String sort;

    private List<T> items;

    public List<T> getItems() {
        return items == null ? null : new ArrayList<>(items);
    }
}
