package com.tm3library.repository;

import com.tm3library.domain.Book;
import com.tm3library.domain.Loan;
import com.tm3library.domain.User;
import com.tm3library.dto.response.LoanResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    @EntityGraph(attributePaths = {"book","user","book.image"})
    Page<Loan> findAllByUser(User user, Pageable pageable);

    @EntityGraph(attributePaths = {"book","user","book.image"})
    Optional<Loan> findByIdAndUser(Long id, User user);
    @EntityGraph(attributePaths = {"book","user","book.image"})
    Page<Loan> findAllByBook(Book book, Pageable pageable);

    @EntityGraph(attributePaths = "id")
    List<Loan> getAllBy();
}
