package com.oimc.aimin.auth.pojo;


import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 微信官方文档起的就这破名字，骂微信文档的第一回
 */
@Data
public class Jscode2sessionResult implements Serializable {

    @JSONField(name = "session_key")
    private String sessionKey;
    private String unionid;
    private String errmsg;
    private String openid;
    private int errcode;
}
