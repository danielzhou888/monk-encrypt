package com.monk.encrypt.annotation.encrypt;

import java.lang.annotation.*;

/**
 * <p> MD5加密 </p>
 *
 * @author: zhouzhixiang
 * @date: 2020-01-15
 **/
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface MD5Encrypt {

}
