package com.yzl.judgehost.exception.http;

/**
 * 全局httpException，
 * 规定错误码和http的状态码
 *
 * @author yuzhanglong
 * @date 2020-6-30 12:55:39
 */
public class HttpException extends RuntimeException {
    protected String code;

    public String getCode() {
        return code;
    }

    public Integer getHttpStatusCode() {
        return httpStatusCode;
    }

    protected Integer httpStatusCode;
}
