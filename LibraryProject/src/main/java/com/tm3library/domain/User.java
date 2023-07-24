package com.tm3library.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "t_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false)
    private String firstName;

    @Column(length = 30, nullable = false)
    private String lastName;

    @Column(nullable = false)
    private int score;

    @Column(length = 100, nullable = false)
    private String address;

    @Column(length = 14, nullable = false)
    private String phone;

    @Column(nullable = false)
    private String birthDate;

    @Column(length = 80, nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDateTime createDate;

    @Column(nullable = true)
    private String resetPasswordCode;

    @Column(nullable = false)
    private Boolean builtIn =false;

    @ManyToMany  // LAZY
    @JoinTable(name="t_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    // ************* Kontrol edilecek !!! ************
    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Loan> loanList = new ArrayList<>();

}
