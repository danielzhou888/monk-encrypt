package com.monk.encrypt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p> 加密数据配置读取类 </p>
 *
 * @author: zhouzhixiang
 * @date: 2020-01-15
 **/
@ConfigurationProperties(prefix = "encrypt")
@Configuration
@Data
public class EncryptConfig {

    private String aesKey;

    private String desKey;

    private String encoding = "UTF-8";
}
