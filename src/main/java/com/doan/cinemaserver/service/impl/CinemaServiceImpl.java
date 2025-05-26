package com.doan.cinemaserver.service.impl;

import com.doan.cinemaserver.constant.ErrorMessage;
import com.doan.cinemaserver.constant.SuccessMessage;
import com.doan.cinemaserver.domain.dto.cinema.CinemaRequestDto;
import com.doan.cinemaserver.domain.dto.cinema.CinemaResponseDto;
import com.doan.cinemaserver.domain.dto.cinema.CinemaSearchRequestDto;
import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.pagination.PaginationResponseDto;
import com.doan.cinemaserver.domain.entity.Cinema;
import com.doan.cinemaserver.domain.entity.Cinema_;
import com.doan.cinemaserver.domain.entity.Movie;
import com.doan.cinemaserver.domain.entity.Movie_;
import com.doan.cinemaserver.exception.NotFoundException;
import com.doan.cinemaserver.mapper.CinemaMapper;
import com.doan.cinemaserver.repository.CinemaRepository;
import com.doan.cinemaserver.service.CinemaService;
import com.doan.cinemaserver.util.MessageSourceUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CinemaServiceImpl implements CinemaService {
    private final CinemaMapper mapper;
    private final MessageSourceUtil messageSourceUtil;
    private final CinemaRepository cinemaRepository;

    @Override
    @Transactional
    public CommonResponseDto createCinema(CinemaRequestDto cinemaRequestDto) {

        cinemaRepository.save(mapper.toCinema(cinemaRequestDto));
        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.CREATE_SUCCESS,null));
    }

    @Override
    @Transactional
    public CommonResponseDto updateCinema(Long cinemaId, CinemaRequestDto cinemaRequestDto) {

        Cinema cinema = cinemaRepository.findById(cinemaId).orElseThrow(
                ()-> new NotFoundException(ErrorMessage.Cinema.ERR_NOT_FOUND_CINEMA,new String[]{cinemaId.toString()})
        );
        Cinema cinemaUpdate = mapper.toCinema(cinemaRequestDto);
        cinemaUpdate.setId(cinemaId);

        cinemaRepository.save(cinemaUpdate);

        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.UPDATE_SUCCESS,null));
    }

    @Override
    public List<String> loadProvince() {
        return cinemaRepository.loadProvince();
    }

    @Override
    public List<CinemaResponseDto> loadCinemasByProvince(String province) {
        return cinemaRepository.loadNameCinemaByProvince(province);
    }

    @Override
    public List<CinemaResponseDto> loadAllCinemas() {
        return cinemaRepository.loadAllCinemas();
    }

    @Override
    public PaginationResponseDto<Cinema> getAllCinema(CinemaSearchRequestDto requestDto) {
        Sort sort = requestDto.getIsAscending() != null && requestDto.getIsAscending()
                ? Sort.by(requestDto.getSortBy()).ascending()
                : Sort.by(requestDto.getSortBy()).descending();
        Pageable pageable = PageRequest.of(requestDto.getPageNo(), requestDto.getPageSize(), sort);
        Page<Cinema> cinemaPage = cinemaRepository.findAll(new Specification<Cinema>() {
            @Override
            public Predicate toPredicate(Root<Cinema> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (requestDto.getName() != null && !requestDto.getName().trim().isEmpty()) {
                    predicates.add(criteriaBuilder.like(
                            root.get(Cinema_.CINEMA_NAME).as(String.class),
                            "%" + requestDto.getName() + "%"
                    ));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        }, pageable);
        return new PaginationResponseDto<>(
                cinemaPage.getTotalElements(), cinemaPage.getTotalPages(), cinemaPage.getNumber(), requestDto.getPageSize(), sort.toString(), cinemaPage.getContent()
        );
    }


}
