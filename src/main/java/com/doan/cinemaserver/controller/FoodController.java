package com.doan.cinemaserver.controller;

import com.doan.cinemaserver.common.RestApiV1;
import com.doan.cinemaserver.common.VsResponseUtil;
import com.doan.cinemaserver.constant.ErrorMessage;
import com.doan.cinemaserver.constant.UrlConstant;
import com.doan.cinemaserver.domain.dto.combo.ComboRequestDto;
import com.doan.cinemaserver.domain.dto.food.FoodRequestDto;
import com.doan.cinemaserver.service.ComboService;
import com.doan.cinemaserver.service.FoodService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestApiV1
@RequiredArgsConstructor
public class FoodController {
    private final FoodService foodService;

    @Operation(summary = "API Create Food")
    @PostMapping(value = UrlConstant.Food.CREATE_FOOD)
    public ResponseEntity<?> createFood(@RequestParam @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD) String name) {
        return VsResponseUtil.success(foodService.createFood(name));
    }

    @Operation(summary = "API Get Food")
    @GetMapping(value = UrlConstant.Food.GET_FOOD)
    public ResponseEntity<?> getFood() {
        return VsResponseUtil.success(foodService.findAllFood());
    }
    @Operation(summary = "API Get Food By Page")
    @PostMapping(value = UrlConstant.Food.GET_FOOD_PAGE)
    public ResponseEntity<?> getFoodByPage(@RequestBody FoodRequestDto requestDto) {
        return VsResponseUtil.success(foodService.findFoodByPage(requestDto));
    }

    @Operation(summary = "API Delete Food")
    @DeleteMapping(value = UrlConstant.Food.DELETE_FOOD)
    public ResponseEntity<?> deleteFood(@PathVariable Long id) {
        return VsResponseUtil.success(foodService.deleteFood(id));
    }
}
