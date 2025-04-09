package com.doan.cinemaserver.service.impl;

import com.doan.cinemaserver.constant.ErrorMessage;
import com.doan.cinemaserver.constant.SuccessMessage;
import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.movie.MovieRequestDto;
import com.doan.cinemaserver.domain.entity.Movie;
import com.doan.cinemaserver.domain.entity.MovieType;
import com.doan.cinemaserver.exception.DataIntegrityViolationException;
import com.doan.cinemaserver.exception.NotFoundException;
import com.doan.cinemaserver.mapper.MovieMapper;
import com.doan.cinemaserver.repository.MovieRepository;
import com.doan.cinemaserver.repository.MovieTypeRepository;
import com.doan.cinemaserver.repository.SeatRepository;
import com.doan.cinemaserver.service.MovieService;
import com.doan.cinemaserver.util.MessageSourceUtil;
import com.doan.cinemaserver.util.UploadFileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final SeatRepository seatRepository;
    private final MovieMapper movieMapper;
    private final MovieTypeRepository movieTypeRepository;
    private final MessageSourceUtil messageSourceUtil;
    private final UploadFileUtil uploadFileUtil;

    @Override
    @Transactional
    public CommonResponseDto createMovie(MovieRequestDto movieRequestDto, MultipartFile image) {

        LocalDate releaseDate = toLocalDate(movieRequestDto.getReleaseDate());
        LocalDate endDate = toLocalDate(movieRequestDto.getEndDate());
        LocalDate today = LocalDate.now();


        if (releaseDate.isBefore(today)) {
            throw new DataIntegrityViolationException(ErrorMessage.Movie.ERR_INVALID_TIME);
        }


        if (releaseDate.isAfter(endDate)) {
            throw new DataIntegrityViolationException(ErrorMessage.Movie.ERR_INVALID_TIME);
        }


        if (movieRequestDto.getDuration() <= 0) {
            throw new DataIntegrityViolationException(ErrorMessage.Movie.ERR_INVALID_DURATION);
        }

        Movie movie = movieMapper.toMovie(movieRequestDto);

        if (image != null && !image.isEmpty()) {
            movie.setImage(uploadFileUtil.uploadFile(image));
        } else {
            throw new DataIntegrityViolationException(ErrorMessage.INVALID_NOT_BLANK_FIELD, new String[]{"image"});
        }

        LocalDateTime now = LocalDateTime.now();
        movie.setCreatedDate(now);
        movie.setLastModifiedDate(now);
        movie.setTypes(new ArrayList<>());

        movieRepository.save(movie);

        movieRequestDto.getMovieTypeId().forEach(id -> {
            MovieType type = movieTypeRepository.findById(id).orElseThrow(() ->
                    new NotFoundException(ErrorMessage.MovieType.ERR_NOT_FOUND_MOVIE_TYPE, new String[]{id.toString()}));
            type.getMovies().add(movie);
            movieTypeRepository.save(type);
        });

        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.CREATE_SUCCESS, null));
    }

    private LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

}
