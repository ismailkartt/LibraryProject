package com.tm3library.service;

import com.tm3library.domain.Book;
import com.tm3library.dto.response.BookResponse;
import com.tm3library.dto.response.ReportResponse;
import com.tm3library.mapper.BookMapper;
import com.tm3library.report.ExcelReporter;
import com.tm3library.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
public class ReportService {

    private final BookService bookService;
    private final AuthorService authorService;
    private final PublisherService publisherService;
    private final CategoryService categoryService;
    private final LoanService loanService;
    private final UserService userService;

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;


    public ReportService(BookService bookService,
                         AuthorService authorService,
                         PublisherService publisherService,
                         CategoryService categoryService,
                         LoanService loanService,
                         UserService userService, BookRepository bookRepository, BookMapper bookMapper) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.publisherService = publisherService;
        this.categoryService = categoryService;
        this.loanService = loanService;
        this.userService = userService;
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    public ByteArrayInputStream getReport() {

        ReportResponse reportResponse= new ReportResponse();
        reportResponse.setBooks(bookService.getAllBooks().size());
        reportResponse.setAuthors(authorService.getAllAuthors().size());
        reportResponse.setPublishers(publisherService.getAllpublishers().size());
        reportResponse.setCategories(categoryService.getAllCategories().size());
        reportResponse.setLoans(loanService.getAllLoans().size());

        reportResponse.setMembers(userService.getAllMembers().size());
        reportResponse.setExpiredBooks(loanService.getAllExpiredBooks().size());
        reportResponse.setUnreturnedBooks(loanService.getAllUnreturnedBooks().size());

        try {
            return ExcelReporter.getExcelReport(reportResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Page<BookResponse> findMostPopularBooksWithPage(Pageable pageable, int amount) {

        Page<Book> bookPage = bookRepository.findMostPopularBooks(pageable,amount);



        return bookPage.map(book -> bookMapper.bookToBookResponse(book));

    }

}
