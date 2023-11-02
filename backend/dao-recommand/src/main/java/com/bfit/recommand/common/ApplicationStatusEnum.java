package com.bfit.recommand.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplicationStatusEnum {

    REJECTED(1, "REJECTED"),
    APPLIED(2, "APPLIED"),
    ACCEPTED(3, "ACCEPTED"),

    ;
    Integer code;
    String status;


}
