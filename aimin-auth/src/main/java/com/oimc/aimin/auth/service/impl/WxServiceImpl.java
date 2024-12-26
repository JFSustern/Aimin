package com.oimc.aimin.auth.service.impl;


import cn.dev33.satoken.stp.SaLoginModel;
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

    private final static String DEVICE_MINIPROGRAM = "miniprogram";
    /**
     * 微信小程序登录接口。
     * 该方法实现通过微信小程序提供的 `code` 参数完成用户登录操作，具体流程如下：
     * 1. 调用 `wxMiniprogramService.Code2Session(code)` 方法，通过微信小程序提供的 `code` 换取用户的 `openid`。
     * 2. 使用用户的 `openid` 查询用户账户信息，判断当前用户是否已经存在于系统中：
     *    - 如果用户不存在（查询结果为 `null`），则创建一个新的用户账户，并保存到数据库中。
     * 3. 判断当前用户是否已登录：
     *    - 如果用户未登录（`StpUtil.isLogin()` 返回 `false`），则使用 `openid` 调用 `StpUtil.login(openid)` 方法进行登录。
     * 4. 最后，返回用户的登录 Token 信息，通过 `StpUtil.getTokenInfo()` 获取。
     *
     * @param code 微信小程序端传入的登录凭证 `code`，用于获取用户的会话信息。
     * @return 用户的登录 Token 信息，包含登录状态、Token 值、有效期等。
     */
    public SaTokenInfo wxLogin(String code) {
        // 使用 code 换取微信用户的 openid
        Jscode2sessionResult jscode2sessionResult = wxMiniprogramService.Code2Session(code);
        String openid = jscode2sessionResult.getOpenid();

        // 根据 openid 查询用户账户是否存在
        UserAccount one = userAccountService.getOne(new LambdaQueryWrapper<UserAccount>().eq(UserAccount::getOpenid, openid));

        // 如果用户账户不存在，则创建新账户并保存到数据库
        if(one == null){
            UserAccount userAccount = new UserAccount();
            userAccount.setOpenid(openid);
            userAccountService.save(userAccount);
        }

        // 如果用户未登录，则使用 openid 进行登录
        if(!StpUtil.isLogin()){
            SaLoginModel saLoginModel = new SaLoginModel();
            saLoginModel.setDevice(DEVICE_MINIPROGRAM);
            saLoginModel.setIsWriteHeader(false);
            StpUtil.login(openid,saLoginModel);
        }

        // 返回当前用户的 Token 信息
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
