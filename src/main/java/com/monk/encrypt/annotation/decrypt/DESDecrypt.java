package com.monk.encrypt.annotation.decrypt;

import java.lang.annotation.*;

/**
 * <p> DES解密 </p>
 *
 * @author: zhouzhixiang
 * @date: 2020-01-15
 **/
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface DESDecrypt {

    String otherKey() default "";
}
