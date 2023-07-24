package com.tm3library.repository;


import com.tm3library.domain.BookImageFile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageFileRepository extends JpaRepository<BookImageFile, String> {
    @EntityGraph(attributePaths = "id")
    Optional<BookImageFile> findImageById(String imageId);
}
