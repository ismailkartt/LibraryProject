package com.tm3library.repository;

import com.tm3library.domain.Publisher;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    @EntityGraph(attributePaths = "id")
    List<Publisher> getAllBy();
}

