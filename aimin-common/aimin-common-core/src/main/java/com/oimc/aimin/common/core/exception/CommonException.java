package com.oimc.aimin.common.core.exception;

import java.io.Serial;

public class CommonException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    private CommonErrorType commonErrorType;

    public CommonException(CommonErrorType commonErrorType) {}

}
