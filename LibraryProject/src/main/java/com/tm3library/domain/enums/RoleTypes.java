package com.tm3library.domain.enums;

public enum RoleTypes {
    ROLE_MEMBER("Member"),
    ROLE_EMPLOYEE("Employee"),
    ROLE_ADMIN("Administrator");

    private String name;

    private RoleTypes(String name){
        this.name=name;
    }

    public String getName(){
        return name;
    }

}

