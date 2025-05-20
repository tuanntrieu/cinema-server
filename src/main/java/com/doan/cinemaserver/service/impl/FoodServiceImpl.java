package com.doan.cinemaserver.service.impl;

import com.doan.cinemaserver.constant.ErrorMessage;
import com.doan.cinemaserver.constant.SuccessMessage;
import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.food.FoodRequestDto;
import com.doan.cinemaserver.domain.dto.pagination.PaginationResponseDto;
import com.doan.cinemaserver.domain.entity.Food;
import com.doan.cinemaserver.domain.entity.Food_;
import com.doan.cinemaserver.exception.NotFoundException;
import com.doan.cinemaserver.repository.ComboDetailRepository;
import com.doan.cinemaserver.repository.FoodRepository;
import com.doan.cinemaserver.service.FoodService;
import com.doan.cinemaserver.util.MessageSourceUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {
    private final FoodRepository foodRepository;
    private final MessageSourceUtil messageSourceUtil;
    private final ComboDetailRepository comboDetailRepository;

    @Override
    public CommonResponseDto createFood(String name) {
        foodRepository.save(Food.builder().name(name).build());
        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.CREATE_SUCCESS,null));
    }

    @Override
    @Transactional
    @Modifying
    public CommonResponseDto deleteFood(Long foodId) {
        Food food = foodRepository.findById(foodId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Food.ERR_NOT_FOUND_FOOD,
                        new String[]{foodId.toString()})
        );
        food.getComboDetails().forEach(comboDetail -> {
            comboDetailRepository.delete(comboDetail);
        });


        foodRepository.delete(food);
        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.DELETE_SUCCESS,null));

    }

    @Override
    public List<Food> findAllFood() {
        return foodRepository.findAll();
    }

    @Override
    public PaginationResponseDto<Food> findFoodByPage(FoodRequestDto request) {
        Sort sort = request.getIsAscending() != null && request.getIsAscending()
                ? Sort.by(request.getSortBy()).ascending()
                : Sort.by(request.getSortBy()).descending();
        Pageable pageable = PageRequest.of(request.getPageNo(), request.getPageSize(), sort);
        Page<Food> foodPage = foodRepository.findAll(
                new Specification<Food>() {
                    List<Predicate> predicates = new ArrayList<>();
                    @Override
                    public Predicate toPredicate(Root<Food> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                        if (request.getName() != null && !request.getName().trim().isEmpty()) {
                            predicates.add(criteriaBuilder.like(
                                    root.get(Food_.NAME).as(String.class),
                                    "%" + request.getName() + "%"
                            ));
                        }
                        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                    }
                }
                , pageable);
        return new PaginationResponseDto<>(
                foodPage.getTotalElements(), foodPage.getTotalPages(), request.getPageNo(), request.getPageSize(), foodPage.getSort().toString(), foodPage.getContent()
        );
    }
}
