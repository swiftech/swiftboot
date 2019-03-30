package org.swiftboot.web.validate;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.swiftboot.util.AnnotationUtils;
import org.swiftboot.util.BeanUtils;
import org.swiftboot.web.exception.ErrorCodeSupport;

import java.util.ArrayList;

/**
 * 表单提交输入验证返回值，出现表单验证错误时创建并填充　HttpResponse　对象。
 * 可以设置多个输入框对应错误消息（或者错误消息资源代码）
 *
 * @author swiftech
 * @see org.swiftboot.web.result.HttpResponse
 */
public class ValidationResult extends ArrayList<ValidationResult.InputError> {

    /**
     * 从校验结果生成可返回客户端的对象
     *
     * @param bean          校验的参数对象
     * @param bindingResult 校验结果对象
     * @return
     */
    public static ValidationResult readFromBindingResult(Object bean, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ValidationResult validationResult = new ValidationResult();
            for (ObjectError objectError : bindingResult.getAllErrors()) {
                String k = getJsonPropertyKeyFromError(bean, objectError.getCodes());
                FieldError fe = (FieldError) objectError;
                String fieldDesc;
                try {
                    ApiModelProperty apiModelAnno =
                            (ApiModelProperty) AnnotationUtils.getFieldAnnotation(BeanUtils.getDeclaredField(bean, fe.getField()), ApiModelProperty.class);
                    if (apiModelAnno == null || StringUtils.isBlank(apiModelAnno.value())) {
                        fieldDesc = k; // 不存在的话退回实用 key 值来标识
                    }
                    else {
                        fieldDesc = apiModelAnno.value();
                    }
                } catch (Exception e) {
                    fieldDesc = k;
                }
                validationResult.addErrorMsg(k, fieldDesc + objectError.getDefaultMessage());
            }
            return validationResult;
        }
        return null;
    }

    /**
     * 从校验结果生成可返回客户端的对象
     *
     * @param bean          校验的参数对象
     * @param bindingResult 校验结果对象
     * @return
     * @deprecated
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
        return AnnotationUtils.getJsonPropertyValue(targetBean, s.substring(StringUtils.indexOf(s, '.') + 1));
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


}
