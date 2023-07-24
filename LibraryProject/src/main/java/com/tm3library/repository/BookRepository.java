package com.tm3library.repository;

import com.tm3library.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("select count(*) from Book b join b.image img where img.id=:id")
    Integer findBookCountByImageId(@Param("id") String id);


    @EntityGraph(attributePaths = {"image"})
    Optional<Book> findBookById(Long id);

    @EntityGraph(attributePaths = {"image"})
    Page<Book> findAll(Pageable pageable);

    @Query("select b from Book b join b.image im where im.id=:id")
    List<Book> findBookByImageId(@Param("id") String id);

    @EntityGraph(attributePaths = "id")
    List<Book> getAllBy();

    @Query("")
    Page<Book> findMostPopularBooks(Pageable pageable, int amount);
}
