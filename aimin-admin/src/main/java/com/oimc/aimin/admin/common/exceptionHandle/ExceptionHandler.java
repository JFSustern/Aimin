package com.oimc.aimin.admin.common.exceptionHandle;


import com.oimc.aimin.base.exception.BusinessException;
import com.oimc.aimin.base.resp.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(BusinessException.class)
    public Result<?> handleServiceException(BusinessException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址：'{}',错误信息'{}'", requestURI,e.getMessage());
        return Result.error(e.getMessage());
    }

}
