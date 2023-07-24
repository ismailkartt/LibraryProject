package com.tm3library.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse {

    private int books;

    private int authors;

    private int publishers;

    private int categories;

    private int loans;

    private int unreturnedBooks;

    private int expiredBooks;

    private int members;

}
