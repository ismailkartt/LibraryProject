package com.tm3library.controller;

import com.tm3library.domain.Book;
import com.tm3library.domain.User;
import com.tm3library.dto.request.LoanRequest;
import com.tm3library.dto.request.LoanUpdateRequest;
import com.tm3library.dto.response.LoanAdminResponse;
import com.tm3library.dto.response.LoanResponse;
import com.tm3library.service.BookService;
import com.tm3library.service.LoanService;
import com.tm3library.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;

    private final UserService userService;

    private final BookService bookService;

    public LoanController(LoanService loanService, UserService userService, BookService bookService) {
        this.loanService = loanService;
        this.userService = userService;
        this.bookService = bookService;
    }


    // Create Loan - Page 24
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<LoanResponse> createLoan(@Valid @RequestBody LoanRequest loanRequest){
        LoanResponse loanResponse = loanService.createLoan(loanRequest);
        return ResponseEntity.ok(loanResponse);
    }

    // Get Loans of authenticated users - Page 19
    @GetMapping
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<Page<LoanResponse>> getAuthMemberLoans(@RequestParam("page") int page,
                                                                 @RequestParam("size") int size,
                                                                 @RequestParam("sort") String prop,
                                                                 @RequestParam(value = "type",
                                                                   required = false,
                                                                   defaultValue = "DESC") Sort.Direction type){
        Pageable pageable = PageRequest.of(page, size, Sort.by(type, prop));

        Page<LoanResponse> pageResponse= loanService.findAuthMemberLoansWithPage(pageable);

        return ResponseEntity.ok(pageResponse);
    }


    // Get Loan with Id - Page 20
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<LoanResponse> getLoanWithId(@PathVariable Long id){

        LoanResponse loanResponse=loanService.getLoanWithId(id);

        return ResponseEntity.ok(loanResponse);

    }

    // get user's loans by user ID - Page 21
    @GetMapping("/user")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<Page<LoanResponse>> getUserLoansById(@RequestParam("userId") Long userId,
                                                               @RequestParam("page") int page,
                                                               @RequestParam("size") int size,
                                                               @RequestParam("sort") String prop,
                                                               @RequestParam(value = "type",
                                                                         required = false,
                                                                         defaultValue = "DESC") Sort.Direction type){
        Pageable pageable = PageRequest.of(page, size, Sort.by(type, prop));

        User user = userService.getUser(userId);

        Page<LoanResponse> pageResponse= loanService.getUserLoansById(user,pageable);

        return ResponseEntity.ok(pageResponse);
    }

    // get book's loan by book ID - Page 22
    @GetMapping("/book")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<Page<LoanResponse>> getBookLoanById(@RequestParam("bookId") Long bookId,
                                                               @RequestParam("page") int page,
                                                               @RequestParam("size") int size,
                                                               @RequestParam("sort") String prop,
                                                               @RequestParam(value = "type",
                                                                       required = false,
                                                                       defaultValue = "DESC") Sort.Direction type){
        Pageable pageable = PageRequest.of(page, size, Sort.by(type, prop));


        Book book=bookService.findBookById(bookId);

        Page<LoanResponse> pageResponse= loanService.getBookLoanById(book,pageable);

        return ResponseEntity.ok(pageResponse);
    }

    // get Loan with Loan ID - Page 23
    @GetMapping("/auth/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<LoanAdminResponse> getLoanDetails(@PathVariable Long id){

        LoanAdminResponse lar=loanService.getLoanDetails(id);

        return ResponseEntity.ok(lar);

    }


    // UPDATE - Page 25
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<LoanResponse> updateLoan(@PathVariable Long id,
                                                   @Valid@RequestBody LoanUpdateRequest loanUpdateRequest){

        LoanResponse loanResponse=loanService.updateLoan(id,loanUpdateRequest);

        return ResponseEntity.ok(loanResponse);

    }


}
