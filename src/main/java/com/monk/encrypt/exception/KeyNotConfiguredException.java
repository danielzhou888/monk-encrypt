package com.monk.encrypt.exception;

/**
 * <p>密钥未配置异常</p>
 *
 * @author: zhouzhixiang
 * @date: 2020-01-15
 **/
public class KeyNotConfiguredException extends RuntimeException {

    public KeyNotConfiguredException() {
        super("Encrypt key not be configured!");
    }

    public KeyNotConfiguredException(String message) {
        super(message);
    }
}
