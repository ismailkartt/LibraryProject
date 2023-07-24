package com.tm3library.service;

import com.tm3library.domain.Book;
import com.tm3library.domain.Loan;
import com.tm3library.domain.User;
import com.tm3library.dto.request.LoanRequest;
import com.tm3library.dto.request.LoanUpdateRequest;
import com.tm3library.dto.response.BookResponse;
import com.tm3library.dto.response.LoanAdminResponse;
import com.tm3library.dto.response.LoanResponse;
import com.tm3library.exception.BadRequestException;
import com.tm3library.exception.ConflictException;
import com.tm3library.exception.ResourceNotFoundException;
import com.tm3library.exception.message.ErrorMessage;
import com.tm3library.mapper.BookMapper;
import com.tm3library.mapper.LoanMapper;
import com.tm3library.mapper.UserMapper;
import com.tm3library.repository.LoanRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanService {
    private final LoanMapper loanMapper;
    private final LoanRepository loanRepository;
    private final UserService userService;
    private final BookService bookService;

    private final LoanResponse loanResponse;

    private final LoanAdminResponse loanAdminResponse;

    private final BookMapper bookMapper;

    private final UserMapper userMapper;

    public LoanService(LoanMapper loanMapper, LoanRepository loanRepository, UserService userService, BookService bookService, LoanResponse loanResponse, LoanAdminResponse loanAdminResponse, BookMapper bookMapper, UserMapper userMapper) {
        this.loanMapper = loanMapper;
        this.loanRepository = loanRepository;
        this.userService = userService;
        this.bookService = bookService;
        this.loanResponse = loanResponse;
        this.loanAdminResponse = loanAdminResponse;
        this.bookMapper = bookMapper;
        this.userMapper = userMapper;
    }


    public LoanResponse createLoan(LoanRequest loanRequest) {
        Loan loan = loanMapper.loanRequestToLoan(loanRequest);
        //user
        User user = userService.getUser(loanRequest.getUserId());
        //Book
        Book book = bookService.findBookById(loanRequest.getBookId());

        //Book is Loanable??
        if (!book.getLoanable()){
            throw new ConflictException(String.format(ErrorMessage.THIS_BOOK_IS_IN_LOAN));
        }

        //User önceki loan'larının kontrolü

//       DateTimeFormatter dtf=DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
//
//       LocalDateTime now=LocalDateTime.now();
//
//       String formattedNow=dtf.format(now);
//
//       LocalDateTime a=(LocalDateTime)formattedNow;

        loan.setLoanDate(LocalDateTime.now());

        // User score'a göre expire date
        switch (user.getScore()){
            case 2:
                loan.setExpireDate(LocalDateTime.now().plusDays(5));
                break;
            case 1:
                loan.setExpireDate(LocalDateTime.now().plusDays(4));
                break;
            case 0:
                loan.setExpireDate(LocalDateTime.now().plusDays(3));
                break;
            case -1:
                loan.setExpireDate(LocalDateTime.now().plusDays(2));
                break;
            case -2:
                loan.setExpireDate(LocalDateTime.now().plusDays(1));
                break;
        }

        // User loanList expire date control
        List<Loan> userLoanList=new ArrayList<>(user.getLoanList());

        for (Loan w:userLoanList){  // !!!! UPDATE METHODUNDA KITAP TESLIM EDILDIYSE EXPIRE DATE NULL YAPILACAK !!!!
            if (w.getExpireDate()!=null){
                Long fark= ChronoUnit.DAYS.between(LocalDateTime.now(),w.getExpireDate());
                if(fark<0){
                    throw new BadRequestException(String.format(ErrorMessage.THIS_USER_HAS_EXPIRED_LOAN));
                }
            }
        }



        loan.setReturnDate(null);

        loan.setBook(book);
        loan.setUser(user);
        loanRepository.save(loan);

        user.getLoanList().add(loan);
        book.setLoanable(false);

        LoanResponse loanResponse = loanMapper.loanToLoanResponse(loan);

        BookResponse bookResponse= bookMapper.bookToBookResponse(book);
        loanResponse.setBookResponse(bookResponse);

        return loanResponse;
    }

    public Page<LoanResponse> findAuthMemberLoansWithPage(Pageable pageable) {

        User user=userService.getCurrentUser();
//        user.getLoanList().get(0).getBook()
//        List<Book> bookList = bookService.
//
//        BookResponse bookResponse= bookMapper.bookToBookResponse(book);
//        loanResponse.setBookResponse(bookResponse);

        Page<Loan> loanPage = loanRepository.findAllByUser(user,pageable);

//        for (Loan w:loanPage){
//            BookResponse bookResponse=bookMapper.bookToBookResponse(w.getBook());
//            LoanResponse loanResponse=loanMapper.loanToLoanResponse(w);
//
//            loanResponse.setBookResponse(bookResponse);
//
//            Page<LoanResponse> loanResponsePage=
//        }


        return loanPage.map(loanMapper::loanToLoanResponse);

    }

    public LoanResponse getLoanWithId(Long id) {

        User user=userService.getCurrentUser();

        Loan loan=loanRepository.findByIdAndUser(id,user).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION)));

        Book book= bookService.findBookById(loan.getBook().getId());

        LoanResponse loanResponse=loanMapper.loanToLoanResponse(loan);

        loanResponse.setBookResponse(bookMapper.bookToBookResponse(book));

        return loanResponse;
    }

    public Page<LoanResponse> getUserLoansById(User user,Pageable pageable) {

        Page<Loan> loanPage=loanRepository.findAllByUser(user,pageable);

        return loanPage.map(loanMapper::loanToLoanResponse);

    }

    public Page<LoanResponse> getBookLoanById(Book book, Pageable pageable) {

        Page<Loan> loanPage=loanRepository.findAllByBook(book,pageable);

        return loanPage.map(loanMapper::loanToLoanResponse);

    }

    public LoanAdminResponse getLoanDetails(Long id) {

        Loan loan=loanRepository.findById(id).orElseThrow(()->new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION)));

        User user= loan.getUser();

        Book book= bookService.findBookById(loan.getBook().getId());

        loanAdminResponse.setBookResponse(bookMapper.bookToBookResponse(book));
        loanAdminResponse.setUserResponse(userMapper.userToUserResponse(user));
        loanAdminResponse.setLoanDate(loan.getLoanDate());
        loanAdminResponse.setExpireDate(loan.getExpireDate());
        loanAdminResponse.setReturnDate(loan.getReturnDate());
        loanAdminResponse.setNotes(loan.getNotes());

        return loanAdminResponse;

        //loanMapper.loanToLoanAdminResponse(loan);

    }

    public LoanResponse updateLoan(Long id, LoanUpdateRequest loanUpdateRequest) {

        Loan loan=loanRepository.findById(id).orElseThrow(()->new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION)));
        Book book= bookService.findBookById(loan.getBook().getId());
        Long fark= ChronoUnit.DAYS.between(LocalDateTime.now(),loan.getExpireDate());
        User user= loan.getUser();


        if (fark>=0){
            if (user.getScore()<2) {
                user.setScore(user.getScore()+1);
            }
        } else {
            if (user.getScore()> -2) {
                user.setScore(user.getScore()-1);
            }
        }

        loan.setReturnDate(LocalDateTime.now());
        book.setLoanable(true);


        if (!(loanUpdateRequest.getNotes()==null)){
            loan.setNotes(loanUpdateRequest.getNotes());
        }

        loan.setExpireDate(null);


        LoanResponse loanResponse=loanMapper.loanToLoanResponse(loan);

        loanResponse.setBookResponse(bookMapper.bookToBookResponse(book));


        return loanResponse;



        // Sadece kitap geri alma işlemi için update yapıyoruz.
        // bu yüzden return date her halükarda null olacağı için kontrol etmedik!!!
//        if (loan.getReturnDate()==null){
//            loan.setReturnDate(loanUpdateRequest.getReturnDate());
//        } else if (loan.getReturnDate()!=null) {
//            book.setLoanable(true);
//        }

    }

    public List<Loan> getAllLoans() {

        return loanRepository.getAllBy();
    }

    public List<Loan> getAllExpiredBooks() {

        List<Loan> allLoans=getAllLoans();

        for (Loan w:allLoans){
            if (w.getExpireDate()!=null){
                if (LocalDateTime.now().isBefore(w.getExpireDate())){
                    allLoans.add(w);
                }
            }
        }

        return allLoans;

    }

    public List<Loan> getAllUnreturnedBooks() {

        List<Loan> allLoans=getAllLoans();

        for (Loan t:allLoans){
            if (t.getReturnDate()==null){
                allLoans.add(t);
            }

        }
        return allLoans;

    }


    // YARDIMCI METHOD


}
