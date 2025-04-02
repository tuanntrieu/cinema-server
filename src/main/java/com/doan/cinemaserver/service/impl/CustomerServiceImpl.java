package com.doan.cinemaserver.service.impl;

import com.doan.cinemaserver.constant.ErrorMessage;
import com.doan.cinemaserver.constant.SuccessMessage;
import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.customer.CustomerDto;
import com.doan.cinemaserver.domain.entity.Customer;
import com.doan.cinemaserver.domain.entity.User;
import com.doan.cinemaserver.exception.DataIntegrityViolationException;
import com.doan.cinemaserver.exception.NotFoundException;
import com.doan.cinemaserver.mapper.CustomerMapper;
import com.doan.cinemaserver.repository.CustomerRepository;
import com.doan.cinemaserver.repository.UserRepository;
import com.doan.cinemaserver.service.CustomerService;
import com.doan.cinemaserver.util.MessageSourceUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final MessageSourceUtil messageSourceUtil;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CommonResponseDto updateCustomer(CustomerDto customerDto) {
        User user = userRepository.findByEmail(customerDto.getEmail()).orElseThrow(
                ()->new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_USERNAME,new String[]{customerDto.getEmail()})
        );

        Customer customer = customerMapper.toCustomer(customerDto);
        customer.setId(user.getCustomer().getId());
        customer.setUser(user);
        customerRepository.save(customer);

        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.UPDATE_SUCCESS,null));
    }
}
