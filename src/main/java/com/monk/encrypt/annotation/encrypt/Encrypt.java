package com.monk.encrypt.annotation.encrypt;

import com.monk.encrypt.enums.EncryptMethod;
import com.monk.encrypt.enums.SHAEncryptType;

import java.lang.annotation.*;

/**
 * <p> 加密{@link org.springframework.web.bind.annotation.ResponseBody}响应数据，可用于整个控制类或者某个控制器上 </p>
 *
 * @author: zhouzhixiang
 * @date: 2020-01-15
 **/
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Encrypt {

    EncryptMethod value() default EncryptMethod.MD5;

    String otherKey() default "";

    SHAEncryptType shaType() default SHAEncryptType.SHA256;
}
