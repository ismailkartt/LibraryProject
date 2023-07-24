package com.tm3library.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class LoanAdminResponse{

    private UserResponse userResponse;

    //@Embedded
    private BookResponse bookResponse;

    private LocalDateTime loanDate;

    private LocalDateTime expireDate;

    private LocalDateTime returnDate;

    private String notes;
}
