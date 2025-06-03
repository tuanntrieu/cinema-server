package com.doan.cinemaserver.controller;

import com.doan.cinemaserver.common.RestApiV1;
import com.doan.cinemaserver.common.VsResponseUtil;
import com.doan.cinemaserver.constant.UrlConstant;
import com.doan.cinemaserver.domain.dto.statistics.RevenueChartRequestDto;
import com.doan.cinemaserver.domain.dto.statistics.RevenueChartResponseDto;
import com.doan.cinemaserver.domain.dto.statistics.RevenueCinemaRequestDto;
import com.doan.cinemaserver.domain.dto.statistics.RevenueMovieRequestDto;
import com.doan.cinemaserver.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDate;

@RestApiV1
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    @Operation(summary = "API Get Revenue Cinema")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(UrlConstant.Statistics.REVENUE_CINEMA)
    public ResponseEntity<?> getRevenueCinema(@RequestBody RevenueCinemaRequestDto requestDto) {
        return VsResponseUtil.success(statisticsService.getRevenueCinema(requestDto));
    }

    @Operation(summary = "API Get Revenue Movie")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(UrlConstant.Statistics.REVENUE_MOVIE)
    public ResponseEntity<?> getRevenueMovie(@RequestBody RevenueMovieRequestDto requestDto) {
        return VsResponseUtil.success(statisticsService.getRevenueMovie(requestDto));
    }

    @Operation(summary = "API Get Revenue Chart By Month")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(UrlConstant.Statistics.REVENUE_CHART_BY_MONTH)
    public ResponseEntity<?> getRevenueChartByMonth(@RequestBody RevenueChartRequestDto requestDto) {
        return VsResponseUtil.success(statisticsService.getRevenueChartByMonth(requestDto));
    }

    @Operation(summary = "API Get Revenue Chart By Year")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(UrlConstant.Statistics.REVENUE_CHART_BY_YEAR)
    public ResponseEntity<?> getRevenueChartByYear(@RequestBody RevenueChartRequestDto requestDto) {
        return VsResponseUtil.success(statisticsService.getRevenueChartByYear(requestDto));
    }
    @Operation(summary = "API Count Customer By Date")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(UrlConstant.Statistics.COUNT_CUSTOMER_BY_DATE)
    public ResponseEntity<?> countCustomerByDate(@RequestParam LocalDate date) {
        return VsResponseUtil.success(statisticsService.countCustomerByDate(date));
    }

    @Operation(summary = "API Count Customer By Week")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(UrlConstant.Statistics.COUNT_CUSTOMER_BY_WEEK)
    public ResponseEntity<?> countCustomerByWeek(@RequestParam LocalDate date) {
        return VsResponseUtil.success(statisticsService.countCustomerByWeek(date));
    }

    @Operation(summary = "API Count Customer By Month")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(UrlConstant.Statistics.COUNT_CUSTOMER_BY_MONTH)
    public ResponseEntity<?> countCustomerByMonth(@RequestParam LocalDate date) {
        return VsResponseUtil.success(statisticsService.countCustomerByMonth(date));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "API Count Ticket By Date")
    @GetMapping(UrlConstant.Statistics.COUNT_TICKET_BY_DATE)
    public ResponseEntity<?> countTicketByDate(@RequestParam LocalDate date) {
        return VsResponseUtil.success(statisticsService.countTicketByDate(date));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "API Count Ticket By Week")
    @GetMapping(UrlConstant.Statistics.COUNT_TICKET_BY_WEEK)
    public ResponseEntity<?> countTicketByWeek(@RequestParam LocalDate date) {
        return VsResponseUtil.success(statisticsService.countTicketByWeek(date));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "API Count Ticket By Month")
    @GetMapping(UrlConstant.Statistics.COUNT_TICKET_BY_MONTH)
    public ResponseEntity<?> countTicketByMonth(@RequestParam LocalDate date) {
        return VsResponseUtil.success(statisticsService.countTicketByMonth(date));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "API Sum Total By Date")
    @GetMapping(UrlConstant.Statistics.SUM_TOTAL_BY_DATE)
    public ResponseEntity<?> sumTotalByDate(@RequestParam LocalDate date) {
        return VsResponseUtil.success(statisticsService.sumTotalByDate(date));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "API Sum Total By Week")
    @GetMapping(UrlConstant.Statistics.SUM_TOTAL_BY_WEEK)
    public ResponseEntity<?> sumTotalByWeek(@RequestParam LocalDate date) {
        return VsResponseUtil.success(statisticsService.sumTotalByWeek(date));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "API Sum Total By Month")
    @GetMapping(UrlConstant.Statistics.SUM_TOTAL_BY_MONTH)
    public ResponseEntity<?> sumTotalByMonth(@RequestParam LocalDate date) {
        return VsResponseUtil.success(statisticsService.sumTotalByMonth(date));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "API Export Movie Excel")
    @PostMapping(UrlConstant.Statistics.EXPORT_MOVIE_EXCEL)
    public void exportMovieExcel(@RequestBody RevenueMovieRequestDto requestDto, HttpServletResponse response) throws IOException {
        statisticsService.exportExcelForMovie(requestDto,response);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "API Export Cinema Excel")
    @PostMapping(UrlConstant.Statistics.EXPORT_CINEMA_EXCEL)
    public void exportCinemaExcel(@RequestBody RevenueCinemaRequestDto requestDto, HttpServletResponse response) throws IOException {
        statisticsService.exportExcelForCinema(requestDto,response);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "API Get Statistics Schedule")
    @GetMapping(UrlConstant.Statistics.STATISTIC_SCHEDULE)
    public ResponseEntity<?> getStatisticsSchedule(@RequestParam LocalDate date) {
        return VsResponseUtil.success(statisticsService.getStatisticsSchedule(date));
    }

}
