package com.yzl.judgehost.core.factory;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;

import java.io.IOException;
import java.util.List;

/**
 * YAML配置文件读取工厂类
 *
 * @author yuzhanglong
 */
public class YamlPropertySourceFactory extends DefaultPropertySourceFactory {


    /**
     * YAML配置文件读取工厂类
     *
     * @param name     配置文件源名称
     * @param resource 配置文件源,如果没有给予name参数，则name会从这里得到
     * @author yuzhanglong
     */
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        String sourceName = resource.getResource().getFilename();
        Resource s = resource.getResource();
        List<PropertySource<?>> sources = new YamlPropertySourceLoader().load(sourceName, s);
        return sources.get(0);
    }
}
