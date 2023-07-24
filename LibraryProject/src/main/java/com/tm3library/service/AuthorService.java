package com.tm3library.service;

import com.tm3library.domain.Author;
import com.tm3library.dto.request.AuthorRequest;
import com.tm3library.dto.response.AuthorResponse;
import com.tm3library.exception.BadRequestException;
import com.tm3library.exception.ResourceNotFoundException;
import com.tm3library.exception.message.ErrorMessage;
import com.tm3library.mapper.AuthorMapper;
import com.tm3library.repository.AuthorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;


    public AuthorService(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;

    }

    // Author oluşturma !!!!!!
    public AuthorResponse createAuthor(AuthorRequest authorRequest) {


        Author author = authorMapper.authorRequestToAuthor(authorRequest);

        authorRepository.save(author);

        return  authorMapper.authorToAuthorResponse(author);


    }


    public AuthorResponse getAuthorById(Long id) {

        Author author= findAuthorById(id);

        return authorMapper.authorToAuthorResponse(author);

    }

    // Yardımcı method !!!
    public Author findAuthorById(Long id){

        Author author= authorRepository.
                findAuthorById(id).orElseThrow(()-> new ResourceNotFoundException(String.format(ErrorMessage.AUTHOR_NOT_FOUND_EXCEPTION,id)));

        return author;
    }


    public Page<AuthorResponse> findAllWithPage(Pageable pageable) {

        Page<Author> authorPage = authorRepository.findAll(pageable);

        return authorPage.map(author -> authorMapper.authorToAuthorResponse(author));

    }

    public void updateAuthor(Long id, AuthorRequest authorRequest) {

        Author author = findAuthorById(id);

        if(author.isBuiltIn()){
            throw new ResourceNotFoundException(String.format(ErrorMessage.AUTHOR_NOT_FOUND_EXCEPTION,id));
        }

        author.setName(authorRequest.getName());
        authorRepository.save(author);

    }


    public AuthorResponse removeAuthorById(Long id) {

        Author author = findAuthorById(id);
        AuthorResponse authorResponse= authorMapper.authorToAuthorResponse(author);

        // Built-in
        if(author.isBuiltIn()){
            throw new ResourceNotFoundException(String.format(ErrorMessage.AUTHOR_NOT_FOUND_EXCEPTION,id));
        }


        // Author'un kitabı var mı kontrol et !!!
        if (!(author.getBookList().size()==0)){
            throw new BadRequestException(String.format(ErrorMessage.AUTHOR_CANNOT_DELETE_EXCEPTION,id));
        }


        authorRepository.delete(author);

        return authorResponse;

    }

    public List<Author> getAllAuthors() {

        return authorRepository.getAllBy();
    }
}
