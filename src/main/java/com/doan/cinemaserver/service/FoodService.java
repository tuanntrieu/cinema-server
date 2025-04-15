package com.doan.cinemaserver.service;

import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.pagination.PaginationResponseDto;
import com.doan.cinemaserver.domain.entity.Food;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FoodService {
    public CommonResponseDto createFood(String name);
    public CommonResponseDto deleteFood(Long foodId);
    public List<Food> findAllFood();
    public PaginationResponseDto<Food> findFoodByPage();
}
