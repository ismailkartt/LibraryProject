package com.tm3library.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tm3library.domain.BookImageFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookRequest {

    private Long id;

    @Size(min= 2, max= 80)
    @NotNull(message = "Please provide book name")
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @NotNull(message = "Please provide ISBN")
    @Pattern(regexp = "^(\\d{3}-\\d{2}-\\d{5}-\\d{2}-\\d)$")
    private String isbn;

    @NotNull(message = "Please provide book pages")
    private int pageCount;

    @NotNull(message = "Please provide book's author id")
    private Long authorId;

    @NotNull(message = "Please provide publisher id")
    private Long publisherId;

    @NotNull(message = "Please provide book publish date")
//    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
//    @Pattern(regexp = "^\\d{4}$")
   // @NotNull(message = "Please provide book publish date")
   // @JsonFormat(pattern="yyyy")
   // private Date publishDate;
    @Max(2023) // Hocaya sor!!!
    private int publishDate;

    @NotNull(message = "Please provide book category")
    private Long categoryId;

    @NotNull(message = "Please provide shelf code")
    private String shelfCode;

    private Boolean loanable=true;

    private Boolean builtIn=false;

    private Boolean active=false;

    private Boolean featured=false;

    private LocalDateTime createDate= LocalDateTime.now();

    private Set<String> image;


}
