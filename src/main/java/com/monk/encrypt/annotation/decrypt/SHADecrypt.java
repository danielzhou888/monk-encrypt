package com.monk.encrypt.annotation.decrypt;

import com.monk.encrypt.enums.SHAEncryptType;

import java.lang.annotation.*;

/**
 * <p> SHA解密 </p>
 *
 * @author: zhouzhixiang
 * @date: 2020-01-15
 **/
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface SHADecrypt {

    SHAEncryptType value() default SHAEncryptType.SHA256;
}
