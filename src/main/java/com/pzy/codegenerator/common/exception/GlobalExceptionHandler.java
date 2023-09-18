package com.pzy.codegenerator.common.exception;

import com.pzy.codegenerator.common.enums.ResultCode;
import com.pzy.codegenerator.common.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 数据接口异常处理类
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        log.error("出现异常", e);
        return new Result<>(ResultCode.FAIL, e.getMessage());
    }
}
