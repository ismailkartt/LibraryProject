package com.tm3library.domain;

import com.tm3library.domain.enums.RoleTypes;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "t_role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleTypes type;


    @Override
    public String toString() {
        return "Role{" +
                ", name='" + type + '\'' +
                '}';
    }
}
