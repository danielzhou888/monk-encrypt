package com.monk.encrypt.bean;

import com.monk.encrypt.enums.EncryptMethod;
import com.monk.encrypt.enums.SHAEncryptType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p> 加密注解信息 </p>
 *
 * @author: zhouzhixiang
 * @date: 2020-01-15
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EncryptAnnotationBean {

    private EncryptMethod encryptMethod;

    private String key;

    private SHAEncryptType shaEncryptType;
}
