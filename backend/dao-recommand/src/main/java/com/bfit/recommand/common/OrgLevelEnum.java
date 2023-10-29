package com.bfit.recommand.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum OrgLevelEnum {

    ORGANIZATION(0, "default organization"),
    PERSON(100, "person level"),

    ;
    Integer code;
    String status;

    public static boolean isOrg(Integer code){
        return Arrays.stream(values()).anyMatch(x->x.code.equals(code));
    }

}
