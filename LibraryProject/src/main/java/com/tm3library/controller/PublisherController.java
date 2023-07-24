package com.tm3library.controller;

import com.tm3library.dto.request.AuthorRequest;
import com.tm3library.dto.request.PublisherRequest;
import com.tm3library.dto.response.AuthorResponse;
import com.tm3library.dto.response.PublisherResponse;
import com.tm3library.service.PublisherService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PublisherResponse> createPublisher(@Valid @RequestBody PublisherRequest publisherRequest){

        PublisherResponse publisherResponse= publisherService.createPublisher(publisherRequest);

        return ResponseEntity.ok(publisherResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherResponse> getPublisherById(@PathVariable Long id){

        PublisherResponse publisherResponse= publisherService.getPublisherById(id);

        return ResponseEntity.ok(publisherResponse);

    }

    @GetMapping
    public ResponseEntity<Page<PublisherResponse>> getAllAuthorsWithPage(@RequestParam("page") int page,
                                                                      @RequestParam("size") int size,
                                                                      @RequestParam("sort") String prop,
                                                                      @RequestParam(value = "type",
                                                                              required = false,
                                                                              defaultValue = "ASC") Sort.Direction type
    ){

        Pageable pageable = PageRequest.of(page, size, Sort.by(type, prop));

        Page<PublisherResponse> pageResponse= publisherService.findAllWithPage(pageable);

        return ResponseEntity.ok(pageResponse);

    }

    //-----------------------------------------------------------------

    //!!! Update !!!
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PublisherResponse> updatePublisher(@PathVariable Long id,
                                                       @Valid @RequestBody PublisherRequest publisherRequest){

        publisherService.updatePublisher(id,publisherRequest);

        PublisherResponse publisherResponse= publisherService.getPublisherById(id);

        return ResponseEntity.ok(publisherResponse);
    }


    //!!! Delete !!!
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PublisherResponse> deletePublisher(@PathVariable Long id){

        PublisherResponse publisherResponse= publisherService.removePublisherById(id);

        return  ResponseEntity.ok(publisherResponse);

    }


}
