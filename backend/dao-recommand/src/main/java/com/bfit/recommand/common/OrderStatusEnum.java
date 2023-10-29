package com.bfit.recommand.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

    INIT(0, "INIT"),
    POSTED(1, "POSTED"),
    ACCEPTED(2, "ACCEPTED"),
    PROCESSING(3,"PROCESSING"),
    COMPLETED(4, "COMPLETED"),
    FINAL_CONFIRMED(9, "FINAL_CONFIRMED")

    ;
    int code;
    String status;


}
