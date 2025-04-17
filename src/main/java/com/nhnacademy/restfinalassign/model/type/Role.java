package com.nhnacademy.restfinalassign.model.type;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
    ADMIN, MEMBER, GOOGLE;

    @JsonCreator
    public static Role fromString(String str) {
        for(Role role : Role.values()) {
            if(role.name().equalsIgnoreCase(str)) {
                return role;
            }
        }

        return MEMBER;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }

}
