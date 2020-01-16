package com.monk.encrypt.exception;

/**
 * <p>解析失败异常</p>
 *
 * @author: zhouzhixiang
 * @date: 2020-01-15
 **/
public class DecryptFailException extends RuntimeException {

    public DecryptFailException() {
        super("Decrypt fail!");
    }

    public DecryptFailException(String message) {
        super(message);
    }
}
