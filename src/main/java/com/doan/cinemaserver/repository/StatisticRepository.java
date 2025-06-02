package com.doan.cinemaserver.repository;

import com.doan.cinemaserver.domain.dto.pagination.PaginationResponseDto;
import com.doan.cinemaserver.domain.dto.statistics.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StatisticRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public PaginationResponseDto<RevenueCinemaResponseDto> getRevenueCinema(RevenueCinemaRequestDto requestDto) {
        StringBuilder str = new StringBuilder();
        StringBuilder countStr = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        str.append("""
                    SELECT new com.doan.cinemaserver.domain.dto.statistics.RevenueCinemaResponseDto(
                        c.id,
                        c.cinemaName,
                        COUNT(t),
                        COALESCE(SUM(t.priceCombo), 0) + COALESCE(SUM(t.priceSeat), 0)
                    )
                    FROM Cinema c
                    LEFT JOIN Ticket t ON t.cinema = c
                    WHERE 1 = 1
                """);

        countStr.append("""
                    SELECT COUNT(DISTINCT c.id)
                    FROM Cinema c
                    LEFT JOIN Ticket t ON t.cinema = c
                    WHERE 1 = 1
                """);

        if (requestDto.getDate() != null) {
            str.append(" AND MONTH(t.createdDate) = MONTH(:date) AND YEAR(t.createdDate) = YEAR(:date) ");
            countStr.append(" AND MONTH(t.createdDate) = MONTH(:date) AND YEAR(t.createdDate) = YEAR(:date) ");
            params.put("date", requestDto.getDate());
        }

        str.append(" GROUP BY c.id, c.cinemaName ");
        str.append(" ORDER BY COALESCE(SUM(t.priceCombo), 0) + COALESCE(SUM(t.priceSeat), 0) ");
        str.append(Boolean.TRUE.equals(requestDto.getIsAscending()) ? "ASC" : "DESC");


        TypedQuery<RevenueCinemaResponseDto> query =
                entityManager.createQuery(str.toString(), RevenueCinemaResponseDto.class);

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        query.setFirstResult(requestDto.getPageNo() * requestDto.getPageSize());
        query.setMaxResults(requestDto.getPageSize());
        List<RevenueCinemaResponseDto> result = query.getResultList();


        TypedQuery<Long> countQuery = entityManager.createQuery(countStr.toString(), Long.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            countQuery.setParameter(entry.getKey(), entry.getValue());
        }

        Long totalElements = countQuery.getSingleResult();
        Page<RevenueCinemaResponseDto> page = new PageImpl<>(
                result,
                PageRequest.of(requestDto.getPageNo(), requestDto.getPageSize()),
                totalElements
        );

        return new PaginationResponseDto<>(
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize(),
                "total",
                result
        );
    }

    public PaginationResponseDto<RevenueMovieResponseDto> getRevenueMovie(RevenueMovieRequestDto requestDto) {
        StringBuilder str = new StringBuilder();
        StringBuilder countStr = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        str.append("""
                    SELECT new com.doan.cinemaserver.domain.dto.statistics.RevenueMovieResponseDto(
                        v.id,
                        v.name,
                        COUNT(t),
                        COALESCE(SUM(t.priceSeat), 0)
                    )
                    FROM Movie v
                    LEFT JOIN Ticket t ON t.movie = v
                    WHERE 1 = 1
                """);

        countStr.append("""
                    SELECT COUNT(DISTINCT v.id)
                    FROM Movie v
                    LEFT JOIN Ticket t ON t.movie = v
                    WHERE 1 = 1 
                """);

        if (requestDto.getDate() != null) {
            str.append(" AND MONTH(t.createdDate) = MONTH(:date) AND YEAR(t.createdDate) = YEAR(:date) ");
            countStr.append(" AND MONTH(t.createdDate) = MONTH(:date) AND YEAR(t.createdDate) = YEAR(:date) ");
            params.put("date", requestDto.getDate());
        }

        if (requestDto.getName() != null && !requestDto.getName().isEmpty()) {
            str.append(" AND v.name LIKE CONCAT('%', :name, '%') ");
            countStr.append(" AND v.name LIKE CONCAT('%', :name, '%') ");
            params.put("name", requestDto.getName());
        }


        str.append(" GROUP BY v.id, v.name ");
        str.append(" ORDER BY COALESCE(SUM(t.priceSeat), 0) ");
        str.append(Boolean.TRUE.equals(requestDto.getIsAscending()) ? "ASC" : "DESC");


        TypedQuery<RevenueMovieResponseDto> query =
                entityManager.createQuery(str.toString(), RevenueMovieResponseDto.class);

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        query.setFirstResult(requestDto.getPageNo() * requestDto.getPageSize());
        query.setMaxResults(requestDto.getPageSize());
        List<RevenueMovieResponseDto> result = query.getResultList();


        TypedQuery<Long> countQuery = entityManager.createQuery(countStr.toString(), Long.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            countQuery.setParameter(entry.getKey(), entry.getValue());
        }

        Long totalElements = countQuery.getSingleResult();
        Page<RevenueMovieResponseDto> page = new PageImpl<>(
                result,
                PageRequest.of(requestDto.getPageNo(), requestDto.getPageSize()),
                totalElements
        );

        return new PaginationResponseDto<>(
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize(),
                "total",
                result
        );

    }

    public RevenueChartResponseDto getRevenueChart(LocalDate startDate, LocalDate endDate, Long cinemaId) {
        StringBuilder str = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        str.append("""
                select new com.doan.cinemaserver.domain.dto.statistics.RevenueChartResponseDto(COUNT(t.id),COALESCE(SUM(t.priceSeat), 0)+COALESCE(SUM(t.priceCombo), 0))
                from Ticket t
                where date(t.createdDate) >= date(:startDate) and date(t.createdDate) <= date(:endDate)
                """);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        if (cinemaId != null) {
            str.append(" and t.cinema.id = :cinemaId ");
            params.put("cinemaId", cinemaId);
        }
        TypedQuery<RevenueChartResponseDto> query =
                entityManager.createQuery(str.toString(), RevenueChartResponseDto.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.getSingleResult();
    }

    public List<RevenueMovieResponseDto> getRevenueMovieExcel(RevenueMovieRequestDto requestDto) {
        StringBuilder str = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        str.append("""
                    SELECT new com.doan.cinemaserver.domain.dto.statistics.RevenueMovieResponseDto(
                        v.id,
                        v.name,
                        COUNT(t),
                        COALESCE(SUM(t.priceSeat), 0)
                    )
                    FROM Movie v
                    LEFT JOIN Ticket t ON t.movie = v
                    WHERE 1 = 1
                """);
        if (requestDto.getDate() != null) {
            str.append(" AND MONTH(t.createdDate) = MONTH(:date) AND YEAR(t.createdDate) = YEAR(:date) ");
            params.put("date", requestDto.getDate());
        }
        if (requestDto.getName() != null && !requestDto.getName().isEmpty()) {
            str.append(" AND v.name LIKE CONCAT('%', :name, '%') ");
            params.put("name", requestDto.getName());
        }
        str.append(" GROUP BY v.id, v.name ");
        str.append(" ORDER BY COALESCE(SUM(t.priceSeat), 0) ");
        str.append(Boolean.TRUE.equals(requestDto.getIsAscending()) ? "ASC" : "DESC");
        TypedQuery<RevenueMovieResponseDto> query =
                entityManager.createQuery(str.toString(), RevenueMovieResponseDto.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.getResultList();
    }

    public List<RevenueCinemaResponseDto> getRevenueCinemaExcel(RevenueCinemaRequestDto requestDto) {
        StringBuilder str = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        str.append("""
                    SELECT new com.doan.cinemaserver.domain.dto.statistics.RevenueCinemaResponseDto(
                        c.id,
                        c.cinemaName,
                        COUNT(t),
                        COALESCE(SUM(t.priceCombo), 0) + COALESCE(SUM(t.priceSeat), 0)
                    )
                    FROM Cinema c
                    LEFT JOIN Ticket t ON t.cinema = c
                    WHERE 1 = 1
                """);

        if (requestDto.getDate() != null) {
            str.append(" AND MONTH(t.createdDate) = MONTH(:date) AND YEAR(t.createdDate) = YEAR(:date) ");
            params.put("date", requestDto.getDate());
        }
        str.append(" GROUP BY c.id, c.cinemaName ");
        str.append(" ORDER BY COALESCE(SUM(t.priceCombo), 0) + COALESCE(SUM(t.priceSeat), 0) ");
        str.append(Boolean.TRUE.equals(requestDto.getIsAscending()) ? "ASC" : "DESC");

        TypedQuery<RevenueCinemaResponseDto> query =
                entityManager.createQuery(str.toString(), RevenueCinemaResponseDto.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        query.setFirstResult(requestDto.getPageNo() * requestDto.getPageSize());
        query.setMaxResults(requestDto.getPageSize());
        return query.getResultList();
    }



}
