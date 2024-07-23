package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration Class: used for AliOssUtil object creation
 */
@Configuration
@Slf4j
public class AliOssConfiguration {

    @Bean // 如何把第三方交给IOC容器管理, 如果是自定义的东西, 直接Component Autowired就可以了
    @ConditionalOnMissingBean // only create when aliossproperties bean type is not exists
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties) { // AliOssProperties已经取了yml的settngs, 并且他是IOC容器管理的bean, 所以我们直接依赖注入
        log.info("start create aliossutil object: {}", aliOssProperties);
        return new AliOssUtil(aliOssProperties.getEndpoint(), aliOssProperties.getAccessKeyId(), aliOssProperties.getAccessKeySecret(), aliOssProperties.getBucketName());
    }
}
