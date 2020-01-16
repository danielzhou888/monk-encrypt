package com.monk.encrypt.exception;

/**
 * <p>解密方法未找到或未定义异常</p>
 *
 * @author: zhouzhixiang
 * @date: 2020-01-15
 **/
public class DecryptMethodNotFoundException extends RuntimeException {

    public DecryptMethodNotFoundException() {
        super("Decrption method not found or not defined !");
    }

    public DecryptMethodNotFoundException(String message) {
        super(message);
    }
}
