package com.tm3library.repository;

import com.tm3library.domain.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String categoryName);

    @EntityGraph(attributePaths = "id")
    List<Category> getAllBy();
}
