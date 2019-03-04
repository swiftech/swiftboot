package org.swiftboot.web.validate;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;
import org.swiftboot.web.exception.ErrorCodeSupport;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * 表单提交输入验证返回值，出现表单验证错误时创建并填充　HttpResponse　对象。
 * 可以设置多个输入框对应错误消息（或者错误消息资源代码）
 *
 * @see org.swiftboot.web.result.HttpResponse
 * @author swiftech
 */
public class ValidationResult extends ArrayList<ValidationResult.InputError> {

    /**
     * 从校验结果生成可返回客户端的对象
     *
     * @param bean      校验的参数对象
     * @param bindingResult 校验结果对象
     * @return
     */
    public static ValidationResult fromBindingResult(Object bean, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ValidationResult validationResult = new ValidationResult();

            for (ObjectError objectError : bindingResult.getAllErrors()) {
                String k = getJsonPropertyKeyFromError(bean, objectError.getCodes());
                validationResult.addErrorMsg(k, objectError.getDefaultMessage());
            }
            return validationResult;
        }
        return null;
    }

    public static String getJsonPropertyKeyFromError(Object targetBean, String[] error) {
        if (error.length < 2) {
            return null;
        }
        String s = error[1];
        return getJsonPropertyValue(targetBean, s.substring(StringUtils.indexOf(s, '.') + 1));
    }

    /**
     * 直接添加一个输入框对应的错误消息
     *
     * @param key 输入框key值
     * @param msg 错误消息
     */
    public void addErrorMsg(String key, String msg) {
        this.add(new InputError(key, msg));
    }

    /**
     * 添加一个输入框对应的错误消息代码
     *
     * @param key
     * @param errCode
     */
    public void addErrorCode(String key, String errCode) {
        addErrorMsg(key, ErrorCodeSupport.getErrorMessage(errCode));
    }

    /**
     * 添加一个错误消息对象
     *
     * @param error
     */
    public void addError(InputError error) {
        this.add(error);
    }

    /**
     * 是否有错误结果
     *
     * @return
     */
    public boolean hasErrors() {
        return !this.isEmpty();
    }


    /**
     * 输入参数错误类
     */
    public static class InputError {
        /**
         * 输入　key　值
         */
        @ApiModelProperty(value = "输入 key 值", example = "content")
        String key;

        /**
         * 错误信息
         */
        @ApiModelProperty(value = "错误信息", example = "长度需要在0和64之间")
        String msg;

        public InputError(String key, String msg) {
            this.key = key;
            this.msg = msg;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    /**
     * 获取 bean 中指定名称 JsonProperty 注解的值
     *
     * @param targetBean
     * @param fieldName
     * @return
     */
    public static String getJsonPropertyValue(Object targetBean, String fieldName) {
        try {
            Field declaredField = getDeclaredField(targetBean, fieldName);
            JsonProperty fieldAnnotation = (JsonProperty) getFieldAnnotation(declaredField, JsonProperty.class);
            if (fieldAnnotation == null) {
                return fieldName;
            }
            else {
                return fieldAnnotation.value();
            }
        } catch (NoSuchFieldException e) {
            // 此处无需处理
        }
        return fieldName;
    }

    /**
     * 获取指定 Field 中的指定类型的注解实例
     *
     * @param field
     * @param annotationClazz
     * @return
     */
    public static Annotation getFieldAnnotation(Field field, Class annotationClazz) {
        Annotation[] annotations = field.getDeclaredAnnotations();
        for (Annotation declaredAnnotation : annotations) {
            if (declaredAnnotation.annotationType() == annotationClazz) {
                return declaredAnnotation;
            }
        }
        return null;
    }


    /**
     * 循环向上转型，获取对象声明的字段。
     *
     * @param object       对象
     * @param propertyName 属性名称
     * @return 字段对象
     * @throws NoSuchFieldException 没有该字段时抛出
     */
    public static Field getDeclaredField(Object object, String propertyName) throws NoSuchFieldException {
        return getDeclaredField(object.getClass(), propertyName);
    }

    /**
     * 循环向上转型，获取对象声明的字段。
     *
     * @param clazz        类
     * @param propertyName 属性名称
     * @return 字段对象
     * @throws NoSuchFieldException 没有该字段时抛出
     */
    public static Field getDeclaredField(Class clazz, String propertyName) throws NoSuchFieldException {
        Assert.notNull(clazz, "");
        Assert.hasText(propertyName, "");
        for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(propertyName);
            } catch (NoSuchFieldException e) {
                // Field不在当前类定义，继续向上转型
            }
        }
        throw new NoSuchFieldException("No such field: " + clazz.getName() + '.' + propertyName);
    }
}
