package com.example.demo.exception;

/**
 * @Author: kuaik
 * @Date: 2018-09-20
 * @Description:
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String msg) {
        super(msg);
    }

    public BusinessException(Throwable exception) {
        super(exception);
    }

    public BusinessException(String msg, Exception exception) {
        super(msg, exception);
    }
}
