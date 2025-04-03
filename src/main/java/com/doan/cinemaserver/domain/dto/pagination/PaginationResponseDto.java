package com.doan.cinemaserver.domain.dto.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
