package com.doan.cinemaserver.service;

import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.customer.CustomerDto;
import com.doan.cinemaserver.domain.dto.customer.CustomerResponseDto;
import com.doan.cinemaserver.domain.dto.customer.CustomerSearchRequestDto;
import com.doan.cinemaserver.domain.dto.pagination.PaginationResponseDto;
import com.doan.cinemaserver.domain.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {
    public CommonResponseDto updateCustomer(CustomerDto customerDto);
    public CommonResponseDto updateCinemaPicked(String username, Long cinemaId) ;
    public CommonResponseDto loadCinemaByCustomer(String username);
    public Customer getCustomerInfor(String email);
    public PaginationResponseDto<CustomerResponseDto> customersPage(CustomerSearchRequestDto requestDto);
    public CommonResponseDto lockAccount(Long customerId);
    public CommonResponseDto unlockAccount(Long customerId);
}
