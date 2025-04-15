package com.doan.cinemaserver.service.impl;

import com.doan.cinemaserver.constant.ErrorMessage;
import com.doan.cinemaserver.constant.SuccessMessage;
import com.doan.cinemaserver.domain.dto.combo.ComboDetailRequestDto;
import com.doan.cinemaserver.domain.dto.combo.ComboRequestDto;
import com.doan.cinemaserver.domain.dto.combo.ComboResponseDto;
import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.pagination.PaginationResponseDto;
import com.doan.cinemaserver.domain.entity.Combo;
import com.doan.cinemaserver.domain.entity.ComboDetail;
import com.doan.cinemaserver.domain.entity.Food;
import com.doan.cinemaserver.exception.NotFoundException;
import com.doan.cinemaserver.repository.ComboDetailRepository;
import com.doan.cinemaserver.repository.ComboRepository;
import com.doan.cinemaserver.repository.FoodRepository;
import com.doan.cinemaserver.service.ComboService;
import com.doan.cinemaserver.util.MessageSourceUtil;
import com.doan.cinemaserver.util.UploadFileUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComboServiceImpl implements ComboService {
    private final ComboRepository comboRepository;
    private final ComboDetailRepository comboDetailRepository;
    private final FoodRepository foodRepository;
    private final MessageSourceUtil messageSourceUtil;
    private final UploadFileUtil uploadFileUtil;

    @Override
    @Transactional
    public CommonResponseDto creatCombo(ComboRequestDto comboRequestDto, MultipartFile file) {

        Combo combo = new Combo();
        combo.setName(comboRequestDto.getName());
        StringBuilder descriptionBui = new StringBuilder();
        List<ComboDetail> comboDetails = new ArrayList<>();
        for (ComboDetailRequestDto detailDto : comboRequestDto.getComboDetails()) {
            Food food = foodRepository.findById(detailDto.getFoodId())
                    .orElseThrow(() -> new NotFoundException(
                            ErrorMessage.Food.ERR_NOT_FOUND_FOOD,
                            new String[]{detailDto.getFoodId().toString()}
                    ));
            descriptionBui.append(food.getName()).append("x").append(detailDto.getQuantity()).append(", ");
            ComboDetail comboDetail = new ComboDetail();
            comboDetail.setQuantity(detailDto.getQuantity());
            comboDetail.setCombo(combo);
            comboDetail.setFood(food);
            comboDetails.add(comboDetail);
        }
        String desciption = !descriptionBui.isEmpty()
                ? descriptionBui.substring(0, descriptionBui.length() - 2)
                : "";
        combo.setDescription(desciption);
        combo.setImage(uploadFileUtil.uploadFile(file));
        combo.setComboDetail(comboDetails);
        combo.setPrice(comboRequestDto.getPrice());
        comboRepository.save(combo);
        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.CREATE_SUCCESS, null));
    }

    @Override
    public PaginationResponseDto<ComboResponseDto> getAllCombo() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<Combo> comboPage = comboRepository.findAll(pageable);
        List<ComboResponseDto> comboList = comboPage.stream().map(
                combo -> {
                    return ComboResponseDto.builder()
                            .id(combo.getId())
                            .price(combo.getPrice())
                            .name(combo.getName())
                            .description(combo.getDescription())
                            .build();
                }
        ).toList();
        return new PaginationResponseDto<>(
                comboPage.getTotalElements(), comboPage.getTotalPages(), comboPage.getNumber(), comboPage.getNumberOfElements(), comboPage.getSort().toString(), comboList
        );
    }


    @Override
    public CommonResponseDto deleteCombo(Long id) {
        Combo combo = comboRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Combo.ERR_NOT_FOUND_COMBO, new String[]{id.toString()})
        );
        uploadFileUtil.destroyFileWithUrl(combo.getImage());
        comboRepository.delete(combo);
        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.DELETE_SUCCESS, null));
    }

}
