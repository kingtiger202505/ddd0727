package com.quotation.interfaces.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * Web MVC 配置（映射本地上传图片静态路径）
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        File dir = new File(uploadDir);
        String path = dir.getAbsolutePath();
        if (!path.endsWith(File.separator)) {
            path += File.separator;
        }
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + path);
    }
}
