package com.tm3library.mapper;

import com.tm3library.domain.Author;
import com.tm3library.domain.Book;
import com.tm3library.dto.request.AuthorRequest;
import com.tm3library.dto.response.AuthorResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    @Mapping(target = "id", ignore = true)
    Author authorRequestToAuthor(AuthorRequest authorRequest);


    @Mapping(target = "bookList", source = "bookList", qualifiedByName = "getBookAsString")
    AuthorResponse authorToAuthorResponse(Author author);

    @Named("getBookAsString")
    public  static List<String> getBooksName(List<Book> bookList){

        List<String> books=new ArrayList<>();

        books= bookList.stream().map(book -> book.getName().toString()).collect(Collectors.toList());

        return books;
    }

}
