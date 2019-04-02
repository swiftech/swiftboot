package org.swiftboot.web.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.swiftboot.util.AnnotationUtils;
import org.swiftboot.util.BeanUtils;

import java.lang.reflect.Field;

/**
 * @author swiftech
 **/
public class JacksonUtils {

    /**
     * 获取 bean 中指定名称 JsonProperty 注解的值
     *
     * @param targetBean
     * @param fieldName
     * @return
     */
    public static String getJsonPropertyValue(Object targetBean, String fieldName) {
        try {
            Field declaredField = BeanUtils.getDeclaredField(targetBean, fieldName);
            JsonProperty fieldAnnotation = (JsonProperty) AnnotationUtils.getFieldAnnotation(declaredField, JsonProperty.class);
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
}
