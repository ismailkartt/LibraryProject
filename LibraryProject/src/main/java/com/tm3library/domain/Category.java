package com.tm3library.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Table(name = "t_category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 80,nullable = false) // unique biz ekledik denemek amaçlı
    private String name;

    @Column(nullable = false)
    private Boolean builtIn=false;

    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int sequence;

    @OneToMany(mappedBy = "category", orphanRemoval = true,cascade = CascadeType.REMOVE)
    private List<Book> bookList =new ArrayList<>();

}
