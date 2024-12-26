package com.oimc.aimin.common.core.exception;

import lombok.Data;

public class BaseException extends RuntimeException{

    private int code;
    private String msg;

}
