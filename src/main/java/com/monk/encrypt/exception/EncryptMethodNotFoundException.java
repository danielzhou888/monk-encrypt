package com.monk.encrypt.exception;

/**
 * <p>加密方式未找对或未定义异常</p>
 *
 * @author: zhouzhixiang
 * @date: 2020-01-15
 **/
public class EncryptMethodNotFoundException extends RuntimeException {

    public EncryptMethodNotFoundException() {
        super("Encrypt method is not found or defined!");
    }

    public EncryptMethodNotFoundException(String message) {
        super(message);
    }
}
