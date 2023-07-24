package com.tm3library.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "t_bookimagefile")
public class BookImageFile {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String name;

    private String type;

    private long length;

    @OneToOne(cascade = CascadeType.ALL)
    private BookImageData bookImageData;

    public BookImageFile(String name, String type, BookImageData bookImageData) {
        this.name = name;
        this.type = type;
        this.length = bookImageData.getData().length;
        this.bookImageData = bookImageData;
    }

}
