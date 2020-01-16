package com.monk.encrypt.exception;

/**
 * <p>加密数据失败</p>
 *
 * @author: zhouzhixiang
 * @date: 2020-01-15
 **/
public class EncryptFailException extends RuntimeException {

    public EncryptFailException() {
        super("Encrypt data failed!");
    }

    public EncryptFailException(String message) {
        super(message);
    }
}
