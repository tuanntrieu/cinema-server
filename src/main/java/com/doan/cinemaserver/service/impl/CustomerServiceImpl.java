package com.doan.cinemaserver.service.impl;

import com.doan.cinemaserver.constant.ErrorMessage;
import com.doan.cinemaserver.constant.RoleConstant;
import com.doan.cinemaserver.constant.SuccessMessage;
import com.doan.cinemaserver.domain.dto.cinema.CinemaResponseDto;
import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.customer.CustomerDto;
import com.doan.cinemaserver.domain.dto.customer.CustomerResponseDto;
import com.doan.cinemaserver.domain.dto.customer.CustomerSearchRequestDto;
import com.doan.cinemaserver.domain.dto.pagination.PaginationResponseDto;
import com.doan.cinemaserver.domain.entity.*;
import com.doan.cinemaserver.exception.NotFoundException;
import com.doan.cinemaserver.mapper.CustomerMapper;
import com.doan.cinemaserver.repository.CinemaRepository;
import com.doan.cinemaserver.repository.CustomerRepository;
import com.doan.cinemaserver.repository.UserRepository;
import com.doan.cinemaserver.service.CustomerService;
import com.doan.cinemaserver.util.MessageSourceUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
                () -> new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_USERNAME, new String[]{customerDto.getEmail()})
        );

        Customer customer = customerMapper.toCustomer(customerDto);
        customer.setId(user.getCustomer().getId());
        customer.setUser(user);
        customer.setCinemaPicked(user.getCustomer().getCinemaPicked());
        customerRepository.save(customer);

        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.UPDATE_SUCCESS, null));
    }

    @Override
    public CommonResponseDto updateCinemaPicked(String username, Long cinemaId) {
        User user = userRepository.findByEmail(username).orElseThrow(
                () -> new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_USERNAME, new String[]{username})
        );

        Cinema cinema = cinemaRepository.findById(cinemaId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Cinema.ERR_NOT_FOUND_CINEMA, new String[]{cinemaId.toString()})
        );

        Customer customer = user.getCustomer();
        customer.setCinemaPicked(cinemaId);
        customerRepository.save(customer);
        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.UPDATE_SUCCESS, null));
    }

    @Override
    public CommonResponseDto loadCinemaByCustomer(String username) {

        User user = userRepository.findByEmail(username).orElseThrow(
                () -> new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_USERNAME, new String[]{username})
        );
        Long cinemaId = user.getCustomer().getCinemaPicked();
        if (cinemaId == null) {
            return new CommonResponseDto("Chưa chọn rạp");
        } else {
            Cinema cinema = cinemaRepository.findById(cinemaId).orElseThrow(
                    () -> new NotFoundException(ErrorMessage.Cinema.ERR_NOT_FOUND_CINEMA, new String[]{cinemaId.toString()})
            );
            CinemaResponseDto cinemaResponseDto = CinemaResponseDto.builder()
                    .id(cinema.getId())
                    .name(cinema.getCinemaName())
                    .build();
            return new CommonResponseDto("Ok", cinemaResponseDto);

        }

    }

    @Override
    public Customer getCustomerInfor(String email) {
        return customerRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Customer.ERR_NOT_FOUND_EMAIL, new String[]{email})
        );
    }

    @Override
    public PaginationResponseDto<CustomerResponseDto> customersPage(CustomerSearchRequestDto requestDto) {
        Sort sort = requestDto.getIsAscending() != null && requestDto.getIsAscending()
                ? Sort.by(requestDto.getSortBy()).ascending()
                : Sort.by(requestDto.getSortBy()).descending();
        Pageable pageable = PageRequest.of(requestDto.getPageNo(), requestDto.getPageSize(), sort);
        Page<Customer> page = customerRepository.findAll(new Specification<Customer>() {
            List<Predicate> predicates = new ArrayList<>();

            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (requestDto.getName() != null && !requestDto.getName().trim().isEmpty()) {
                    predicates.add(criteriaBuilder.like(
                            root.get(Customer_.FULL_NAME).as(String.class),
                            "%" + requestDto.getName() + "%"
                    ));

                }
                predicates.add(criteriaBuilder.notEqual(root.get(Customer_.USER).get(User_.ROLE).get(Role_.CODE), RoleConstant.ROLE_ADMIN));
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        }, pageable);
        return new PaginationResponseDto<>(
                page.getTotalElements(), page.getTotalPages(), requestDto.getPageNo(), requestDto.getPageSize(), page.getSort().toString(),
                page.getContent().stream().map(
                        customer -> {
                            return CustomerResponseDto.builder()
                                    .id(customer.getId())
                                    .name(customer.getFullName())
                                    .phone(customer.getPhoneNumber() == null ? "" : customer.getPhoneNumber())
                                    .email(customer.getUser().getEmail())
                                    .gender(customer.getGender() == null ? "" : customer.getGender().getName())
                                    .countTickets(customer.getTickets().size())
                                    .createdAt(customer.getCreatedDate())
                                    .isLocked(customer.getUser().getIsLocked())
                                    .build();
                        }
                ).collect(Collectors.toList())
        );
    }

    @Override
    public CommonResponseDto lockAccount(Long customerId) {
        customerRepository.lockAccount(customerId);
        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.UPDATE_SUCCESS, null));
    }

    @Override
    public CommonResponseDto unlockAccount(Long customerId) {
        customerRepository.unLockAccount(customerId);
        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.UPDATE_SUCCESS, null));
    }


}
