package com.doan.cinemaserver.service;

import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.customer.CustomerDto;
import com.doan.cinemaserver.domain.entity.Customer;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {
    public CommonResponseDto updateCustomer(CustomerDto customerDto);
    public CommonResponseDto updateCinemaPicked(String username, Long cinemaId) ;
    public CommonResponseDto loadCinemaByCustomer(String username);
    public Customer getCustomerInfor(String email);
}
