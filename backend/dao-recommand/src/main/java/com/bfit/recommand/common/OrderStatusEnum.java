package com.bfit.recommand.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

    INIT(0, "INIT"),
    POSTED(1, "POSTED"),
    APPLIED(2, "APPLIED"),
    ACCEPTED(3, "ACCEPTED"),
    PROCESSING(4,"PROCESSING"),
    COMPLETED(5, "COMPLETED"),
    FINAL_CONFIRMED(9, "FINAL_CONFIRMED")

    ;
    Integer code;
    String status;


}
