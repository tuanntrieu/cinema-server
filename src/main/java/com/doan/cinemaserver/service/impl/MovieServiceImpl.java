package com.doan.cinemaserver.service.impl;

import com.doan.cinemaserver.constant.ErrorMessage;
import com.doan.cinemaserver.constant.SuccessMessage;
import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.movie.MovieRequestDto;
import com.doan.cinemaserver.domain.dto.movie.MovieResponseDto;
import com.doan.cinemaserver.domain.dto.movie.MovieSearchRequestDto;
import com.doan.cinemaserver.domain.dto.pagination.PaginationResponseDto;
import com.doan.cinemaserver.domain.entity.Cinema;
import com.doan.cinemaserver.domain.entity.Movie;
import com.doan.cinemaserver.domain.entity.MovieType;
import com.doan.cinemaserver.exception.DataIntegrityViolationException;
import com.doan.cinemaserver.exception.InvalidException;
import com.doan.cinemaserver.exception.NotFoundException;
import com.doan.cinemaserver.mapper.MovieMapper;
import com.doan.cinemaserver.repository.CinemaRepository;
import com.doan.cinemaserver.repository.MovieRepository;
import com.doan.cinemaserver.repository.MovieTypeRepository;
import com.doan.cinemaserver.repository.SeatRepository;
import com.doan.cinemaserver.service.MovieService;
import com.doan.cinemaserver.util.MessageSourceUtil;
import com.doan.cinemaserver.util.UploadFileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final SeatRepository seatRepository;
    private final MovieMapper movieMapper;
    private final CinemaRepository cinemaRepository;
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

    @Override
    public PaginationResponseDto<MovieResponseDto> findMovieByDate(MovieSearchRequestDto movieSearchRequestDto) {
        if(movieSearchRequestDto.getDateSearch() == null) {
            throw new InvalidException(ErrorMessage.INVALID_NOT_BLANK_FIELD,"DateSearch");
        }
        Sort sort = movieSearchRequestDto.getIsAscending() != null && movieSearchRequestDto.getIsAscending()
                ? Sort.by(movieSearchRequestDto.getSortBy()).ascending()
                : Sort.by(movieSearchRequestDto.getSortBy()).descending();
        Pageable pageable =PageRequest.of(movieSearchRequestDto.getPageNo(), movieSearchRequestDto.getPageSize(),sort);

        Cinema cinema = cinemaRepository.findById(movieSearchRequestDto.getCinemaId()).orElseThrow(() ->
                new NotFoundException(ErrorMessage.Cinema.ERR_NOT_FOUND_CINEMA, new String[]{movieSearchRequestDto.getCinemaId().toString()}));

        Page<Movie> movies = movieRepository.getMoviesByDate(movieSearchRequestDto.getCinemaId(),movieSearchRequestDto.getDateSearch(),pageable);

        List<MovieResponseDto> moviesRep=movies.getContent().stream().map(
                movie -> {
                    StringBuilder typeBuilder = new StringBuilder();
                    movie.getTypes().forEach(t -> {
                        typeBuilder.append(t.getName()).append(", ");
                    });
                    String types = typeBuilder.length() > 0
                            ? typeBuilder.substring(0, typeBuilder.length() - 2)
                            : "";
                    return MovieResponseDto.builder()
                            .id(movie.getId())
                            .description(movie.getDescription())
                            .name(movie.getName())
                            .duration(movie.getDuration())
                            .language(movie.getLanguage())
                            .image(movie.getImage())
                            .type(types)
                            .actors(movie.getActors())
                            .endDate(movie.getEndDate())
                            .releaseDate(movie.getReleaseDate())
                            .build();
                }
        ).collect(Collectors.toList());

        PaginationResponseDto<MovieResponseDto> response = new PaginationResponseDto<>(
                movies.getTotalElements(),movies.getTotalPages(),movies.getNumber(),movies.getNumberOfElements(),sort.toString(),moviesRep
        );

        return response;
    }

    @Override
    public PaginationResponseDto<MovieResponseDto> findMovieComingSoon(MovieSearchRequestDto movieSearchRequestDto) {
        if(movieSearchRequestDto.getDateSearch() == null) {
            throw new InvalidException(ErrorMessage.INVALID_NOT_BLANK_FIELD,"DateSearch");
        }
        Sort sort = movieSearchRequestDto.getIsAscending() != null && movieSearchRequestDto.getIsAscending()
                ? Sort.by(movieSearchRequestDto.getSortBy()).ascending()
                : Sort.by(movieSearchRequestDto.getSortBy()).descending();
        Pageable pageable =PageRequest.of(movieSearchRequestDto.getPageNo(), movieSearchRequestDto.getPageSize(),sort);

        Page<Movie> movies = movieRepository.getMoviesComingSoon(movieSearchRequestDto.getDateSearch(),pageable);
        List<MovieResponseDto> moviesRep=movies.getContent().stream().map(
                movie -> {
                    StringBuilder typeBuilder = new StringBuilder();
                    movie.getTypes().forEach(t -> {
                        typeBuilder.append(t.getName()).append(", ");
                    });
                    String types = typeBuilder.length() > 0
                            ? typeBuilder.substring(0, typeBuilder.length() - 2)
                            : "";
                    return MovieResponseDto.builder()
                            .id(movie.getId())
                            .description(movie.getDescription())
                            .name(movie.getName())
                            .duration(movie.getDuration())
                            .language(movie.getLanguage())
                            .image(movie.getImage())
                            .type(types)
                            .actors(movie.getActors())
                            .endDate(movie.getEndDate())
                            .releaseDate(movie.getReleaseDate())
                            .build();
                }
        ).collect(Collectors.toList());

        PaginationResponseDto<MovieResponseDto> response = new PaginationResponseDto<>(
                movies.getTotalElements(),movies.getTotalPages(),movies.getNumber(),movies.getNumberOfElements(),sort.toString(),moviesRep
        );
        return response;
    }

    private LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

}
