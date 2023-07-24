package com.tm3library.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor


@Entity
@Table(name = "t_author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 70)
    private String name;

    @Column(nullable = false)
    private boolean builtIn=false;

    @OneToMany(mappedBy = "author", orphanRemoval = true,cascade = CascadeType.REMOVE)
    private List<Book> bookList;

    @Override
    public String toString() {
        return "Author{" +
                "bookList=" + bookList +
                '}';
    }
}
