package com.tm3library.repository;

import com.tm3library.domain.Book;
import com.tm3library.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

// ***** Exist by email ********
    @EntityGraph(attributePaths = "roles")
    Optional<User> findByEmail(String email);


    boolean existsByEmail(String email);

    //@EntityGraph
    Optional<User> findUserById(Long id);

    @EntityGraph(attributePaths = "roles")
    Page<User> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "roles")
    List<User> getAllBy();
}
