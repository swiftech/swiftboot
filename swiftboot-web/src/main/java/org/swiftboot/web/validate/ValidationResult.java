package org.swiftboot.web.validate;

import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.swiftboot.util.AnnotationUtils;
import org.swiftboot.util.BeanUtils;
import org.swiftboot.web.response.Response;
import org.swiftboot.web.util.JacksonUtils;
import org.swiftboot.web.util.MessageUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 表单提交输入验证返回值，出现表单验证错误时创建并填充　{@link Response}　对象。
 * 可以设置多个输入框对应错误消息（或者错误消息资源代码）
 *
 * @author swiftech
 * @see Response
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
                if (objectError == null) {
                    continue;
                }
                String k = getJsonPropertyKeyFromError(bean, objectError.getCodes());
                FieldError fe = (FieldError) objectError;
                String fieldDesc;
                try {
                    Schema apiModelAnno =
                            (Schema) AnnotationUtils.getFieldAnnotation(
                                    BeanUtils.getDeclaredField(bean, fe.getField()), Schema.class);
                    if (apiModelAnno == null || StringUtils.isBlank(apiModelAnno.description())) {
                        fieldDesc = k; // 不存在的话退回实用 key 值来标识
                    }
                    else {
                        fieldDesc = apiModelAnno.description();
                    }
                } catch (Exception e) {
                    fieldDesc = k;
                }

                String actualMsg;
                if (objectError.getArguments() != null && objectError.getArguments().length > 1) { // > 1 means there are message arguments.
                    // handle the user-defined parameterized message;
                    List<String> argumentList = Arrays.stream(objectError.getArguments()).map(String::valueOf).toList();
                    String[] arguments = argumentList.subList(1, argumentList.size()).toArray(new String[]{}); // first one is not argument.
                    actualMsg = MessageUtils.concatSentences(fieldDesc,
                            MessageUtils.instantiateMessage(objectError.getDefaultMessage(), arguments));
                }
                else {
                    actualMsg = MessageUtils.concatSentences(fieldDesc, objectError.getDefaultMessage());
                }
                validationResult.addErrorMsg(k, actualMsg);
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
        return JacksonUtils.getJsonPropertyValue(targetBean, s.substring(StringUtils.indexOf(s, '.') + 1));
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
        @Schema(description = "Key of Input", example = "content")
        String key;

        /**
         * 错误信息
         */
        @Schema(description = "Error Message", example = "length must be between 0 and 64")
        String msg;

        public InputError() {
        }

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
