package com.oimc.aimin.auth.service;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.nacos.shaded.com.google.common.base.Joiner;
import com.oimc.aimin.auth.config.wx.WXProperties;
import com.oimc.aimin.auth.pojo.GetAccessTokenResult;
import com.oimc.aimin.auth.pojo.Jscode2sessionResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WxService {

    private final RestTemplate restTemplate;
    private final WXProperties wxProperties;


    public static final String DO_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";
    public static final String CHECK_SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session";
    public static final String GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";

    /**
     * https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-login/code2Session.html
     * @param jsCode
     * @return
     */
    public Jscode2sessionResult Code2Session(String jsCode) {
        Map<String, String> params = new HashMap<>(8);
        params.put("appid", wxProperties.getAppid());
        params.put("secret", wxProperties.getSecret());
        params.put("js_code", jsCode);
        params.put("grant_type", "authorization_code");
        String paramsStr = Joiner.on("&").withKeyValueSeparator("=").join(params);
        String url = DO_LOGIN_URL + "?" + paramsStr;
        String response = restTemplate.getForObject(url, String.class);
        return JSONObject.parseObject(response, Jscode2sessionResult.class);
    }

    public GetAccessTokenResult getAccessToken() {
        Map<String, String> params = new HashMap<>(8);
        params.put("appid", wxProperties.getAppid());
        params.put("secret", wxProperties.getSecret());
        params.put("grant_type", "authorization_code");
        String paramsStr = Joiner.on("&").withKeyValueSeparator("=").join(params);
        String url = GET_ACCESS_TOKEN_URL + "?" + paramsStr;
        String response = restTemplate.getForObject(url, String.class);
        return JSONObject.parseObject(response, GetAccessTokenResult.class);
    }


}
