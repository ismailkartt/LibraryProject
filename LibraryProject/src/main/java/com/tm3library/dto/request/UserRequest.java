package com.tm3library.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tm3library.domain.Role;
import com.tm3library.domain.enums.RoleTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest extends RegisterRequest {


    @NotNull
    @Max(2)
    @Min(-2)
    //@Size(min =-2 ,max =2 )
    private int score=0;

    @NotNull
    private Boolean builtIn =false;

    private Set<String> roles;

}


