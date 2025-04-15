package com.doan.cinemaserver.controller;

import com.doan.cinemaserver.common.RestApiV1;
import com.doan.cinemaserver.common.VsResponseUtil;
import com.doan.cinemaserver.constant.ErrorMessage;
import com.doan.cinemaserver.constant.UrlConstant;
import com.doan.cinemaserver.domain.dto.customer.CustomerDto;
import com.doan.cinemaserver.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestApiV1
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @Operation(summary = "API Update Customer")
    @PatchMapping(UrlConstant.Customer.UPDATE_CUSTOMER)
    public ResponseEntity<?> updateCustomer(@Valid @RequestBody CustomerDto customerDto){
        return VsResponseUtil.success(customerService.updateCustomer(customerDto));
    }


    @Operation(summary = "API Update Customer Cinema")
    @PatchMapping(UrlConstant.Customer.UPDATE_CUSTOMER_CINEMA)
    public ResponseEntity<?> updateCustomerCinema(@RequestParam @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD) String username,@RequestParam Long cinemaId){
        if(cinemaId == null){
            cinemaId = 0L;
        }
        return VsResponseUtil.success(customerService.updateCinemaPicked(username,cinemaId));
    }

    @Operation(summary = "API Get Customer Cinema")
    @GetMapping(UrlConstant.Customer.GET_CUSTOMER_CINEMA)
    public ResponseEntity<?> getCinemaByCustomer(@RequestParam @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD) String username){
        return VsResponseUtil.success(customerService.loadCinemaByCustomer(username));
    }
}
