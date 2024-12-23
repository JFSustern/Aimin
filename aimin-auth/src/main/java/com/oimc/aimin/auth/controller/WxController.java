package com.oimc.aimin.auth.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.util.SaResult;
import com.oimc.aimin.auth.controller.vo.MiniProgramLoginVO;
import com.oimc.aimin.auth.exception.BuildURLException;
import com.oimc.aimin.auth.utils.ObjectConvertor;
import com.oimc.aimin.auth.wx.pojo.Jscode2sessionResult;
import com.oimc.aimin.auth.service.WxService;
import com.oimc.aimin.common.core.pojo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wx")
@RequiredArgsConstructor
public class WxController {

    private final WxService wxService;
    private final ObjectConvertor objectConvertor;

    @GetMapping("/token")
    public Result<?> token(String code) throws BuildURLException {
        SaTokenInfo saTokenInfo = wxService.wxLogin(code);
        MiniProgramLoginVO vo = objectConvertor.toMiniProgramLoginVO(saTokenInfo);
        return Result.success(vo);
    }


}
