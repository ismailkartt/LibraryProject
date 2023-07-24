package com.tm3library.repository;

import com.tm3library.domain.Role;
import com.tm3library.domain.enums.RoleTypes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, Long> {


    Optional<Role> findByType(RoleTypes roleType);

}
