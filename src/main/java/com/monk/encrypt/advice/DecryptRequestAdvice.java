package com.monk.encrypt.advice;

import com.monk.encrypt.annotation.decrypt.AESDecrypt;
import com.monk.encrypt.annotation.decrypt.DESDecrypt;
import com.monk.encrypt.annotation.decrypt.Decrypt;
import com.monk.encrypt.annotation.decrypt.RSADecrypt;
import com.monk.encrypt.annotation.encrypt.DESEncrypt;
import com.monk.encrypt.bean.DecryptAnnotationBean;
import com.monk.encrypt.bean.DecryptHttpMessage;
import com.monk.encrypt.config.EncryptConfig;
import com.monk.encrypt.enums.DecryptMethod;
import com.monk.encrypt.exception.DecryptFailException;
import com.monk.encrypt.exception.DecryptMethodNotFoundException;
import com.monk.encrypt.util.AESEncryptUtil;
import com.monk.encrypt.util.CheckUtils;
import com.monk.encrypt.util.DESEncryptUtil;
import com.monk.encrypt.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import javax.security.auth.DestroyFailedException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * <p>请求数据的加密信息解密处理器</p>
 *
 * @author: zhouzhixiang
 * @date: 2020-01-15
 **/
@Order(1)
@ControllerAdvice
@Slf4j
public class DecryptRequestAdvice implements RequestBodyAdvice {

    @Autowired
    private EncryptConfig encryptConfig;

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        Annotation[] annotations = methodParameter.getDeclaringClass().getAnnotations();
        if (annotations != null && annotations.length > 0) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof Decrypt ||
                    annotation instanceof AESDecrypt ||
                    annotation instanceof DESDecrypt ||
                    annotation instanceof RSADecrypt) {
                    return true;
                }
            }
        }
        return methodParameter.getMethod().isAnnotationPresent(Decrypt.class) ||
                methodParameter.getMethod().isAnnotationPresent(AESDecrypt.class) ||
                methodParameter.getMethod().isAnnotationPresent(DESDecrypt.class) ||
                methodParameter.getMethod().isAnnotationPresent(RSADecrypt.class);
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        if (httpInputMessage.getBody() == null)
            return httpInputMessage;
        String body;
        try {
            body = IOUtils.toString(httpInputMessage.getBody(), encryptConfig.getEncoding());
        } catch (Exception e) {
            throw new DecryptFailException("Unable to get request body data, please check if" +
                    "the sending data body or request method is in compliance with the sepecification.");
        }
        if (StringUtils.isNullOrEmpty(body)) {
            throw new DecryptFailException("The request body is NULL or empty string, so the decryption failed!");
        }
        String decryptBody = null;
        DecryptAnnotationBean classAnnotation = this.getClassAnnotation(methodParameter.getDeclaringClass());
        if (classAnnotation != null) {
            decryptBody = toDecrypt(body, classAnnotation);
        } else {
            DecryptAnnotationBean methodAnnotation = this.getMethodAnnotation(methodParameter);
            if (methodAnnotation != null)
                decryptBody = toDecrypt(body, methodAnnotation);
        }
        if (decryptBody == null)
            throw new DecryptFailException("Decryption error, " +
                    "please check if the selected source data is encrypted correctly.");
        try {
            InputStream inputStream = IOUtils.toInputStream(decryptBody, encryptConfig.getEncoding());
            return new DecryptHttpMessage(inputStream, httpInputMessage.getHeaders());
        } catch (Exception e) {
            throw new DecryptFailException("The  string converted to a stream format exception. " +
                    "Please check if the format such as encoding is correct.");
        }
    }

    private String toDecrypt(String body, DecryptAnnotationBean annotationBean) {
        DecryptMethod decryptMethod = annotationBean.getDecryptMethod();
        if (decryptMethod == null)
            throw new DecryptMethodNotFoundException();
        String key = annotationBean.getKey();
        if (decryptMethod == DecryptMethod.AES) {
            key = CheckUtils.checkAndGetKey(encryptConfig.getAesKey(), key, "AES-KEY");
            return AESEncryptUtil.decrypt(body, key);
        }
        if (decryptMethod == DecryptMethod.DES) {
            key = CheckUtils.checkAndGetKey(encryptConfig.getAesKey(), key, "DES-KEY");
            return DESEncryptUtil.decrypt(body, key);
        }
        throw new DecryptFailException();
    }

    private DecryptAnnotationBean getMethodAnnotation(MethodParameter methodParameter) {
        if (methodParameter.getMethod().isAnnotationPresent(Decrypt.class)) {
            Decrypt decrypt = methodParameter.getMethodAnnotation(Decrypt.class);
            return DecryptAnnotationBean.builder()
                    .decryptMethod(decrypt.value())
                    .key(decrypt.otherKey())
                    .build();
        }
        if (methodParameter.getMethod().isAnnotationPresent(AESDecrypt.class)) {
            return DecryptAnnotationBean.builder()
                    .decryptMethod(DecryptMethod.AES)
                    .key(methodParameter.getMethodAnnotation(AESDecrypt.class).otherKey())
                    .build();
        }
        if (methodParameter.getMethod().isAnnotationPresent(DESDecrypt.class)) {
            return DecryptAnnotationBean.builder()
                    .decryptMethod(DecryptMethod.DES)
                    .key(methodParameter.getMethodAnnotation(DESDecrypt.class).otherKey())
                    .build();
        }
        return null;
    }

    private DecryptAnnotationBean getClassAnnotation(Class<?> clazz) {
        Annotation[] annotations = clazz.getAnnotations();
        if (annotations != null && annotations.length > 0) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof Decrypt) {
                    Decrypt decrypt = (Decrypt) annotation;
                    return DecryptAnnotationBean.builder()
                            .decryptMethod(decrypt.value())
                            .key(decrypt.otherKey())
                            .build();
                }
                if (annotation instanceof AESDecrypt) {
                    AESDecrypt decrypt = (AESDecrypt) annotation;
                    return DecryptAnnotationBean.builder()
                            .decryptMethod(DecryptMethod.AES)
                            .key(decrypt.otherKey())
                            .build();
                }
                if (annotation instanceof DESDecrypt) {
                    DESDecrypt desDecrypt = (DESDecrypt) annotation;
                    return DecryptAnnotationBean.builder()
                            .decryptMethod(DecryptMethod.DES)
                            .key(desDecrypt.otherKey())
                            .build();
                }
                if (annotation instanceof RSADecrypt) {
                    RSADecrypt decrypt = (RSADecrypt) annotation;
                    return DecryptAnnotationBean.builder()
                            .decryptMethod(DecryptMethod.RSA)
                            .build();
                }
            }
        }
        return null;
    }

    @Override
    public Object afterBodyRead(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }

    @Override
    public Object handleEmptyBody(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }
}
