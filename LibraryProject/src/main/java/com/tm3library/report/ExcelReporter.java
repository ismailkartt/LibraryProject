package com.tm3library.report;


import com.tm3library.domain.Book;
import com.tm3library.dto.response.ReportResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelReporter {

//    // !!! USER ******************
//    static String SHEET_USER = "Users";
//    static String[] USER_HEADERS = {"id","FirstName","LastName","PhoneNumber",
//            "Email","Address","ZipCode","Roles"};

    // !!! BOOK ******************
//    static String SHEET_BOOK = "Books";
//    static String[] BOOK_HEADERS = {"id","BookName","ISBN","Pages","ShelfCode","CreateDate",
//            "PublishDate","AuthorName","PublisherName","CategoryName"};

    // !!! Library - Page 55 ******************
    static String SHEET_REPORT = "Report";
    static String[] REPORT_HEADERS = {"Books","Authors","Publishers","Categories","Loans","UnreturnedBooks",
            "ExpiredBooks","Members"};


    //*********************************************************
    //******************* PAGE - 55 ***************************
    //*********************************************************
    public static ByteArrayInputStream getExcelReport(ReportResponse reportResponse) throws IOException {
        Workbook workbook = new XSSFWorkbook();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Sheet sheet = workbook.createSheet(SHEET_REPORT);
        Row headerRow =  sheet.createRow(0);

        // header row dolduruluyor
        for(int i=0; i< REPORT_HEADERS.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(REPORT_HEADERS[i]);
        }

        // dataları dolduruyoruz


            Row row = sheet.createRow(1);
            row.createCell(0).setCellValue(reportResponse.getBooks());
            row.createCell(1).setCellValue(reportResponse.getAuthors());
            row.createCell(2).setCellValue(reportResponse.getPublishers());
            row.createCell(3).setCellValue(reportResponse.getCategories());
            row.createCell(4).setCellValue(reportResponse.getLoans());
            row.createCell(5).setCellValue(reportResponse.getUnreturnedBooks());
            row.createCell(6).setCellValue(reportResponse.getExpiredBooks());
            row.createCell(7).setCellValue(reportResponse.getMembers());


        workbook.write(out);
        workbook.close();

        return new ByteArrayInputStream(out.toByteArray());

    }

    //*********************************************************
    //******************* Page - 56 ***************************
    //*********************************************************
//    public static ByteArrayInputStream getLibraryExcelReport(List<Book> books) throws IOException {
//        Workbook workbook = new XSSFWorkbook();
//
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        Sheet sheet = workbook.createSheet(SHEET_REPORT);
//        Row headerRow =  sheet.createRow(0);
//
//        // header row dolduruluyor
//        for(int i=0; i< REPORT_HEADERS.length; i++) {
//            Cell cell = headerRow.createCell(i);
//            cell.setCellValue(REPORT_HEADERS[i]);
//        }
//
//        // dataları dolduruyoruz
//        int rowId = 1;
//        for(Book book: books) {
//            Row row = sheet.createRow(rowId++);
//            row.createCell(0).setCellValue(book.getId());
//            row.createCell(1).setCellValue(book.getName());
//            row.createCell(2).setCellValue(book.getIsbn());
//            row.createCell(3).setCellValue(book.getPageCount());
//            row.createCell(4).setCellValue(book.getShelfCode());
//            row.createCell(5).setCellValue(book.getCreateDate().toString());
//            row.createCell(6).setCellValue(book.getPublishDate());
//            row.createCell(7).setCellValue(book.getAuthor().getName());
//            row.createCell(8).setCellValue(book.getPublisher().getName());
//            row.createCell(9).setCellValue(book.getCategory().getName());
//
//        }
//        workbook.write(out);
//        workbook.close();
//
//        return new ByteArrayInputStream(out.toByteArray());
//
//    }


}
