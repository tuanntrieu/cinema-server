package com.doan.cinemaserver.controller;

import com.doan.cinemaserver.common.RestApiV1;
import com.doan.cinemaserver.common.VsResponseUtil;
import com.doan.cinemaserver.constant.UrlConstant;
import com.doan.cinemaserver.domain.dto.combo.ComboRequestDto;
import com.doan.cinemaserver.domain.dto.combo.ComboSearchRequestDto;
import com.doan.cinemaserver.service.ComboService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestApiV1
@RequiredArgsConstructor
public class ComboController {
    private final ComboService comboService;


    @Operation(summary = "API Create Combo")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = UrlConstant.Combo.CREATE_COMBO, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createCombo(
            @Parameter(
                    description = "ComboRequestDto JSON string",
                    required = true,
                    schema = @Schema(implementation = ComboRequestDto.class)
            ) @RequestPart("data") String data,
            @RequestPart("file") MultipartFile file) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ComboRequestDto requestDto = objectMapper.readValue(data, ComboRequestDto.class);
        return VsResponseUtil.success(comboService.creatCombo(requestDto, file));
    }

    @Operation(summary = "API Get Combo")
    @PostMapping(UrlConstant.Combo.GET_COMBO)
    public ResponseEntity<?> getCombo(@RequestBody ComboSearchRequestDto requestDto)  {
        return VsResponseUtil.success(comboService.getAllCombo(requestDto));
    }

    @Operation(summary = "API Delete Combo")
    @DeleteMapping(UrlConstant.Combo.DELETE_COMBO)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCombo(@PathVariable Long id)  {
        return VsResponseUtil.success(comboService.deleteCombo(id));
    }

}
