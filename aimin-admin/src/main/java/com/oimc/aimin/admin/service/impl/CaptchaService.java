package com.oimc.aimin.admin.service.impl;

import com.aliyun.captcha20230305.Client;
import com.aliyun.captcha20230305.models.VerifyIntelligentCaptchaRequest;
import com.aliyun.captcha20230305.models.VerifyIntelligentCaptchaResponse;
import com.aliyun.captcha20230305.models.VerifyIntelligentCaptchaResponseBody;
import com.oimc.aimin.admin.config.aliyun.AliyunCaptchaProperties;
import com.aliyun.teaopenapi.models.Config;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CaptchaService {

    private final AliyunCaptchaProperties config;
    private Client client; // 通过@PostConstruct初始化

    @PostConstruct
    private void initClient() throws Exception {
        this.client = createClient(config);
    }

    public Boolean verify(String captchaVerifyParam) {
        VerifyIntelligentCaptchaRequest request = new VerifyIntelligentCaptchaRequest()
                .setSceneId(config.getSceneId())
                .setCaptchaVerifyParam(captchaVerifyParam);

        log.debug("Captcha verify start sceneId={}", config.getSceneId());
        VerifyIntelligentCaptchaResponse resp = null;
        try {
            resp = client.verifyIntelligentCaptcha(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return handleResponse(resp);

    }


    private Client createClient(AliyunCaptchaProperties config) throws Exception {
        Config sdkConfig = new Config()
                .setAccessKeyId(config.getAccessKeyId())
                .setAccessKeySecret(config.getAccessKeySecret())
                .setEndpoint(config.getEndpoint())
                .setConnectTimeout(config.getConnectTimeout())
                .setReadTimeout(config.getReadTimeout());
        return new Client(sdkConfig); // 建议通过依赖注入复用
    }

    // 专用响应处理方法
    private Boolean handleResponse(VerifyIntelligentCaptchaResponse resp) {
        if (resp == null || resp.getBody() == null) {
            log.warn("验证码响应体为空");
            return false;
        }
        VerifyIntelligentCaptchaResponseBody.VerifyIntelligentCaptchaResponseBodyResult result = resp.getBody().getResult();
        if (result != null) {
            return result.getVerifyResult();
        }
        return false;
    }
}
