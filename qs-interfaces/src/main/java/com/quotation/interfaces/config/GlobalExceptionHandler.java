package com.quotation.interfaces.config;

import com.quotation.interfaces.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public R<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("业务异常：{}", e.getMessage());
        return R.fail(400, e.getMessage());
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e) {
        log.error("系统异常", e);
        return R.fail("系统繁忙，请稍后重试");
    }
}
