package com.monk.encrypt.annotation.decrypt;

import com.monk.encrypt.enums.DecryptMethod;
import com.monk.encrypt.enums.SHAEncryptType;

import java.lang.annotation.*;

/**
 * <p> 解密RequestBody </p>
 *
 * @author: zhouzhixiang
 * @date: 2020-01-15
 **/
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface Decrypt {

    DecryptMethod value() default DecryptMethod.MD5;

    String otherKey() default "";

    SHAEncryptType shaType() default SHAEncryptType.SHA256;
}
