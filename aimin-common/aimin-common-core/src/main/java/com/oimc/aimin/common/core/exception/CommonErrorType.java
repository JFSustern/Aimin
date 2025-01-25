package com.oimc.aimin.common.core.exception;

import lombok.Getter;

@Getter
public enum CommonErrorType {
    OBJECT_NOT_NULL(1001, "对象不能为null"),
    SYNC_FAILURE(1002,"mysql同步数据到es失败");


    private final int code;
    private final String message;

    CommonErrorType(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
