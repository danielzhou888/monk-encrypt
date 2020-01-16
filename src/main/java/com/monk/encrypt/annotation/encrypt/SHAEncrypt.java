package com.monk.encrypt.annotation.encrypt;

import com.monk.encrypt.enums.SHAEncryptType;

import java.lang.annotation.*;

/**
 * <p> SHA加密 </p>
 *
 * @author: zhouzhixiang
 * @date: 2020-01-15
 **/
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface SHAEncrypt {

    SHAEncryptType value() default SHAEncryptType.SHA256;
}
