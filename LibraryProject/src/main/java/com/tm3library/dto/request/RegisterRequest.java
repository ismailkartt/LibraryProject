package com.tm3library.dto.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {



    @Size(max=50)
    @NotBlank(message="Please provide your First Name")
    private String firstName;

    @Size(max=50)
    @NotBlank(message="Please provide your First Name")
    private String lastName;


    @Size(max=100)
    @NotBlank(message="Please provide your address")
    private String address;

    @Pattern(regexp = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$", //(541) 317-8828
            message = "Please provide valid phone number")
    @Size(min=14, max=14)
    @NotBlank(message = "Please provide your phone number")
    private String phone;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    @NotNull(message = "Please provide your birthday")
    private String birthDate;

    @Size(min=10 ,max=80)
    @Email(message = "Please provide valid e-mail")
    private String email;

    @Size(min=4, max=20, message="Please provide Correct Size of Password")
    @NotBlank(message="Please provide your password")
    private String password;



}
