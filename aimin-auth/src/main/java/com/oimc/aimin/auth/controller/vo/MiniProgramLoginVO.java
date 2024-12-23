package com.oimc.aimin.auth.controller.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class MiniProgramLoginVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String tokenName;
    private String tokenValue;
}
