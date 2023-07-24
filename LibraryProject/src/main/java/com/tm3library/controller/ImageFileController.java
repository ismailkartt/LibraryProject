package com.tm3library.controller;

import com.tm3library.dto.response.ImageSaveResponse;
import com.tm3library.dto.response.ResponseMessage;
import com.tm3library.service.ImageFileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class ImageFileController {


    private final ImageFileService imageFileService;



    public ImageFileController(ImageFileService imageFileService) {
        this.imageFileService = imageFileService;
    }

    // Upload İmage
    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ImageSaveResponse> uploadFile(@RequestParam("file")MultipartFile file){

        String imgId=imageFileService.saveImage(file);

        ImageSaveResponse response= new ImageSaveResponse(imgId,ResponseMessage.IMAGE_SAVE_RESPONSE_MESSAGE, true);

        return ResponseEntity.ok(response);
    }


}
