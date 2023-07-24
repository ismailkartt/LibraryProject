package com.tm3library.service;

import com.tm3library.domain.*;
import com.tm3library.dto.request.BookRequest;
import com.tm3library.dto.response.BookResponse;
import com.tm3library.exception.BadRequestException;
import com.tm3library.exception.ConflictException;
import com.tm3library.exception.ResourceNotFoundException;
import com.tm3library.exception.message.ErrorMessage;
import com.tm3library.mapper.BookMapper;
import com.tm3library.repository.BookRepository;
import com.tm3library.repository.PublisherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    private final ImageFileService imageFileService;
    private final PublisherRepository publisherRepository;

    private final AuthorService authorService;

    private final PublisherService publisherService;

    private final CategoryService categoryService;

    public BookService(BookRepository bookRepository, BookMapper bookMapper, ImageFileService imageFileService,
                       PublisherRepository publisherRepository, AuthorService authorService, PublisherService publisherService, CategoryService categoryService) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;

        this.imageFileService = imageFileService;
        this.publisherRepository = publisherRepository;
        this.authorService = authorService;
        this.publisherService = publisherService;
        this.categoryService = categoryService;
    }

    public BookResponse saveBook(String imageId, BookRequest bookRequest, Author author, Publisher publisher, Category category) {

        //image-control
        BookImageFile bookImageFile = imageFileService.findImageById(imageId);

        Integer usedBookCount = bookRepository.findBookCountByImageId(bookImageFile.getId());

        if (usedBookCount>0){
            throw new ConflictException(ErrorMessage.IMAGE_ALREADY_USING_EXCEPTION);
        }

        Book book= bookMapper.bookRequestToBook(bookRequest);

        Set<BookImageFile> imageFiles = new HashSet<>();

        imageFiles.add(bookImageFile);

        book.setImage(imageFiles);
        book.setAuthor(author);
        book.setPublisher(publisher);
        book.setCategory(category);

        author.getBookList().add(book);
        publisher.getBookList().add(book);
        category.getBookList().add(book);

        bookRepository.save(book);

        return bookMapper.bookToBookResponse(book);

    }

    public BookResponse findById(Long id) {

        Book book=findBookById(id);

        return bookMapper.bookToBookResponse(book);

    }

    // Yardımcı Method - Tüm dataları çeker
    public Book findBookById(Long id){

        Book book= bookRepository.
                findBookById(id).orElseThrow(()-> new ResourceNotFoundException(String.format(ErrorMessage.BOOK_NOT_FOUND_EXCEPTION,id)));

        return book;
    }

    public Page<BookResponse> findAllWithPage(Pageable pageable) {

        Page<Book> bookPage = bookRepository.findAll(pageable);

        return bookPage.map(book -> bookMapper.bookToBookResponse(book));

    }


    public BookResponse updateBook(Long id, String imageId, BookRequest bookRequest) {

        // BuiltIn ?
        Book book=findBookById(id);
        if (book.getBuiltIn()){
            throw  new BadRequestException(String.format(ErrorMessage.BOOK_CANNOT_UPDATE_EXCEPTION,id));
        }

        // Resim kullanılıyor mu ?
        BookImageFile bookImageFile= imageFileService.findImageById(imageId);
        List<Book> bookList= bookRepository.findBookByImageId(bookImageFile.getId());

        for (Book b:bookList){
            if (book.getId().longValue()!=b.getId().longValue()){
                throw new ConflictException(String.format(ErrorMessage.IMAGE_HAS_ALREADY_BEEN_USED));
            }
        }

        //Author var mı ?
        authorService.findAuthorById(bookRequest.getAuthorId());
        //Publisher var mı?
        publisherService.findPublisherById(bookRequest.getPublisherId());
        //Category var mı?
        categoryService.findCategoryById(bookRequest.getCategoryId());



        book.getImage().add(bookImageFile);
        book.setAuthor(authorService.findAuthorById(bookRequest.getAuthorId()));
        book.setPublisher(publisherService.findPublisherById(bookRequest.getPublisherId()));
        book.setCategory(categoryService.findCategoryById(bookRequest.getCategoryId()));

        book.setName(bookRequest.getName());
        book.setIsbn(bookRequest.getIsbn());
        book.setPageCount(bookRequest.getPageCount());
        book.setShelfCode(bookRequest.getShelfCode());
        book.setPublishDate(bookRequest.getPublishDate());

        book.setLoanable(bookRequest.getLoanable());
        book.setFeatured(bookRequest.getFeatured());
        book.setActive(bookRequest.getActive());
        book.setBuiltIn(bookRequest.getBuiltIn());

        book.setCreateDate(bookRequest.getCreateDate());

        bookRepository.save(book);

        return bookMapper.bookToBookResponse(book);

    }

    public BookResponse removeBookById(Long id) {

        // BuiltIn ?
        Book book=findBookById(id);
        if (book.getBuiltIn()){
            throw  new BadRequestException(String.format(ErrorMessage.BOOK_CANNOT_UPDATE_EXCEPTION,id));
        }

        // !!!! Loan kontrolü yapılacak !!!!
        if (!(book.getLoanable())){
            throw new BadRequestException(String.format(ErrorMessage.BOOK_IS_IN_LOAN_EXCEPTION));
        }


        bookRepository.delete(book);

        return bookMapper.bookToBookResponse(book);

    }


    public List<Book> getAllBooks() {

        return bookRepository.getAllBy();
    }
}
