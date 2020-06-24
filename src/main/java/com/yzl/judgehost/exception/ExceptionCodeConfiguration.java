package com.yzl.judgehost.exception;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yuzhanglong
 * 从配置文件中获取错误码对应的描述
 * 这个描述会被返回到前端
 */
@ConfigurationProperties(prefix = "judgehost-exceptions")
@PropertySource(value = "classpath:config/exceptionCodes.yml")
@Component
public class ExceptionCodeConfiguration {
    public void setCodes(Map<Integer, String> codes) {
        this.codes = codes;
    }

    private Map<Integer,String> codes = new HashMap<>();

    public Map<Integer, String> getCodes(){
        return codes;
    }
    public String getMessage(Integer code) {
        return codes.get(code);
    }


}
