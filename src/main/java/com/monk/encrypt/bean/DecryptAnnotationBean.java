package com.monk.encrypt.bean;

import com.monk.encrypt.enums.DecryptMethod;
import com.monk.encrypt.enums.SHAEncryptType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p> 解密注解信息 </p>
 *
 * @author: zhouzhixiang
 * @date: 2020-01-15
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DecryptAnnotationBean {

    private DecryptMethod decryptMethod;

    private String key;

    private SHAEncryptType shaEncryptType;
}
