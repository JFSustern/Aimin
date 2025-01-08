package com.oimc.aimin.es.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
public enum ErrorType {
    ID_NOT_NULL(3001, "id不能为空"),
    LIST_NOT_EMPTY(3002,"list不能为empty");


    private final int code;
    private final String message;

    ErrorType(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
