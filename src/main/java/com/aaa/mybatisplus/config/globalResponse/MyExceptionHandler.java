package com.aaa.mybatisplus.config.globalResponse;

import com.aaa.mybatisplus.config.globalResponse.exceptions.DemoException;
import com.aaa.mybatisplus.config.globalResponse.exceptions.LimitException;
import com.aaa.mybatisplus.config.httpResult.HttpResult;
import com.aaa.mybatisplus.enums.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义异常处理器
 * @author liuzhen.tian
 * @version 1.0 MyExceptionHandler.java  2020/9/14 10:36
 */

@Slf4j
@ControllerAdvice
public class MyExceptionHandler {

    @Autowired
    HttpServletRequest httpServletRequest;

    /**
     * 异常日志记录
     */

    private void logErrorRequest(Exception e) {
        log.error("报错API URL:{}", httpServletRequest.getRequestURL().toString());
        log.error("异常:{}", e.getMessage());
    }


    /**
     * RuntimeException
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody
    HttpResult RuntimeExceptionHandler() {
        return HttpResult.fail(ResultCode.INVALID_PARAM);
    }

    /**
     * 参数未通过@Valid验证异常，
     */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    private HttpResult methodArgumentNotValid(MethodArgumentNotValidException exception) {
        logErrorRequest(exception);
        return HttpResult.fail(ResultCode.INVALID_PARAM);
    }


    /**
     * 参数格式有误
     */

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    @ResponseBody
    private HttpResult typeMismatch(Exception exception) {
        logErrorRequest(exception);
        return HttpResult.fail(ResultCode.MISTYPE_PARAM);
    }


    /**
     * 缺少参数
     */

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    private HttpResult missingServletRequestParameter(MissingServletRequestParameterException exception) {
        logErrorRequest(exception);
        return HttpResult.fail(ResultCode.MISSING_PARAM);
    }


    /**
     * 不支持的请求类型
     */

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    private HttpResult httpRequestMethodNotSupported(HttpRequestMethodNotSupportedException exception) {
        logErrorRequest(exception);
        return HttpResult.fail(ResultCode.UNSUPPORTED_METHOD);
    }


    /**
     * 业务层异常
     */
    @ExceptionHandler(DemoException.class)
    @ResponseBody
    private HttpResult serviceExceptionHandler(DemoException exception) {
        logErrorRequest(exception);
        return HttpResult.fail(exception.getErrorCode(), exception.getErrorMsg());
    }

    /**
     * 限流异常
     */
    @ExceptionHandler(LimitException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody
    Map limitExceptionHandler() {
        Map<String, Object> result = new HashMap();
        result.put("code", "500");
        result.put("msg", "请求次数已经到设置限流次数！");
        return result;
    }
}