package com.tm3library.controller;

import com.tm3library.domain.Author;
import com.tm3library.dto.request.AuthorRequest;
import com.tm3library.dto.response.AuthorResponse;
import com.tm3library.service.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;


    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping("/publishers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthorResponse> createAuthor(@Valid @RequestBody AuthorRequest authorRequest){

       AuthorResponse authorResponse= authorService.createAuthor(authorRequest);

       return ResponseEntity.ok(authorResponse);
    }

    // http://localhost:8080/authors/1
    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponse> getAuthorById(@PathVariable Long id){

        AuthorResponse authorResponse= authorService.getAuthorById(id);

        return ResponseEntity.ok(authorResponse);

    }

    // http://localhost:8080/authors?page=0&size=10&sort=name&type=ASC
    @GetMapping
    public ResponseEntity<Page<AuthorResponse>> getAllAuthorsWithPage(@RequestParam("page") int page,
                                                                      @RequestParam("size") int size,
                                                                      @RequestParam("sort") String prop,
                                                                      @RequestParam(value = "type",
                                                                              required = false,
                                                                              defaultValue = "ASC") Sort.Direction type
    ){

        Pageable pageable = PageRequest.of(page, size, Sort.by(type, prop));

        Page<AuthorResponse> pageResponse= authorService.findAllWithPage(pageable);

        return ResponseEntity.ok(pageResponse);

    }

    // Update
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthorResponse> updateAuthor(@PathVariable Long id,
                                                       @Valid @RequestBody AuthorRequest authorRequest){

        authorService.updateAuthor(id,authorRequest);

        AuthorResponse authorResponse= authorService.getAuthorById(id);

        return ResponseEntity.ok(authorResponse);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthorResponse> deleteAuthor(@PathVariable Long id){

        AuthorResponse authorResponse= authorService.removeAuthorById(id);

        return  ResponseEntity.ok(authorResponse);

    }



}
