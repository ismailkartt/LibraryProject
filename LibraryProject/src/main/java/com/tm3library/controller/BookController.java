package com.tm3library.controller;


import com.tm3library.domain.Author;
import com.tm3library.domain.Category;
import com.tm3library.domain.Publisher;
import com.tm3library.dto.response.AuthorResponse;
import com.tm3library.dto.response.BookResponse;
import com.tm3library.dto.request.BookRequest;
import com.tm3library.service.AuthorService;
import com.tm3library.service.BookService;
import com.tm3library.service.CategoryService;
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
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final PublisherService publisherService;

    private final CategoryService categoryService;

    public BookController(BookService bookService, AuthorService authorService, PublisherService publisherService, CategoryService categoryService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.publisherService = publisherService;
        this.categoryService = categoryService;
    }

    @PostMapping("/{imageId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookResponse> saveBook(@PathVariable String imageId, @Valid @RequestBody BookRequest bookRequest){

        Author author = authorService.findAuthorById(bookRequest.getAuthorId());

        Publisher publisher = publisherService.findPublisherById(bookRequest.getPublisherId());

        Category category= categoryService.findCategoryById(bookRequest.getCategoryId());

        BookResponse bookResponse=bookService.saveBook(imageId, bookRequest, author, publisher, category);

        return ResponseEntity.ok(bookResponse);

    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id){



        BookResponse bookResponse=bookService.findById(id);

        return ResponseEntity.ok(bookResponse);

    }

    @GetMapping
    public ResponseEntity<Page<BookResponse>> getAllBooksWithPage(@RequestParam("page") int page,
                                                                      @RequestParam("size") int size,
                                                                      @RequestParam("sort") String prop,
                                                                      @RequestParam(value = "type",
                                                                              required = false,
                                                                              defaultValue = "ASC") Sort.Direction type
    ){

        Pageable pageable = PageRequest.of(page, size, Sort.by(type, prop));

        Page<BookResponse> pageResponse= bookService.findAllWithPage(pageable);

        return ResponseEntity.ok(pageResponse);

    }

    // !!!!!!! UPDATE !!!!!!!
    @PutMapping("/id")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookResponse> updateBook(@RequestParam("id") Long id,
                                                   @RequestParam("imageId") String imageId, 
                                                   @Valid @RequestBody BookRequest bookRequest){
        
        BookResponse bookResponse= bookService.updateBook(id,imageId,bookRequest);
        
        return ResponseEntity.ok(bookResponse);
        
    }

    // DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookResponse> deleteBook(@PathVariable Long id){

        BookResponse bookResponse=bookService.removeBookById(id);

        return ResponseEntity.ok(bookResponse);

    }


}
