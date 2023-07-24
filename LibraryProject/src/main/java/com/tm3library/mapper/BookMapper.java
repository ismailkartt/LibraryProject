package com.tm3library.mapper;

import com.tm3library.domain.*;
import com.tm3library.dto.request.BookRequest;
import com.tm3library.dto.response.BookResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BookMapper {


    // Book request ---> Book
    @Mapping(target = "image", ignore = true)
    Book bookRequestToBook(BookRequest bookRequest);

    // Book ---> BookResponse
    @Mapping(source = "author", target = "authorId", qualifiedByName = "getAuthorId")
    @Mapping(source = "publisher", target = "publisherId", qualifiedByName = "getPublisherId")
    @Mapping(source = "category", target = "categoryId", qualifiedByName = "getCategoryId")
    @Mapping(source = "image", target = "image", qualifiedByName = "getImageAsString")
    BookResponse bookToBookResponse(Book book);

    @Named("getImageAsString")
    public static Set<String> getImageIds(Set<BookImageFile> imageFiles) {

        Set<String> imgs = new HashSet<>();
        imgs = imageFiles.stream().map(imFile -> imFile.getId().toString()).collect(Collectors.toSet());
        return imgs;
    }

    @Named("getAuthorId")
    public static Long getAuthorId(Author author){

        return author.getId();

    }

    @Named("getPublisherId")
    public static Long getPublisherId(Publisher publisher){

        return publisher.getId();

    }
    @Named("getCategoryId")
    public static Long getCategoryId(Category category){

        return category.getId();

    }




}
