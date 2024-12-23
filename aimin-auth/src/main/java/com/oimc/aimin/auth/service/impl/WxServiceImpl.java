package com.oimc.aimin.auth.service.impl;


import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.oimc.aimin.auth.entity.UserAccount;
import com.oimc.aimin.auth.exception.BuildURLException;
import com.oimc.aimin.auth.mapper.UserAccountMapper;
import com.oimc.aimin.auth.service.UserAccountService;
import com.oimc.aimin.auth.service.WxService;
import com.oimc.aimin.auth.utils.ObjectConvertor;
import com.oimc.aimin.auth.wx.pojo.AccessTokenResult;
import com.oimc.aimin.auth.wx.pojo.Jscode2sessionResult;
import com.oimc.aimin.auth.wx.service.WxMiniprogramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WxServiceImpl implements WxService {

    private final WxMiniprogramService wxMiniprogramService;
    private final UserAccountService userAccountService;

    @Override
    public SaTokenInfo wxLogin(String code) {
        Jscode2sessionResult jscode2sessionResult = wxMiniprogramService.Code2Session(code);
        String openid = jscode2sessionResult.getOpenid();
        UserAccount one = userAccountService.getOne(new LambdaQueryWrapper<UserAccount>().eq(UserAccount::getOpenid, openid));
        if(one == null){
            UserAccount userAccount = UserAccount.builder()
                    .openid(openid)
                    .build();
            userAccountService.save(userAccount);
        }
        boolean login = StpUtil.isLogin();
        System.out.println("tokenValue:" + login);
        StpUtil.login(openid);
        return StpUtil.getTokenInfo();

    }

    @Override
    public Jscode2sessionResult Code2Session(String jsCode) {
        return null;
    }

    @Override
    public AccessTokenResult getAccessToken() throws BuildURLException {
        return null;
    }
}
