package com.tm3library.dto.response;

import lombok.*;


import javax.persistence.Embeddable;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@Embeddable
public class BookResponse {

    private Long id;

    private String name;

    private String isbn;

    private int pageCount;

    private int publishDate;

    private String shelfCode;

    private Boolean loanable=true;

    private Boolean active=true;

    private Boolean featured=false;

    private Boolean builtIn=false;

    private LocalDateTime createDate;

    private Long authorId;

    private Long publisherId;

    private Long categoryId;

    private Set<String> image;


}
