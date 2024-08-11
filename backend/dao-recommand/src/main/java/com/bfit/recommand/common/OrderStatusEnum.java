package com.bfit.recommand.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.stream.Collectors;

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

    public static boolean checkUnApplicableStatus(Integer status){
        return Arrays.asList(ACCEPTED, PROCESSING, COMPLETED, FINAL_CONFIRMED).stream()
                .map(OrderStatusEnum::getCode)
                .collect(Collectors.toList())
                .contains(status)
                || !Arrays.stream(values())
                        .map(OrderStatusEnum::getCode)
                        .collect(Collectors.toList())
                        .contains(status);
    }


}