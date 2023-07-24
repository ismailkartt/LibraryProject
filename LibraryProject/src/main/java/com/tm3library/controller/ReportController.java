package com.tm3library.controller;

import com.tm3library.dto.response.AuthorResponse;
import com.tm3library.dto.response.BookResponse;
import com.tm3library.service.ReportService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // ************* Book_REPORT ************************
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<Resource> getReport(){
        String fileName = "Books.xlsx";
        ByteArrayInputStream bais = reportService.getReport();
        InputStreamResource file = new InputStreamResource(bais);

        return ResponseEntity.ok().
                header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName).
                contentType(MediaType.parseMediaType("application/vmd.ms-excel")).
                body(file);

    }

    @GetMapping("/most-popular-books")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<Page<BookResponse>> getPopularBooks(@RequestParam("amount") int amount,
                                                              @RequestParam("page") int page,
                                                              @RequestParam("size") int size
                                                              )
    {

        Pageable pageable = PageRequest.of(page, size, Sort.unsorted());

        Page<BookResponse> pageResponse = reportService.findMostPopularBooksWithPage(pageable,amount);

        return ResponseEntity.ok(pageResponse);
    }

}
