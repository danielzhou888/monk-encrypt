package com.monk.encrypt.util;

import com.monk.encrypt.exception.KeyNotConfiguredException;

/**
 * <p>加密密钥校验获取辅助类</p>
 *
 * @author: zhouzhixiang
 * @date: 2020-01-15
 **/
public class CheckUtils {

    public static String checkAndGetKey(String configKey, String otherKey, String keyName) {
        if (StringUtils.isNullOrEmpty(configKey) && StringUtils.isNullOrEmpty(otherKey)) {
            throw new KeyNotConfiguredException();
        }
        if (StringUtils.isNullOrEmpty(configKey)) return otherKey;
        return configKey;
    }
}
