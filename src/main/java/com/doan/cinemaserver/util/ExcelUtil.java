package com.doan.cinemaserver.util;

import com.doan.cinemaserver.domain.dto.statistics.RevenueCinemaResponseDto;
import com.doan.cinemaserver.domain.dto.statistics.RevenueMovieResponseDto;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Component
public class ExcelUtil {

    private void createCell(XSSFSheet sheet, XSSFWorkbook workbook, Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Float) {
            cell.setCellValue((Float) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Date) {
            CellStyle dateCellStyle = workbook.createCellStyle();
            dateCellStyle.cloneStyleFrom(style);
            CreationHelper createHelper = workbook.getCreationHelper();
            dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
            cell.setCellValue((Date) value);
            cell.setCellStyle(dateCellStyle);
            return;
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void createHeaderRowForMovie(XSSFWorkbook workbook, XSSFSheet sheet, LocalDate date) {
        CellStyle titleStyle = workbook.createCellStyle();
        XSSFFont titleFont = workbook.createFont();
        titleFont.setFontHeight(16);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);

        Row row1 = sheet.createRow(0);
        String title = "THỐNG KÊ DOANH THU THEO PHIM";
        if (date != null) {
            title += String.format(" (%02d/%d)", date.getMonthValue(), date.getYear());
        }

        createCell(sheet, workbook, row1, 0, title, titleStyle);
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 3));

        // Style cho các cột header
        CellStyle headerStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(12);
        headerStyle.setFont(font);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        Row row2 = sheet.createRow(1);
        createCell(sheet, workbook, row2, 0, "STT", headerStyle);
        createCell(sheet, workbook, row2, 1, "Tên phim", headerStyle);
        createCell(sheet, workbook, row2, 2, "Số vé bán ra", headerStyle);
        createCell(sheet, workbook, row2, 3, "Doanh thu", headerStyle);
    }

    private void writeDataLinesForMovie(XSSFWorkbook workbook, XSSFSheet sheet, List<RevenueMovieResponseDto> list) {
        int rowCount = 2;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        font.setBold(Boolean.FALSE);
        style.setFont(font);
        long sum = 0;
        for (int i = 0; i < list.size(); i++) {
            Row row = sheet.createRow(rowCount++);
            createCell(sheet, workbook, row, 0, i + 1, style);
            createCell(sheet, workbook, row, 1, list.get(i).getName(), style);
            createCell(sheet, workbook, row, 2, list.get(i).getSumTicket(), style);
            createCell(sheet, workbook, row, 3, list.get(i).getTotalSeat(), style);
            sum += list.get(i).getTotalSeat();
        }

        // Ghi dòng tổng doanh thu
        CellStyle style2 = workbook.createCellStyle();
        Row totalRow = sheet.createRow(rowCount);
        XSSFFont boldFont = workbook.createFont();
        boldFont.setBold(true);
        boldFont.setFontHeight(16);
        style2.setFont(boldFont);
        createCell(sheet, workbook, totalRow, 3, sum+" VND", style2);
    }

    public void exportDataMovieToExcel(HttpServletResponse response, LocalDate date, List<RevenueMovieResponseDto> list) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Doanh thu");

        createHeaderRowForMovie(workbook, sheet, date);
        writeDataLinesForMovie(workbook, sheet, list);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
    private void createHeaderRowForCinema(XSSFWorkbook workbook, XSSFSheet sheet, LocalDate date) {
        CellStyle titleStyle = workbook.createCellStyle();
        XSSFFont titleFont = workbook.createFont();
        titleFont.setFontHeight(16);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);

        Row row1 = sheet.createRow(0);
        String title = "THỐNG KÊ DOANH THU THEO RẠP";
        if (date != null) {
            title += String.format(" (%02d/%d)", date.getMonthValue(), date.getYear());
        }

        createCell(sheet, workbook, row1, 0, title, titleStyle);
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 3));

        // Style cho các cột header
        CellStyle headerStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(12);
        headerStyle.setFont(font);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        Row row2 = sheet.createRow(1);
        createCell(sheet, workbook, row2, 0, "STT", headerStyle);
        createCell(sheet, workbook, row2, 1, "Tên rạp ", headerStyle);
        createCell(sheet, workbook, row2, 2, "Số vé bán ra", headerStyle);
        createCell(sheet, workbook, row2, 3, "Doanh thu", headerStyle);
    }
    private void writeDataLinesForCinema(XSSFWorkbook workbook, XSSFSheet sheet, List<RevenueCinemaResponseDto> list) {
        int rowCount = 2;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        font.setBold(Boolean.FALSE);
        style.setFont(font);
        long sum = 0;
        for (int i = 0; i < list.size(); i++) {
            Row row = sheet.createRow(rowCount++);
            createCell(sheet, workbook, row, 0, i + 1, style);
            createCell(sheet, workbook, row, 1, list.get(i).getCinemaName(), style);
            createCell(sheet, workbook, row, 2, list.get(i).getSumTickets(), style);
            createCell(sheet, workbook, row, 3, list.get(i).getTotal(), style);
            sum += list.get(i).getTotal();
        }

        // Ghi dòng tổng doanh thu
        CellStyle style2 = workbook.createCellStyle();
        Row totalRow = sheet.createRow(rowCount);
        XSSFFont boldFont = workbook.createFont();
        boldFont.setBold(true);
        boldFont.setFontHeight(16);
        style2.setFont(boldFont);
        createCell(sheet, workbook, totalRow, 3, (sum + "VND"), style2);
    }
    public void exportDataCinemaToExcel(HttpServletResponse response, LocalDate date, List<RevenueCinemaResponseDto> list) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Doanh thu");

        createHeaderRowForCinema(workbook, sheet, date);
        writeDataLinesForCinema(workbook, sheet, list);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
