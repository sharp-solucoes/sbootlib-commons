package com.libcommons.enums;


import lombok.Getter;

@Getter
public enum UserRoleENUM {
    GUEST(0),
    USER(1),
    ADMIN(2),
    DEV_ADMIN(3);

    private Integer code;

    UserRoleENUM(Integer code) {
        this.code = code;
    }

    public static UserRoleENUM fromCode(int cd) {
        for (UserRoleENUM userRoleENUM : UserRoleENUM.values()) {
            if (userRoleENUM.getCode() == cd) {
                return userRoleENUM;
            }
        }
        throw new IllegalArgumentException("No enum found for code: " + cd);
    }
}