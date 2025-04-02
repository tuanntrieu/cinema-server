package com.doan.cinemaserver.service;

import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.customer.CustomerDto;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {
    public CommonResponseDto updateCustomer(CustomerDto customerDto);
}
