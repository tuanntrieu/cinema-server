package com.doan.cinemaserver.service.impl;

import com.doan.cinemaserver.domain.dto.pagination.PaginationResponseDto;
import com.doan.cinemaserver.domain.dto.statistics.*;
import com.doan.cinemaserver.repository.CustomerRepository;
import com.doan.cinemaserver.repository.StatisticRepository;
import com.doan.cinemaserver.repository.TicketRepository;
import com.doan.cinemaserver.service.StatisticsService;
import com.doan.cinemaserver.util.ExcelUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final TicketRepository ticketRepository;
    private final CustomerRepository customerRepository;
    private final StatisticRepository statisticRepository;

    @Override
    public GeneralStatisticsResponseDto countCustomerByDate(LocalDate date) {
        long now = customerRepository.countCustomerByDate(date);
        long last = customerRepository.countCustomerByDate(date.minusDays(1));

        double rate = 0.0;
        if (last > 0) {
            if (now == 0) {
                rate = 0;
            } else {
                rate = ((now - last) / (double) last) * 100;
            }
        } else if (now > 0) {
            rate = 100.0;
        }
        return GeneralStatisticsResponseDto.builder()
                .total(now)
                .rate(rate)
                .build();
    }

    @Override
    public GeneralStatisticsResponseDto countCustomerByWeek(LocalDate date) {
        long now = customerRepository.countCustomerByWeek(date);
        long last = customerRepository.countCustomerByWeek(date.minusWeeks(1));

        double rate = 0.0;
        if (last > 0) {
            if (now == 0) {
                rate = 0;
            } else {
                rate = ((now - last) / (double) last) * 100;
            }
        } else if (now > 0) {
            rate = 100.0;
        }
        return GeneralStatisticsResponseDto.builder()
                .total(now)
                .rate(rate)
                .build();
    }

    @Override
    public GeneralStatisticsResponseDto countCustomerByMonth(LocalDate date) {
        long now = customerRepository.countCustomerByMonth(date);
        long last = customerRepository.countCustomerByMonth(date.minusMonths(1));

        double rate = 0.0;
        if (last > 0) {
            if (now == 0) {
                rate = 0;
            } else {
                rate = ((now - last) / (double) last) * 100;
            }
        } else if (now > 0) {
            rate = 100.0;
        }
        return GeneralStatisticsResponseDto.builder()
                .total(now)
                .rate(rate)
                .build();
    }

    @Override
    public GeneralStatisticsResponseDto countTicketByDate(LocalDate date) {
        long now = ticketRepository.countTicketByDate(date);
        long last = ticketRepository.countTicketByDate(date.minusDays(1));

        double rate = 0.0;
        if (last > 0) {
            if (now == 0) {
                rate = 0;
            } else {
                rate = ((now - last) / (double) last) * 100;
            }
        } else if (now > 0) {
            rate = 100.0;
        }
        return GeneralStatisticsResponseDto.builder()
                .total(now)
                .rate(rate)
                .build();
    }

    @Override
    public GeneralStatisticsResponseDto countTicketByWeek(LocalDate date) {
        long now = ticketRepository.countTicketByWeek(date);
        long last = ticketRepository.countTicketByWeek(date.minusWeeks(1));

        double rate = 0.0;
        if (last > 0) {
            if (now == 0) {
                rate = 0;
            } else {
                rate = ((now - last) / (double) last) * 100;
            }
        } else if (now > 0) {
            rate = 100.0;
        }
        return GeneralStatisticsResponseDto.builder()
                .total(now)
                .rate(rate)
                .build();
    }

    @Override
    public GeneralStatisticsResponseDto countTicketByMonth(LocalDate date) {
        long now = ticketRepository.countTicketByMonth(date);
        long last = ticketRepository.countTicketByMonth(date.minusMonths(1));

        double rate = 0.0;
        if (last > 0) {
            if (now == 0) {
                rate = 0;
            } else {
                rate = ((now - last) / (double) last) * 100;
            }
        } else if (now > 0) {
            rate = 100.0;
        }
        return GeneralStatisticsResponseDto.builder()
                .total(now)
                .rate(rate)
                .build();
    }

    @Override
    public GeneralStatisticsResponseDto sumTotalByDate(LocalDate date) {
        long now = ticketRepository.sumTotalByDate(date);
        long last = ticketRepository.sumTotalByDate(date.minusDays(1));

        double rate = 0.0;
        if (last > 0) {
            if (now == 0) {
                rate = 0;
            } else {
                rate = ((now - last) / (double) last) * 100;
            }
        } else if (now > 0) {
            rate = 100.0;
        }
        return GeneralStatisticsResponseDto.builder()
                .total(now)
                .rate(rate)
                .build();
    }

    @Override
    public GeneralStatisticsResponseDto sumTotalByWeek(LocalDate date) {
        long now = ticketRepository.sumTotalByWeek(date);
        long last = ticketRepository.sumTotalByWeek(date.minusWeeks(1));
        double rate = 0.0;
        if (last > 0) {
            if (now == 0) {
                rate = 0;
            } else {
                rate = ((now - last) / (double) last) * 100;
            }
        } else if (now > 0) {
            rate = 100.0;
        }
        return GeneralStatisticsResponseDto.builder()
                .total(now)
                .rate(rate)
                .build();
    }

    @Override
    public GeneralStatisticsResponseDto sumTotalByMonth(LocalDate date) {
        long now = ticketRepository.sumTotalByMonth(date);
        long last = ticketRepository.sumTotalByMonth(date.minusMonths(1));
        double rate = 0.0;
        if (last > 0) {
            if (now == 0) {
                rate = 0;
            } else {
                rate = ((now - last) / (double) last) * 100;
            }
        } else if (now > 0) {
            rate = 100.0;
        }
        return GeneralStatisticsResponseDto.builder()
                .total(now)
                .rate(rate)
                .build();
    }

    @Override
    public PaginationResponseDto<RevenueMovieResponseDto> getRevenueMovie(RevenueMovieRequestDto requestDto) {
        return statisticRepository.getRevenueMovie(requestDto);
    }

    @Override
    public PaginationResponseDto<RevenueCinemaResponseDto> getRevenueCinema(RevenueCinemaRequestDto requestDto) {
        return statisticRepository.getRevenueCinema(requestDto);
    }

    @Override
    public List<RevenueChartResponseDto> getRevenueChartByMonth(RevenueChartRequestDto request) {
        List<RevenueChartResponseDto> result = new ArrayList<>();
        LocalDate firstDay = request.getDate().withDayOfMonth(1);
        int lastDayOfMonth = request.getDate().lengthOfMonth();
        int week = 1;
        for (int day = 1; day <= lastDayOfMonth; day += 7) {
            LocalDate start = firstDay.withDayOfMonth(day);
            LocalDate end = start.plusDays(6);
            if (end.getMonthValue() != start.getMonthValue()) {
                end = firstDay.withDayOfMonth(lastDayOfMonth);
            }
            RevenueChartResponseDto dto = statisticRepository.getRevenueChart(start, end, request.getCinemaId());
            dto.setLabel("Tuần " + week);
            result.add(dto);
            week++;
        }

        return result;
    }

    @Override
    public List<RevenueChartResponseDto> getRevenueChartByYear(RevenueChartRequestDto request) {
        List<RevenueChartResponseDto> result = new ArrayList<>();
        int year = request.getDate().getYear();
        for (int month = 1; month <= 12; month++) {
            LocalDate start = LocalDate.of(year, month, 1);
            LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
            RevenueChartResponseDto dto = statisticRepository.getRevenueChart(start, end, request.getCinemaId());
            dto.setLabel("Tháng " + month);
            result.add(dto);
        }
        return result;
    }

    private final ExcelUtil excelUtil;

    @Override
    public void exportExcelForMovie(RevenueMovieRequestDto requestDto, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=statistic_movie_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        List<RevenueMovieResponseDto> list = statisticRepository.getRevenueMovieExcel(requestDto);
        excelUtil.exportDataMovieToExcel(response, requestDto.getDate(), list);


    }

    @Override
    public void exportExcelForCinema(RevenueCinemaRequestDto requestDto, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=statistic_cinema_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        List<RevenueCinemaResponseDto> list = statisticRepository.getRevenueCinemaExcel(requestDto);
        excelUtil.exportDataCinemaToExcel(response, requestDto.getDate(), list);
    }


}
