package com.doan.cinemaserver.service.impl;

import com.doan.cinemaserver.constant.ErrorMessage;
import com.doan.cinemaserver.constant.SeatStatus;
import com.doan.cinemaserver.constant.SuccessMessage;
import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.schedule.*;
import com.doan.cinemaserver.domain.entity.*;
import com.doan.cinemaserver.exception.DataIntegrityViolationException;
import com.doan.cinemaserver.exception.NotFoundException;
import com.doan.cinemaserver.repository.MovieRepository;
import com.doan.cinemaserver.repository.RoomRepository;
import com.doan.cinemaserver.repository.ScheduleRepository;
import com.doan.cinemaserver.repository.SeatRepository;
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
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;


@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final MessageSourceUtil messageSourceUtil;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    private final SeatRepository seatRepository;

    @Override
    public CommonResponseDto createSchedule(ScheduleRequestDto requestDto) {

        Room room = roomRepository.findById(requestDto.getRoomId()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Room.ERR_NOT_FOUND_ROOM, new String[]{String.valueOf(requestDto.getRoomId())})
        );

        Movie movie = movieRepository.findById(requestDto.getMovieId()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Movie.ERR_NOT_FOUND_MOVIE, new String[]{String.valueOf(requestDto.getMovieId())})
        );

        LocalDateTime startTime = requestDto.getScheduleTime();
        LocalDateTime endTime = startTime.plus(movie.getDuration() + 15, ChronoUnit.MINUTES);

        if (requestDto.getScheduleTime().toLocalDate().isBefore(movie.getReleaseDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
            throw new DataIntegrityViolationException(ErrorMessage.Schedule.ERR_INTERVAL_CONFLICT);
        }

        //kiem tra xem co khoang thoi gian nao giao voi khoang thoi gian moi khong co thi khong thoa man
        for (Schedule schedule : room.getSchedules()) {
            LocalDateTime existingStart = schedule.getScheduleTime();
            LocalDateTime existingEnd = existingStart.plus(schedule.getMovie().getDuration() + 15, ChronoUnit.MINUTES);
            if ((startTime.isBefore(existingEnd) || startTime.isEqual(existingEnd))
                    && (endTime.isAfter(existingStart) || endTime.isEqual(existingStart))) {
                throw new DataIntegrityViolationException(ErrorMessage.Schedule.ERR_INTERVAL_CONFLICT);
            }
        }
        room.getMovies().add(movie);
        roomRepository.save(room);

        Map<Long, SeatStatus> seats = new HashMap<>();

        room.getSeats().forEach(seat -> {
            seats.put(seat.getId(), SeatStatus.AVAILABLE);
        });

        Schedule schedule = Schedule.builder()
                .scheduleTime(startTime)
                .room(room)
                .movie(movie)
                .seats(seats)
                .build();
        scheduleRepository.save(schedule);
        room.getSchedules().add(schedule);
        roomRepository.save(room);
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

    //cho admin
    @Override
    public List<ScheduleForRoomResponseDto> searchSchedule(ScheduleSearchByRoomRequestDto requestDto) {

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

        List<ScheduleForRoomResponseDto> scheduleForRoomResponse = new ArrayList<>();
        schedules.forEach(schedule -> {
            scheduleForRoomResponse.add(
                    ScheduleForRoomResponseDto.builder()
                            .movieName(schedule.getMovie().getName())
                            .startTime(schedule.getScheduleTime().toLocalTime())
                            .endTime(schedule.getScheduleTime().plus(schedule.getMovie().getDuration(), ChronoUnit.MINUTES).toLocalTime())
                            .build()
            );
        });

        return scheduleForRoomResponse;
    }

    //cho user
    @Override
    public List<ScheduleForCinemaResponseDto> getScheduleForMovieByCinema(ScheduleSearchByCinemaRequestDto requestDto) {

        List<Schedule> schedules = scheduleRepository.getScheduleForCinema(requestDto.getCinemaId(), requestDto.getMovieId());

        List<RoomScheduleResponseDto> roomSchedules =buildRoomScheduleResponse(schedules);
        Map<LocalDate, List<RoomScheduleResponseDto>> scheduleTimes = new TreeMap<>();
        for (RoomScheduleResponseDto roomScheduleResponseDto : roomSchedules) {
            LocalDate date = roomScheduleResponseDto.getDate();
            if (scheduleTimes.containsKey(date)) {
                scheduleTimes.get(date).add(roomScheduleResponseDto);
            } else {
                List<RoomScheduleResponseDto> rooms = new ArrayList<>();
                rooms.add(roomScheduleResponseDto);
                scheduleTimes.put(date, rooms);
            }

        }

        List<ScheduleForCinemaResponseDto> response = new ArrayList<>();
        for (Map.Entry<LocalDate, List<RoomScheduleResponseDto>> entry : scheduleTimes.entrySet()) {
            response.add(ScheduleForCinemaResponseDto.builder()
                    .date(entry.getKey())
                    .roomSchedules(entry.getValue())
                    .build());
        }
        return response;
    }

    @Override
    public ScheduleForCinemaResponseDto getScheduleForMovieByDate(ScheduleForMovieByDateRequestDto requestDto) {
        List<Schedule> schedules = scheduleRepository.getScheduleForMovieByDate(requestDto.getCinemaId(), requestDto.getMovieId(),requestDto.getDate());

        List<RoomScheduleResponseDto> roomSchedules =buildRoomScheduleResponse(schedules);
        return ScheduleForCinemaResponseDto.builder()
                .date(requestDto.getDate())
                .roomSchedules(roomSchedules)
                .build();
    }

    private List<RoomScheduleResponseDto> buildRoomScheduleResponse(List<Schedule> schedules){
        Map<RoomScheduleDto, List<TimeScheduleDto>> roomTimes = new HashMap<>();
        Map<Long, Room> roomMap = new HashMap<>();
        for (Schedule schedule : schedules) {
            RoomScheduleDto roomScheduleDto = RoomScheduleDto.builder()
                    .roomId(schedule.getRoom().getId())
                    .date(schedule.getScheduleTime().toLocalDate())
                    .build();
            int count = seatRepository.countSeatByStatus(schedule.getRoom().getCinema().getId(), schedule.getId(), SeatStatus.AVAILABLE.toString());
            Long roomId = schedule.getRoom().getId();

            TimeScheduleDto time = TimeScheduleDto.builder()
                    .id(schedule.getId())
                    .date(schedule.getScheduleTime().toLocalDate())
                    .time(schedule.getScheduleTime().toLocalTime().truncatedTo(ChronoUnit.MINUTES))
                    .countSeatAvailable(count)
                    .build();

            roomTimes.computeIfAbsent(roomScheduleDto, k -> new ArrayList<>()).add(time);
            roomMap.putIfAbsent(roomId, schedule.getRoom());
        }
        List<RoomScheduleResponseDto> roomSchedules = new ArrayList<>();
        for (Map.Entry<RoomScheduleDto, List<TimeScheduleDto>> entry : roomTimes.entrySet()) {
            Room tmp = roomMap.get(entry.getKey().getRoomId());
            roomSchedules.add(RoomScheduleResponseDto.builder()
                    .roomId(tmp.getId())
                    .times(entry.getValue())
                    .date(entry.getKey().getDate())
                    .name(tmp.getName() + "-" + tmp.getRoomType().getRoomType().getValue())
                    .build());
        }
        return  roomSchedules;
    }

}
