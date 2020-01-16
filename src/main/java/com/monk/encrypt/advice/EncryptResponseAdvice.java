package com.monk.encrypt.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monk.encrypt.annotation.encrypt.*;
import com.monk.encrypt.bean.EncryptAnnotationBean;
import com.monk.encrypt.config.EncryptConfig;
import com.monk.encrypt.enums.EncryptMethod;
import com.monk.encrypt.enums.SHAEncryptType;
import com.monk.encrypt.exception.EncryptFailException;
import com.monk.encrypt.exception.EncryptMethodNotFoundException;
import com.monk.encrypt.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import sun.security.provider.SHA;

import java.lang.annotation.Annotation;

/**
 * <p>响应数据加密处理逻辑</p>
 *
 * @author: zhouzhixiang
 * @date: 2020-01-15
 **/
@Order(1)
@ControllerAdvice
@Slf4j
public class EncryptResponseAdvice implements ResponseBodyAdvice {

    private final ObjectMapper objectMapper;
    private final EncryptConfig encryptConfig;

    @Autowired
    public EncryptResponseAdvice(ObjectMapper objectMapper, EncryptConfig encryptConfig) {
        this.objectMapper = objectMapper;
        this.encryptConfig = encryptConfig;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        Annotation[] annotations = returnType.getDeclaringClass().getAnnotations();
        if (annotations != null && annotations.length > 0) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof Encrypt ||
                        annotation instanceof AESEncrypt ||
                        annotation instanceof DESEncrypt ||
                        annotation instanceof MD5Encrypt ||
                        annotation instanceof SHAEncrypt ||
                        annotation instanceof RSAEncrypt)
                    return true;
            }
        }
        return returnType.getMethod().isAnnotationPresent(Encrypt.class) ||
                returnType.getMethod().isAnnotationPresent(AESEncrypt.class) ||
                returnType.getMethod().isAnnotationPresent(DESEncrypt.class) ||
                returnType.getMethod().isAnnotationPresent(SHAEncrypt.class) ||
                returnType.getMethod().isAnnotationPresent(RSAEncrypt.class) ||
                returnType.getMethod().isAnnotationPresent(MD5Encrypt.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (body == null)
            return null;
        serverHttpResponse.getHeaders().setContentType(MediaType.TEXT_PLAIN);
        String res = null;
        try {
            res = objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        EncryptAnnotationBean classAnnotation = getClassAnnotation(methodParameter.getDeclaringClass());
        if (classAnnotation != null)
            return toEncrypt(res, classAnnotation);
        EncryptAnnotationBean methodAnnotation = getMethodAnnotation(methodParameter);
        if (mediaType != null)
            return toEncrypt(res, methodAnnotation);
        throw new EncryptFailException();
    }

    private String toEncrypt(String resBodyString, EncryptAnnotationBean annotationBean) {
        EncryptMethod encryptMethod = annotationBean.getEncryptMethod();
        if (encryptMethod == null)
            throw new EncryptMethodNotFoundException();
        if (encryptMethod == EncryptMethod.MD5) {
            return MD5EncryptUtil.encrypt(resBodyString);
        } else if (encryptMethod == EncryptMethod.SHA) {
            SHAEncryptType shaEncryptType = annotationBean.getShaEncryptType();
            if (shaEncryptType == null)
                shaEncryptType = SHAEncryptType.SHA256;
            return SHAEncryptUtil.encrypt(resBodyString, shaEncryptType);
        }
        String key = annotationBean.getKey();
        if (encryptMethod == EncryptMethod.AES) {
            key = CheckUtils.checkAndGetKey(encryptConfig.getAesKey(), key, "AES-KEY");
            return AESEncryptUtil.encrypt(resBodyString, key);
        } else if (encryptMethod == EncryptMethod.DES) {
            key = CheckUtils.checkAndGetKey(encryptConfig.getDesKey(), key, "DES-KEY");
            return DESEncryptUtil.encrypt(resBodyString, key);
        }
        throw new EncryptFailException();
    }

    private EncryptAnnotationBean getMethodAnnotation(MethodParameter methodParameter) {
        if (methodParameter.getMethod().isAnnotationPresent(Encrypt.class)) {
            Encrypt encrypt = methodParameter.getMethodAnnotation(Encrypt.class);
            return EncryptAnnotationBean.builder()
                    .encryptMethod(encrypt.value())
                    .key(encrypt.otherKey())
                    .shaEncryptType(encrypt.shaType())
                    .build();
        } else if (methodParameter.getMethod().isAnnotationPresent(AESEncrypt.class)) {
            AESEncrypt encrypt = methodParameter.getMethodAnnotation(AESEncrypt.class);
            return EncryptAnnotationBean.builder()
                    .encryptMethod(EncryptMethod.AES)
                    .key(encrypt.otherKey())
                    .build();
        } else if (methodParameter.getMethod().isAnnotationPresent(DESEncrypt.class)) {
            DESEncrypt encrypt = methodParameter.getMethodAnnotation(DESEncrypt.class);
            return EncryptAnnotationBean.builder()
                    .encryptMethod(EncryptMethod.DES)
                    .key(encrypt.otherKey())
                    .build();
        } else if (methodParameter.getMethod().isAnnotationPresent(MD5Encrypt.class)) {
            MD5Encrypt encrypt = methodParameter.getMethodAnnotation(MD5Encrypt.class);
            return EncryptAnnotationBean.builder()
                    .encryptMethod(EncryptMethod.MD5)
                    .build();
        } else if (methodParameter.getMethod().isAnnotationPresent(SHAEncrypt.class)) {
            SHAEncrypt encrypt = methodParameter.getMethodAnnotation(SHAEncrypt.class);
            return EncryptAnnotationBean.builder()
                    .encryptMethod(EncryptMethod.SHA)
                    .shaEncryptType(encrypt.value())
                    .build();
        }
        return null;
    }

    private EncryptAnnotationBean getClassAnnotation(Class<?> clazz) {
        Annotation[] annotations = clazz.getDeclaredAnnotations();
        if (annotations != null && annotations.length > 0) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof Encrypt) {
                    Encrypt encrypt = (Encrypt) annotation;
                    return EncryptAnnotationBean.builder()
                            .encryptMethod(encrypt.value())
                            .key(encrypt.otherKey())
                            .shaEncryptType(encrypt.shaType())
                            .build();
                } else if (annotation instanceof AESEncrypt) {
                    AESEncrypt encrypt = (AESEncrypt) annotation;
                    return EncryptAnnotationBean.builder()
                            .encryptMethod(EncryptMethod.AES)
                            .key(encrypt.otherKey())
                            .build();
                } else if (annotation instanceof DESEncrypt) {
                    DESEncrypt encrypt = (DESEncrypt) annotation;
                    return EncryptAnnotationBean.builder()
                            .encryptMethod(EncryptMethod.DES)
                            .key(encrypt.otherKey())
                            .build();
                } else if (annotation instanceof MD5Encrypt) {
                    MD5Encrypt encrypt = (MD5Encrypt) annotation;
                    return EncryptAnnotationBean.builder()
                            .encryptMethod(EncryptMethod.MD5)
                            .build();
                } else if (annotation instanceof SHAEncrypt) {
                    SHAEncrypt encrypt = (SHAEncrypt) annotation;
                    return EncryptAnnotationBean.builder()
                            .encryptMethod(EncryptMethod.SHA)
                            .shaEncryptType(encrypt.value())
                            .build();
                }
            }
        }
        return null;
    }
}
