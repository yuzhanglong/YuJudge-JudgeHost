package com.yzl.judgehost.exception.http;

/**
 * @author yuzhanglong
 */
public class ForbiddenException extends HttpException{
    public ForbiddenException(Integer code){
        this.code = code;
        this.httpStatusCode = 403;
    }
}
