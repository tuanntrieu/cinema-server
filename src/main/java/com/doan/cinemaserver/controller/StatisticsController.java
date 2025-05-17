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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@RestApiV1
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    @Operation(summary = "API Get Revenue Cinema")
    @PostMapping(UrlConstant.Statistics.REVENUE_CINEMA)
    public ResponseEntity<?> getRevenueCinema(@RequestBody RevenueCinemaRequestDto requestDto) {
        return VsResponseUtil.success(statisticsService.getRevenueCinema(requestDto));
    }

    @Operation(summary = "API Get Revenue Movie")
    @PostMapping(UrlConstant.Statistics.REVENUE_MOVIE)
    public ResponseEntity<?> getRevenueMovie(@RequestBody RevenueMovieRequestDto requestDto) {
        return VsResponseUtil.success(statisticsService.getRevenueMovie(requestDto));
    }

    @Operation(summary = "API Get Revenue Chart By Month")
    @PostMapping(UrlConstant.Statistics.REVENUE_CHART_BY_MONTH)
    public ResponseEntity<?> getRevenueChartByMonth(@RequestBody RevenueChartRequestDto requestDto) {
        return VsResponseUtil.success(statisticsService.getRevenueChartByMonth(requestDto));
    }

    @Operation(summary = "API Get Revenue Chart By Year")
    @PostMapping(UrlConstant.Statistics.REVENUE_CHART_BY_YEAR)
    public ResponseEntity<?> getRevenueChartByYear(@RequestBody RevenueChartRequestDto requestDto) {
        return VsResponseUtil.success(statisticsService.getRevenueChartByYear(requestDto));
    }
    @Operation(summary = "API Count Customer By Date")
    @GetMapping(UrlConstant.Statistics.COUNT_CUSTOMER_BY_DATE)
    public ResponseEntity<?> countCustomerByDate(@RequestParam LocalDate date) {
        return VsResponseUtil.success(statisticsService.countCustomerByDate(date));
    }
    @Operation(summary = "API Count Customer By Week")
    @GetMapping(UrlConstant.Statistics.COUNT_CUSTOMER_BY_WEEK)
    public ResponseEntity<?> countCustomerByWeek(@RequestParam LocalDate date) {
        return VsResponseUtil.success(statisticsService.countTicketByWeek(date));
    }
    @Operation(summary = "API Count Customer By Month")
    @GetMapping(UrlConstant.Statistics.COUNT_CUSTOMER_BY_MONTH)
    public ResponseEntity<?> countCustomerByMonth(@RequestParam LocalDate date) {
        return VsResponseUtil.success(statisticsService.countTicketByMonth(date));
    }
    @Operation(summary = "API Count Ticket By Date")
    @GetMapping(UrlConstant.Statistics.COUNT_TICKET_BY_DATE)
    public ResponseEntity<?> countTicketByDate(@RequestParam LocalDate date) {
        return VsResponseUtil.success(statisticsService.countTicketByDate(date));
    }
    @Operation(summary = "API Count Ticket By Week")
    @GetMapping(UrlConstant.Statistics.COUNT_TICKET_BY_WEEK)
    public ResponseEntity<?> countTicketByWeek(@RequestParam LocalDate date) {
        return VsResponseUtil.success(statisticsService.countTicketByWeek(date));
    }
    @Operation(summary = "API Count Ticket By Month")
    @GetMapping(UrlConstant.Statistics.COUNT_TICKET_BY_MONTH)
    public ResponseEntity<?> countTicketByMonth(@RequestParam LocalDate date) {
        return VsResponseUtil.success(statisticsService.countTicketByMonth(date));
    }
    @Operation(summary = "API Sum Total By Date")
    @GetMapping(UrlConstant.Statistics.SUM_TOTAL_BY_DATE)
    public ResponseEntity<?> sumTotalByDate(@RequestParam LocalDate date) {
        return VsResponseUtil.success(statisticsService.sumTotalByDate(date));
    }
    @Operation(summary = "API Sum Total By Week")
    @GetMapping(UrlConstant.Statistics.SUM_TOTAL_BY_WEEK)
    public ResponseEntity<?> sumTotalByWeek(@RequestParam LocalDate date) {
        return VsResponseUtil.success(statisticsService.sumTotalByWeek(date));
    }
    @Operation(summary = "API Sum Total By Month")
    @GetMapping(UrlConstant.Statistics.SUM_TOTAL_BY_MONTH)
    public ResponseEntity<?> sumTotalByMonth(@RequestParam LocalDate date) {
        return VsResponseUtil.success(statisticsService.sumTotalByMonth(date));
    }


}
