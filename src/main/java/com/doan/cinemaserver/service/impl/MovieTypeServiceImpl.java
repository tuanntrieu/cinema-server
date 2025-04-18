package com.doan.cinemaserver.service.impl;

import com.doan.cinemaserver.constant.ErrorMessage;
import com.doan.cinemaserver.constant.SuccessMessage;
import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.movietype.MovieTypeRequestDto;
import com.doan.cinemaserver.domain.dto.pagination.PaginationResponseDto;
import com.doan.cinemaserver.domain.entity.MovieType;
import com.doan.cinemaserver.domain.entity.MovieType_;
import com.doan.cinemaserver.exception.NotFoundException;
import com.doan.cinemaserver.repository.MovieTypeRepository;
import com.doan.cinemaserver.service.MovieTypeService;
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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieTypeServiceImpl implements MovieTypeService {
    private final MovieTypeRepository movieTypeRepository;
    private final MessageSourceUtil messageSourceUtil;

    @Override
    public CommonResponseDto addMovieType(MovieTypeRequestDto movieTypeRequestDto) {
        movieTypeRepository.save(
                MovieType.builder()
                        .name(movieTypeRequestDto.getName())
                        .build()
        );
        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.CREATE_SUCCESS, null));
    }

    @Override
    public CommonResponseDto updateMovieType(Long movieTypeId, MovieTypeRequestDto movieTypeRequestDto) {
        MovieType movieType = movieTypeRepository.findById(movieTypeId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.MovieType.ERR_NOT_FOUND_MOVIE_TYPE, new String[]{movieTypeId.toString()})
        );
        movieType.setName(movieTypeRequestDto.getName());
        movieTypeRepository.save(movieType);
        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.UPDATE_SUCCESS, null));

    }

    @Override
    public PaginationResponseDto<MovieType> getMovieTypePage(String name) {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<MovieType> movieTypePage = movieTypeRepository.findAll(
                new Specification<MovieType>() {
                    List<Predicate> predicates = new ArrayList<>();

                    @Override
                    public Predicate toPredicate(Root<MovieType> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                        if (name != null) {
                            predicates.add(criteriaBuilder.like(
                                    root.get(MovieType_.NAME).as(String.class),
                                    "%" + name + "%"
                            ));
                        }
                        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                    }
                }
                , pageable);
        return new PaginationResponseDto<>(
                movieTypePage.getTotalElements(), movieTypePage.getTotalPages(), movieTypePage.getNumber(), movieTypePage.getNumberOfElements(), movieTypePage.getSort().toString(), movieTypePage.getContent()
        );
    }

    @Override
    public List<MovieType> getAllMovieType() {
        return movieTypeRepository.findAll();
    }
}
