package com.doan.cinemaserver.service.impl;

import com.doan.cinemaserver.constant.ErrorMessage;
import com.doan.cinemaserver.constant.SuccessMessage;
import com.doan.cinemaserver.domain.dto.cinema.*;
import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.pagination.PaginationRequestDto;
import com.doan.cinemaserver.domain.dto.pagination.PaginationResponseDto;
import com.doan.cinemaserver.domain.dto.room.RoomResponseDto;
import com.doan.cinemaserver.domain.entity.Cinema;
import com.doan.cinemaserver.domain.entity.Cinema_;
import com.doan.cinemaserver.domain.entity.Room;
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
import java.util.stream.Collectors;

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
        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.CREATE_SUCCESS, null));
    }

    @Override
    @Transactional
    public CommonResponseDto updateCinema(Long cinemaId, CinemaRequestDto cinemaRequestDto) {

        Cinema cinema = cinemaRepository.findById(cinemaId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Cinema.ERR_NOT_FOUND_CINEMA, new String[]{cinemaId.toString()})
        );
        Cinema cinemaUpdate = mapper.toCinema(cinemaRequestDto);
        cinemaUpdate.setId(cinemaId);

        cinemaRepository.save(cinemaUpdate);

        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.UPDATE_SUCCESS, null));
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
    public PaginationResponseDto<CinemaResponsePageDto> getAllCinema(CinemaSearchRequestDto requestDto) {
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
                cinemaPage.getTotalElements(), cinemaPage.getTotalPages(), requestDto.getPageNo(), requestDto.getPageSize(), sort.toString(),
                cinemaPage.getContent().stream().map(
                        cinema -> CinemaResponsePageDto.builder()
                                .cinemaName(cinema.getCinemaName())
                                .id(cinema.getId())
                                .detailAddress(cinema.getDetailAddress())
                                .district(cinema.getDistrict())
                                .ward(cinema.getWard())
                                .province(cinema.getProvince())
                                .sumRoom(cinema.getRooms().size())
                                .hotline(cinema.getHotline())
                                .build()
                ).collect(Collectors.toList())
        );
    }

    @Override
    public Cinema getCinemaDetail(Long id) {
        Cinema cinema = cinemaRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Cinema.ERR_NOT_FOUND_CINEMA, new String[]{id.toString()})
        );
        return cinema;
    }

    @Override
    public PaginationResponseDto<RoomResponseDto> getRoomByCinema( CinemaGetRoomRequestDto requestDto) {
        Sort sort = requestDto.getIsAscending() != null && requestDto.getIsAscending()
                ? Sort.by(requestDto.getSortBy()).ascending()
                : Sort.by(requestDto.getSortBy()).descending();
        Pageable pageable = PageRequest.of(requestDto.getPageNo(), requestDto.getPageSize(), sort);
        Page<Room> rooms= cinemaRepository.findRoomsByCinema(requestDto.getCinemaId(),pageable);
        return new PaginationResponseDto<>(
                rooms.getTotalElements(), rooms.getTotalPages(), requestDto.getPageNo(), requestDto.getPageSize(), sort.toString(),
                rooms.getContent().stream().map(
                        room -> RoomResponseDto.builder()
                                .id(room.getId())
                                .name(room.getName())
                                .roomType(room.getRoomType().getRoomType())
                                .sumSeat((long) room.getSeats().size())
                                .build()
                ).collect(Collectors.toList()));
    }


}
