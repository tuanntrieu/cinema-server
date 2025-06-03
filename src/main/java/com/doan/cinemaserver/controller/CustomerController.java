package com.doan.cinemaserver.controller;

import com.doan.cinemaserver.common.RestApiV1;
import com.doan.cinemaserver.common.VsResponseUtil;
import com.doan.cinemaserver.constant.ErrorMessage;
import com.doan.cinemaserver.constant.UrlConstant;
import com.doan.cinemaserver.domain.dto.customer.CustomerDto;
import com.doan.cinemaserver.domain.dto.customer.CustomerSearchRequestDto;
import com.doan.cinemaserver.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestApiV1
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @Operation(summary = "API Update Customer")
    @PatchMapping(UrlConstant.Customer.UPDATE_CUSTOMER)
    public ResponseEntity<?> updateCustomer(@Valid @RequestBody CustomerDto customerDto){
        return VsResponseUtil.success(customerService.updateCustomer(customerDto));
    }

    @Operation(summary = "API Lock Account")
    @PatchMapping(UrlConstant.Customer.LOCK_ACCOUNT)
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> lockAccount(@PathVariable long id){
        return VsResponseUtil.success(customerService.lockAccount(id));
    }
    @Operation(summary = "API UnLock Account")
    @PatchMapping(UrlConstant.Customer.UN_LOCK_ACCOUNT)
    public ResponseEntity<?> unLockAccount(@PathVariable long id){
        return VsResponseUtil.success(customerService.unlockAccount(id));
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

    @Operation(summary = "API Get Customer Infor")
    @GetMapping(UrlConstant.Customer.GET_CUSTOMER_INFOR)
    public ResponseEntity<?> getCustomerInfor(@RequestParam @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD) String email){
        return VsResponseUtil.success(customerService.getCustomerInfor(email));
    }
    @Operation(summary = "API Get All Customer ")
    @PostMapping(UrlConstant.Customer.GET_ALL_CUSTOMER)
    public ResponseEntity<?> getAllCustomers(@RequestBody CustomerSearchRequestDto requestDto){
        return VsResponseUtil.success(customerService.customersPage(requestDto));
    }
}
