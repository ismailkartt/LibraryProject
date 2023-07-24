package com.tm3library.service;

import com.tm3library.domain.BookImageData;
import com.tm3library.domain.BookImageFile;
import com.tm3library.exception.ResourceNotFoundException;
import com.tm3library.exception.message.ErrorMessage;
import com.tm3library.repository.ImageFileRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
public class ImageFileService {

    private final ImageFileRepository imageFileRepository;

    public ImageFileService(ImageFileRepository imageFileRepository) {
        this.imageFileRepository = imageFileRepository;
    }

    public String saveImage(MultipartFile file) {

        BookImageFile imageFile = null;
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        //!!!data
        try {
            BookImageData imData = new BookImageData(file.getBytes());
            imageFile = new BookImageFile(fileName,file.getContentType(),imData);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        imageFileRepository.save(imageFile);

        return imageFile.getId();


    }

    public BookImageFile findImageById(String imageId) {

        return imageFileRepository.findImageById(imageId).orElseThrow(()->new ResourceNotFoundException(String.format(ErrorMessage.IMAGE_NOT_FOUND_EXCEPTION)));

    }


}
