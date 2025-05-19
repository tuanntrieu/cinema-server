package com.doan.cinemaserver.service;

import com.doan.cinemaserver.domain.dto.pagination.PaginationResponseDto;
import com.doan.cinemaserver.domain.dto.statistics.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public interface StatisticsService {
    public GeneralStatisticsResponseDto countCustomerByDate(LocalDate date);
    public GeneralStatisticsResponseDto countCustomerByWeek(LocalDate date);
    public GeneralStatisticsResponseDto countCustomerByMonth(LocalDate date);

    public GeneralStatisticsResponseDto countTicketByDate(LocalDate date);
    public GeneralStatisticsResponseDto countTicketByWeek(LocalDate date);
    public GeneralStatisticsResponseDto countTicketByMonth(LocalDate date);

    public GeneralStatisticsResponseDto sumTotalByDate(LocalDate date);
    public GeneralStatisticsResponseDto sumTotalByWeek(LocalDate date);
    public GeneralStatisticsResponseDto sumTotalByMonth(LocalDate date);

    public PaginationResponseDto<RevenueMovieResponseDto> getRevenueMovie(RevenueMovieRequestDto requestDto);
    public PaginationResponseDto<RevenueCinemaResponseDto> getRevenueCinema(RevenueCinemaRequestDto requestDto);

    public List<RevenueChartResponseDto> getRevenueChartByMonth(RevenueChartRequestDto request);

    public List<RevenueChartResponseDto> getRevenueChartByYear(RevenueChartRequestDto request);

    public void exportExcelForMovie(RevenueMovieRequestDto requestDto, HttpServletResponse response)throws IOException;
    public void exportExcelForCinema(RevenueCinemaRequestDto requestDto, HttpServletResponse response)throws IOException;



}
