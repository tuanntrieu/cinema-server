package com.doan.cinemaserver.service.impl;

import com.doan.cinemaserver.constant.ErrorMessage;
import com.doan.cinemaserver.constant.SuccessMessage;
import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.movie.MovieDetailResponseDto;
import com.doan.cinemaserver.domain.dto.movie.MovieRequestDto;
import com.doan.cinemaserver.domain.dto.movie.MovieResponseDto;
import com.doan.cinemaserver.domain.dto.movie.MovieSearchRequestDto;
import com.doan.cinemaserver.domain.dto.pagination.PaginationResponseDto;
import com.doan.cinemaserver.domain.entity.Cinema;
import com.doan.cinemaserver.domain.entity.Movie;
import com.doan.cinemaserver.domain.entity.MovieType;
import com.doan.cinemaserver.domain.entity.Movie_;
import com.doan.cinemaserver.exception.DataIntegrityViolationException;
import com.doan.cinemaserver.exception.InvalidException;
import com.doan.cinemaserver.exception.NotFoundException;
import com.doan.cinemaserver.mapper.MovieMapper;
import com.doan.cinemaserver.repository.CinemaRepository;
import com.doan.cinemaserver.repository.MovieRepository;
import com.doan.cinemaserver.repository.MovieTypeRepository;
import com.doan.cinemaserver.service.MovieService;
import com.doan.cinemaserver.util.MessageSourceUtil;
import com.doan.cinemaserver.util.UploadFileUtil;
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
    private final MovieMapper movieMapper;
    private final CinemaRepository cinemaRepository;
    private final MovieTypeRepository movieTypeRepository;
    private final MessageSourceUtil messageSourceUtil;
    private final UploadFileUtil uploadFileUtil;


    @Override
    @Transactional
    public CommonResponseDto createMovie(MovieRequestDto movieRequestDto, MultipartFile image) {
        LocalDate releaseDate = movieRequestDto.getReleaseDate();
        LocalDate endDate = movieRequestDto.getEndDate();
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
        if (movieSearchRequestDto.getDateSearch() == null) {
            throw new InvalidException(ErrorMessage.INVALID_NOT_BLANK_FIELD, "DateSearch");
        }
        Sort sort = movieSearchRequestDto.getIsAscending() != null && movieSearchRequestDto.getIsAscending()
                ? Sort.by(movieSearchRequestDto.getSortBy()).ascending()
                : Sort.by(movieSearchRequestDto.getSortBy()).descending();
        Pageable pageable = PageRequest.of(movieSearchRequestDto.getPageNo(), movieSearchRequestDto.getPageSize(), sort);

        Cinema cinema = cinemaRepository.findById(movieSearchRequestDto.getCinemaId()).orElseThrow(() ->
                new NotFoundException(ErrorMessage.Cinema.ERR_NOT_FOUND_CINEMA, new String[]{movieSearchRequestDto.getCinemaId().toString()}));

        Page<Movie> movies = movieRepository.getMoviesByDate(movieSearchRequestDto.getCinemaId(), movieSearchRequestDto.getDateSearch(), pageable);

        List<MovieResponseDto> moviesRep = movies.getContent().stream().map(
                movie -> {
                    StringBuilder typeBuilder = new StringBuilder();
                    movie.getTypes().forEach(t -> {
                        typeBuilder.append(t.getName()).append(", ");
                    });
                    String types = !typeBuilder.isEmpty()
                            ? typeBuilder.substring(0, typeBuilder.length() - 2)
                            : "";
                    return MovieResponseDto.builder()
                            .id(movie.getId())
                            .description(movie.getDescription())
                            .name(movie.getName())
                            .duration(movie.getDuration())
                            .language(movie.getLanguage())
                            .image(movie.getImage())
                            .director(movie.getDirector())
                            .type(types)
                            .isSub(movie.getIsSub())
                            .ageLimit(movie.getAgeLimit())
                            .actors(movie.getActors())
                            .endDate(movie.getEndDate())
                            .trailer(movie.getTrailer())
                            //.releaseDate(movie.getReleaseDate())
                            .build();
                }
        ).collect(Collectors.toList());

        return new PaginationResponseDto<>(
                movies.getTotalElements(), movies.getTotalPages(), movies.getNumber(), movieSearchRequestDto.getPageSize(), sort.toString(), moviesRep
        );
    }

    @Override
    public PaginationResponseDto<MovieResponseDto> findMovieComingSoon(MovieSearchRequestDto movieSearchRequestDto) {
        if (movieSearchRequestDto.getDateSearch() == null) {
            throw new InvalidException(ErrorMessage.INVALID_NOT_BLANK_FIELD, "DateSearch");
        }
        Sort sort = movieSearchRequestDto.getIsAscending() != null && movieSearchRequestDto.getIsAscending()
                ? Sort.by(movieSearchRequestDto.getSortBy()).ascending()
                : Sort.by(movieSearchRequestDto.getSortBy()).descending();
        Pageable pageable = PageRequest.of(movieSearchRequestDto.getPageNo(), movieSearchRequestDto.getPageSize(), sort);

        Page<Movie> movies = movieRepository.getMoviesComingSoon(movieSearchRequestDto.getDateSearch(), pageable);
        return getMovieResponseDtoPaginationResponseDto(movieSearchRequestDto, sort, movies);
    }

    @Override
    public Movie getMovieDetail(Long movieId) {
        return movieRepository.findById(movieId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Movie.ERR_NOT_FOUND_MOVIE, (Object) new Object[]{movieId.toString()})
        );
    }

    @Override
    @Transactional
    public CommonResponseDto updateMovie(Long movieId, MovieRequestDto movieRequestDto, MultipartFile image) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Movie.ERR_NOT_FOUND_MOVIE, (Object) new Object[]{movieId.toString()})
        );
        String imageTmp = movie.getImage();
        LocalDate releaseDate = movieRequestDto.getReleaseDate();
        LocalDate endDate = movieRequestDto.getEndDate();
        if (releaseDate.isAfter(endDate) || releaseDate.equals(endDate)) {
            throw new DataIntegrityViolationException(ErrorMessage.Movie.ERR_INVALID_TIME);
        }
        movie.getSchedules().forEach(schedule -> {
            if(!schedule.getScheduleTime().toLocalDate().isAfter(releaseDate)) {
                throw new DataIntegrityViolationException(ErrorMessage.Movie.ERR_PREVIOUS_SCHEDULE);
            }
            if(!schedule.getScheduleTime().toLocalDate().isBefore(endDate) ) {
                throw new DataIntegrityViolationException(ErrorMessage.Movie.ERR_NEXT_SCHEDULE);
            }
        });
        movie.getTypes().forEach(movieType -> {
            movieRepository.deleteMovieType(movie.getId(),movieType.getId());
        });
        for (MovieType type : movie.getTypes()) {
            type.getMovies().remove(movie);
        }
        movie.getTypes().clear();
        movie.setTrailer(movieRequestDto.getTrailer());
        movie.setActors(movieRequestDto.getActors());
        movie.setDescription(movieRequestDto.getDescription());
        movie.setDuration(movieRequestDto.getDuration());
        movie.setAgeLimit(movieRequestDto.getAgeLimit());
        movie.setLanguage(movieRequestDto.getLanguage());
        movie.setIsSub(movieRequestDto.getIsSub());
        movie.setReleaseDate(convertLocalDateToDate(movieRequestDto.getReleaseDate()));
        movie.setEndDate(convertLocalDateToDate(movieRequestDto.getEndDate()));

        for (Long id : movieRequestDto.getMovieTypeId()) {
            MovieType type = movieTypeRepository.findById(id).orElseThrow(() ->
                    new NotFoundException(ErrorMessage.MovieType.ERR_NOT_FOUND_MOVIE_TYPE, new String[]{id.toString()}));
            type.getMovies().add(movie);
            movieTypeRepository.save(type);
        }
        if (image != null && !image.isEmpty()) {
            movie.setImage(uploadFileUtil.uploadFile(image));
            uploadFileUtil.destroyFileWithUrl(imageTmp);
        } else {
            movie.setImage(imageTmp);
        }
        movieRepository.save(movie);
        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.UPDATE_SUCCESS, null));
    }
    public static Date convertLocalDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public PaginationResponseDto<MovieResponseDto> getAllMovies(MovieSearchRequestDto movieSearchRequestDto) {
        Sort sort = movieSearchRequestDto.getIsAscending() != null && movieSearchRequestDto.getIsAscending()
                ? Sort.by(movieSearchRequestDto.getSortBy()).ascending()
                : Sort.by(movieSearchRequestDto.getSortBy()).descending();
        Pageable pageable = PageRequest.of(movieSearchRequestDto.getPageNo(), movieSearchRequestDto.getPageSize(), sort);
        Page<Movie> getMovies = movieRepository.findAll(new Specification<Movie>() {
            @Override
            public Predicate toPredicate(Root<Movie> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (movieSearchRequestDto.getName() != null && !movieSearchRequestDto.getName().trim().isEmpty()) {
                    predicates.add(criteriaBuilder.like(
                            root.get(Movie_.NAME).as(String.class),
                            "%" + movieSearchRequestDto.getName() + "%"
                    ));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        }, pageable);
        return getMovieResponseDtoPaginationResponseDto(movieSearchRequestDto, sort, getMovies);
    }

    private PaginationResponseDto<MovieResponseDto> getMovieResponseDtoPaginationResponseDto(MovieSearchRequestDto movieSearchRequestDto, Sort sort, Page<Movie> getMovies) {
        List<MovieResponseDto> moviesRep = getMovies.getContent().stream().map(
                movie -> {
                    StringBuilder typeBuilder = new StringBuilder();
                    movie.getTypes().forEach(t -> {
                        typeBuilder.append(t.getName()).append(", ");
                    });
                    String types = !typeBuilder.isEmpty()
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
                            .isSub(movie.getIsSub())
                            .ageLimit(movie.getAgeLimit())
                            .director(movie.getDirector())
                            .actors(movie.getActors())
                            .endDate(movie.getEndDate())
                            .trailer(movie.getTrailer())
                            .releaseDate(movie.getReleaseDate())
                            .build();
                }
        ).collect(Collectors.toList());

        return new PaginationResponseDto<>(
                getMovies.getTotalElements(), getMovies.getTotalPages(), getMovies.getNumber(), movieSearchRequestDto.getPageSize(), sort.toString(), moviesRep
        );
    }

    @Override
    public MovieDetailResponseDto getMovieDetail(long movieId) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Movie.ERR_NOT_FOUND_MOVIE, (Object) new Object[]{String.valueOf(movieId)})
        );
        return MovieDetailResponseDto.builder()
                .id(movie.getId())
                .description(movie.getDescription())
                .name(movie.getName())
                .duration(movie.getDuration())
                .language(movie.getLanguage())
                .image(movie.getImage())
                .movieTypeId(movie.getTypes().stream()
                        .mapToLong(MovieType::getId)
                        .toArray())
                .isSub(movie.getIsSub())
                .ageLimit(movie.getAgeLimit())
                .director(movie.getDirector())
                .actors(movie.getActors())
                .endDate(movie.getEndDate())
                .trailer(movie.getTrailer())
                .releaseDate(movie.getReleaseDate())
                .build();
    }

    @Override
    public CommonResponseDto deleteMovie(Long movieId) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Movie.ERR_NOT_FOUND_MOVIE, (Object) new Object[]{movieId.toString()})
        );
        LocalDate now = LocalDate.now();
        LocalDate release = toLocalDate(movie.getReleaseDate());
        LocalDate end = toLocalDate(movie.getEndDate());

        if (!now.isBefore(release) && !now.isAfter(end)) {
            throw new InvalidException(ErrorMessage.Movie.ERR_CURRENTLY_SHOWING);
        }
        String imageTmp=movie.getImage();
        movieRepository.delete(movie);
        uploadFileUtil.destroyFileWithUrl(imageTmp);
        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.CREATE_SUCCESS, null));
    }



    private LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

}
