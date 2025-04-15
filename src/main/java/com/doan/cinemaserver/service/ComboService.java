package com.doan.cinemaserver.service;

import com.doan.cinemaserver.domain.dto.combo.ComboRequestDto;
import com.doan.cinemaserver.domain.dto.combo.ComboResponseDto;
import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.pagination.PaginationResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface ComboService {
    public CommonResponseDto creatCombo(ComboRequestDto comboRequestDto, MultipartFile file);

    public PaginationResponseDto<ComboResponseDto> getAllCombo();


    public CommonResponseDto deleteCombo(Long id);

}
