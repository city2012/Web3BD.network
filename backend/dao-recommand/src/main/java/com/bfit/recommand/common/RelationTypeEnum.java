package com.bfit.recommand.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
@AllArgsConstructor
public enum RelationTypeEnum {

    POST(1, "POST"),
    JOIN(2, "JOIN"),
    RELATED(3, "ACCEPTED"),


    ;
    Integer code;
    String status;

    public static RelationTypeEnum fetchByCode(Integer code){
        if (Objects.isNull(code)){
            return null;
        }
        return Arrays.stream(values()).filter(x -> x.code.equals(code)).findFirst().orElse(null);
    }

}
