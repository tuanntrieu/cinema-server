package com.doan.cinemaserver.service.impl;

import com.doan.cinemaserver.constant.ErrorMessage;
import com.doan.cinemaserver.constant.SuccessMessage;
import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.movietype.MovieTypeRequestDto;
import com.doan.cinemaserver.domain.entity.MovieType;
import com.doan.cinemaserver.exception.NotFoundException;
import com.doan.cinemaserver.repository.MovieTypeRepository;
import com.doan.cinemaserver.service.MovieTypeService;
import com.doan.cinemaserver.util.MessageSourceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.CREATE_SUCCESS,null));
    }

    @Override
    public CommonResponseDto updateMovieType(Long movieTypeId, MovieTypeRequestDto movieTypeRequestDto) {
        MovieType movieType = movieTypeRepository.findById(movieTypeId).orElseThrow(
                ()->new NotFoundException(ErrorMessage.MovieType.ERR_NOT_FOUND_MOVIE_TYPE,new String[]{movieTypeId.toString()})
        );
        movieType.setName(movieTypeRequestDto.getName());
        movieTypeRepository.save(movieType);
        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.UPDATE_SUCCESS,null));

    }
}
