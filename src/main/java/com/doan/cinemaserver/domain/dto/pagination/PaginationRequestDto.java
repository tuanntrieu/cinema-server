package com.doan.cinemaserver.domain.dto.pagination;

import com.doan.cinemaserver.constant.CommonConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaginationRequestDto {
    private Integer pageNo ;
    private Integer pageSize;
    private String sortBy;
    private Boolean isAscending;

    public PaginationRequestDto() {
        this.pageNo = CommonConstant.ZERO_INT_VALUE;
        this.pageSize = CommonConstant.PAGE_SIZE_DEFAULT;
        this.sortBy = "id";
        this.isAscending  = Boolean.FALSE;
    }

    public Integer getPageNo() {
        if (pageNo < 0) return CommonConstant.ZERO_INT_VALUE;
        return pageNo;
    }

    public Integer getPageSize() {
        if (pageSize < 1) {
            pageSize = CommonConstant.PAGE_SIZE_DEFAULT;
        }
        return pageSize;
    }
}
