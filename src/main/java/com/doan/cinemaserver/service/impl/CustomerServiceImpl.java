package com.doan.cinemaserver.service.impl;

import com.doan.cinemaserver.constant.ErrorMessage;
import com.doan.cinemaserver.constant.SuccessMessage;
import com.doan.cinemaserver.domain.dto.cinema.CinemaResponseDto;
import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.customer.CustomerDto;
import com.doan.cinemaserver.domain.entity.Cinema;
import com.doan.cinemaserver.domain.entity.Customer;
import com.doan.cinemaserver.domain.entity.User;
import com.doan.cinemaserver.exception.NotFoundException;
import com.doan.cinemaserver.mapper.CustomerMapper;
import com.doan.cinemaserver.repository.CinemaRepository;
import com.doan.cinemaserver.repository.CustomerRepository;
import com.doan.cinemaserver.repository.UserRepository;
import com.doan.cinemaserver.service.CustomerService;
import com.doan.cinemaserver.util.MessageSourceUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final MessageSourceUtil messageSourceUtil;
    private final UserRepository userRepository;
    private final CinemaRepository cinemaRepository;

    @Override
    @Transactional
    public CommonResponseDto updateCustomer(CustomerDto customerDto) {
        User user = userRepository.findByEmail(customerDto.getEmail()).orElseThrow(
                ()->new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_USERNAME, new String[]{customerDto.getEmail()})
        );

        Customer customer = customerMapper.toCustomer(customerDto);
        customer.setId(user.getCustomer().getId());
        customer.setUser(user);
        customer.setCinemaPicked(user.getCustomer().getCinemaPicked());
        customerRepository.save(customer);

        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.UPDATE_SUCCESS,null));
    }

    @Override
    public CommonResponseDto updateCinemaPicked(String username, Long cinemaId) {
        User user = userRepository.findByEmail(username).orElseThrow(
                ()->new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_USERNAME,new String[]{username})
        );

        Cinema cinema = cinemaRepository.findById(cinemaId).orElseThrow(
                ()-> new NotFoundException(ErrorMessage.Cinema.ERR_NOT_FOUND_CINEMA,new String[]{cinemaId.toString()})
        );

        Customer customer = user.getCustomer();
        customer.setCinemaPicked(cinemaId);
        customerRepository.save(customer);
        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.UPDATE_SUCCESS,null));
    }

    @Override
    public CommonResponseDto loadCinemaByCustomer(String username) {

        User user = userRepository.findByEmail(username).orElseThrow(
                ()->new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_USERNAME,  new String[]{username})
        );
        Long cinemaId = user.getCustomer().getCinemaPicked();
        if (cinemaId == null) {
            return new CommonResponseDto("Chưa chọn rạp");
        }else{
            Cinema cinema = cinemaRepository.findById(cinemaId).orElseThrow(
                    ()-> new NotFoundException(ErrorMessage.Cinema.ERR_NOT_FOUND_CINEMA, new String[]{cinemaId.toString()})
            );
            CinemaResponseDto cinemaResponseDto = CinemaResponseDto.builder()
                    .id(cinema.getId())
                    .name(cinema.getCinemaName())
                    .build();
            return new CommonResponseDto("Ok",cinemaResponseDto);

        }

    }

    @Override
    public Customer getCustomerInfor(String email) {
        return customerRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Customer.ERR_NOT_FOUND_EMAIL, new String[]{email})
        );
    }


}
