package com.oimc.aimin.base.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class BusinessException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    private String message;

    public static BusinessException of(String message) {

        return new BusinessException(message);
    }

}
