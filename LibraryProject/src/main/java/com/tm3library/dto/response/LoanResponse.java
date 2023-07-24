package com.tm3library.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tm3library.domain.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.Embedded;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class LoanResponse {

    private Long id;

    //@Embedded
    private BookResponse bookResponse;

    private Long userId;

    private LocalDateTime loanDate;

    private LocalDateTime expireDate;

    private LocalDateTime returnDate;

    private String notes;

}

