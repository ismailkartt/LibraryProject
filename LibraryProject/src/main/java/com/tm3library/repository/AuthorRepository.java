package com.tm3library.repository;

import com.tm3library.domain.Author;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    @EntityGraph(attributePaths = "id")
    Optional<Author> findAuthorById(Long id);

    @EntityGraph(attributePaths = "id")
    List<Author> getAllBy();
}
