package com.doan.cinemaserver.service.impl;

import com.doan.cinemaserver.constant.ErrorMessage;
import com.doan.cinemaserver.constant.SuccessMessage;
import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.schedule.ScheduleRequestDto;
import com.doan.cinemaserver.domain.dto.schedule.ScheduleResponseDto;
import com.doan.cinemaserver.domain.dto.schedule.ScheduleSearchRequestDto;
import com.doan.cinemaserver.domain.entity.*;

import com.doan.cinemaserver.exception.DataIntegrityViolationException;
import com.doan.cinemaserver.exception.NotFoundException;
import com.doan.cinemaserver.repository.MovieRepository;
import com.doan.cinemaserver.repository.RoomRepository;
import com.doan.cinemaserver.repository.ScheduleRepository;
import com.doan.cinemaserver.service.ScheduleService;
import com.doan.cinemaserver.util.MessageSourceUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final MessageSourceUtil messageSourceUtil;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;

    @Override
    public CommonResponseDto createSchedule(ScheduleRequestDto requestDto) {

        Room room = roomRepository.findById(requestDto.getRoomId()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Room.ERR_NOT_FOUND_ROOM)
        );

        Movie movie = movieRepository.findById(requestDto.getMovieId()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Movie.ERR_NOT_FOUND_MOVIE)
        );

        LocalDateTime startTime = requestDto.getScheduleTime();
        LocalDateTime endTime = startTime.plus(movie.getDuration() + 15, ChronoUnit.MINUTES);
//
        //kiem tra xem co khoang thoi gian nao giao voi khong thoi gian moi khong
        for (Schedule schedule : room.getSchedules()) {
            LocalDateTime existingStart = schedule.getScheduleTime();
            LocalDateTime existingEnd = existingStart.plus(schedule.getMovie().getDuration() + 15, ChronoUnit.MINUTES);
            if ((startTime.isBefore(existingEnd) || startTime.isEqual(existingEnd))
                    && (endTime.isAfter(existingStart) || endTime.isEqual(existingStart))) {
                throw new DataIntegrityViolationException(ErrorMessage.Schedule.ERR_INTERVAL_CONFLICT);
            }
        }
        scheduleRepository.save(Schedule.builder()
                .scheduleTime(startTime)
                .room(room)
                .movie(movie)
                .build());
        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.CREATE_SUCCESS, null));
    }


    @Override
    public CommonResponseDto deleteSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Schedule.ERR_NOT_FOUND_SCHEDULE, new String[]{scheduleId.toString()})
        );

        schedule.getRoom().getSchedules().remove(schedule);
        roomRepository.save(schedule.getRoom());

        schedule.getMovie().getSchedules().remove(schedule);
        movieRepository.save(schedule.getMovie());

        scheduleRepository.delete(schedule);
        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.DELETE_SUCCESS, null));
    }

    @Override
    public List<ScheduleResponseDto> searchSchedule(ScheduleSearchRequestDto requestDto) {
        List<Schedule> schedules = scheduleRepository.findAll(
                new Specification<Schedule>() {
                    @Override
                    public Predicate toPredicate(Root<Schedule> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                        List<Predicate> predicates = new ArrayList<>();
                        if (requestDto.getMovieId() != null) {
                            predicates.add(criteriaBuilder.equal(root.get(Schedule_.movie).get(Movie_.id), requestDto.getMovieId()));
                        }
                        if (requestDto.getRoomId() != null) {
                            predicates.add(criteriaBuilder.equal(root.get(Schedule_.room).get(Room_.id), requestDto.getRoomId()));
                        }
                        if (requestDto.getDate() != null) {

                            predicates.add(criteriaBuilder.equal(criteriaBuilder.function("DATE", LocalDate.class, root.get(Schedule_.scheduleTime)), requestDto.getDate()));
                        }

                        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                    }
                }
        );

        List<ScheduleResponseDto> scheduleResponseDtos = new ArrayList<>();
        schedules.forEach(schedule -> {
            scheduleResponseDtos.add(
                    ScheduleResponseDto.builder()
                            .movieName(schedule.getMovie().getName())
                            .startTime(schedule.getScheduleTime().toLocalTime())
                            .endTime(schedule.getScheduleTime().plus(schedule.getMovie().getDuration(), ChronoUnit.MINUTES).toLocalTime())
                            .build()
            );
        });

        return scheduleResponseDtos;
    }



}
