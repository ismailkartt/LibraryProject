package com.tm3library.service;

import com.tm3library.domain.Role;
import com.tm3library.domain.enums.RoleTypes;
import com.tm3library.exception.ResourceNotFoundException;
import com.tm3library.exception.message.ErrorMessage;
import com.tm3library.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;


    public Role findByType(RoleTypes roleType){

        Role role= roleRepository.findByType(roleType).
                orElseThrow(()-> new ResourceNotFoundException(String.format(ErrorMessage.ROLE_NOT_FOUND_EXCEPTION, roleType.name())));

        return role;
    }


}
