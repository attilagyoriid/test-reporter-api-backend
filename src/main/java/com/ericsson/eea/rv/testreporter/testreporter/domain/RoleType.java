package com.ericsson.eea.rv.testreporter.testreporter.domain;

public enum RoleType {
    ROLE_ADMIN, ROLE_EVALUATOR, ROLE_READER;


    public static boolean isExistByName(String name) {
        for (RoleType roleType : RoleType.values()) {
            if (roleType.name().equalsIgnoreCase(name))
                return true;
        }
        return false;
    }
}
