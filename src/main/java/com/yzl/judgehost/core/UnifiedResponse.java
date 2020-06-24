package com.yzl.judgehost.core;

/**
 * @author yuzhanglong
 */
public class UnifiedResponse {
    private final int code;
    private final String message;
    private final String request;



    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getRequest() {
        return request;
    }

    public UnifiedResponse(int code, String message, String request) {
        this.code = code;
        this.message = message;
        this.request = request;
    }



}
