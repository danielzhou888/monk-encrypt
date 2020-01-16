package com.monk.encrypt.annotation;

import com.monk.encrypt.advice.DecryptRequestAdvice;
import com.monk.encrypt.advice.EncryptResponseAdvice;
import com.monk.encrypt.config.HttpConverterConfig;
import com.monk.encrypt.config.EncryptConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * <p> 启动类 </p>
 * <p> 使用方法：在SpringBoot的Application启动类上添加此注释即可</p>
 * @author: zhouzhixiang
 * @date: 2020-01-15
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({EncryptConfig.class,
        HttpConverterConfig.class,
        EncryptResponseAdvice.class,
        DecryptRequestAdvice.class})
public @interface EnableEncrypt {
}
