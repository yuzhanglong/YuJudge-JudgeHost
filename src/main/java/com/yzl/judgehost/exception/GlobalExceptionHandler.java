package com.yzl.judgehost.exception;

import com.yzl.judgehost.core.UnifiedResponse;
import com.yzl.judgehost.exception.http.HttpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yuzhanglong
 * 实现全局异常处理
 * 统一返回值 errorcode method url 给前端
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private final ExceptionCodeConfiguration exceptionCodeConfiguration;

    public GlobalExceptionHandler(ExceptionCodeConfiguration exceptionCodeConfiguration) {
        this.exceptionCodeConfiguration = exceptionCodeConfiguration;
    }

    /**
     * @param request   请求参数
     * @param exception 抛出的异常
     * @author yzl
     * @description 拦截并处理程序内部发生的异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public UnifiedResponse handleException(HttpServletRequest request, Exception exception) {
        String requestUrl = request.getRequestURI();
        String method = request.getMethod();
        String requestString = getRequestUrlString(method, requestUrl);

        //TODO: 日志记录
        return new UnifiedResponse(1000, "SERVER_ERROR", requestString);
    }

    /**
     * @param request   请求参数
     * @param exception 抛出的异常
     * @author yzl
     * @description 拦截自定义的异常处理（例如数据不存在等）
     */

    @ExceptionHandler(HttpException.class)
    @ResponseBody
    public ResponseEntity<UnifiedResponse> handleHttpException(HttpServletRequest request, HttpException exception) {
        String requestUrl = request.getRequestURI();
        String method = request.getMethod();
        Integer errorCode = exception.getCode();
        String message = getMessageByExceptionCode(errorCode);

        // 初始化unifyresponse
        UnifiedResponse unifiedResponse = new UnifiedResponse(errorCode, message, getRequestUrlString(method, requestUrl));

        // statuscode 到 HttpStatus
        HttpStatus httpStatus = HttpStatus.resolve(exception.getHttpStatusCode());
        assert httpStatus != null;
        return new ResponseEntity<>(unifiedResponse, null, httpStatus);
    }


    private String getRequestUrlString(String method, String url) {
        return method + " " + url;
    }

    private String getMessageByExceptionCode(Integer code) {
        return exceptionCodeConfiguration.getMessage(code);
    }
}
