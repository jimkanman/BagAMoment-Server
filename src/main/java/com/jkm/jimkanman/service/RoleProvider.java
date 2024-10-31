package com.jkm.jimkanman.service;

import lombok.Getter;

import java.util.List;

@Getter
public class RoleProvider {
    private final static String ROLE_USER = "ROLE_USER";
    private final static String ROLE_ADMIN = "ROLE_ADMIN";

    public static List<String> getUserRoles(){
        return List.of(ROLE_USER);
    }

    public static List<String> getAdminRoles(){
        return List.of(ROLE_ADMIN, ROLE_USER);
    }
}
