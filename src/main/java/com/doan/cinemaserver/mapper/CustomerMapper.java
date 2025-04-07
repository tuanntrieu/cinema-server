package com.doan.cinemaserver.mapper;

import com.doan.cinemaserver.domain.dto.customer.CustomerDto;
import com.doan.cinemaserver.domain.entity.Customer;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface CustomerMapper {

    Customer toCustomer(CustomerDto customerDto);
}
