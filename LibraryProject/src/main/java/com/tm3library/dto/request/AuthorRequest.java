package com.tm3library.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorRequest {

    @Size(min=4, max=70)
    @NotNull(message = "Please provide author name")
    private String name;


}
