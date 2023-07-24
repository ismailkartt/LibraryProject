package com.tm3library.dto.response;

import com.tm3library.domain.Loan;
import com.tm3library.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Long id;


    private String firstName;

    private String lastName;

    private int score;

    private String address;

    private String phone;

    private String birthDate;

    private String email;

    private String password;

    private LocalDateTime createDate;

    private String resetPasswordCode;

    private Boolean builtIn =false;

    private Set<String> roles;

    public void setRoles(Set<Role> roles) {
        Set<String> roleStr = new HashSet<>();
        roles.forEach(r -> {
            roleStr.add(r.getType().getName()); // Customer , Administrator
        });

        this.roles = roleStr;
    }


}
