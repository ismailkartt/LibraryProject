package com.tm3library.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "t_book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 80, nullable = false)
    private String name;

    @Column(length = 17, nullable = false)
    private String isbn;

    @Column(nullable = true)
    private int pageCount;

    @Column(nullable = true)
    private int publishDate;

    @Column(nullable = false)
    private Boolean loanable=true;

    @Column(length = 6,nullable = false)
    private String shelfCode;

    @Column(nullable = false)
    private Boolean active=true;

    @Column(nullable = false)
    private Boolean featured=false;

    @Column(nullable = false)
    private LocalDateTime createDate;

    private Boolean builtIn=false;


    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "book_id")
    private Set<BookImageFile> image;

    @ManyToOne
    @JoinColumn(name = "authorId", referencedColumnName = "id" )
    private Author author;

    @ManyToOne
    @JoinColumn
    private Publisher publisher;

    @ManyToOne
    //@JoinColumn
    private Category category;

}
