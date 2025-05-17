package com.doan.cinemaserver.service;

import com.doan.cinemaserver.domain.dto.pagination.PaginationResponseDto;
import com.doan.cinemaserver.domain.dto.statistics.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface StatisticsService {
    public int countCustomerByDate(LocalDate date);
    public int countCustomerByWeek(LocalDate date);
    public int countCustomerByMonth(LocalDate date);

    public int countTicketByDate(LocalDate date);
    public int countTicketByWeek(LocalDate date);
    public int countTicketByMonth(LocalDate date);

    public long sumTotalByDate(LocalDate date);
    public long sumTotalByWeek(LocalDate date);
    public long sumTotalByMonth(LocalDate date);

    public PaginationResponseDto<RevenueMovieResponseDto> getRevenueMovie(RevenueMovieRequestDto requestDto);
    public PaginationResponseDto<RevenueCinemaResponseDto> getRevenueCinema(RevenueCinemaRequestDto requestDto);

    public List<RevenueChartResponseDto> getRevenueChartByMonth(RevenueChartRequestDto request);

    public List<RevenueChartResponseDto> getRevenueChartByYear(RevenueChartRequestDto request);


}
