package org.swiftboot.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * 通过反射处理 Java 注解的工具类
 *
 * @author swiftech
 */
public class AnnotationUtils {

    /**
     * 获取包含指定注解类型的方法的注解
     *
     * @param targetClass
     * @param annoClass
     * @param <T>         注解的类型
     * @return
     */
    public static <T extends Annotation> T getMethodsAnnotation(Object targetClass, Class<T> annoClass) {
        Method[] declaredMethods = targetClass.getClass().getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            T annotation = declaredMethod.getDeclaredAnnotation(annoClass);
            if (annotation != null) {
                return annotation;
            }
        }
        return null;
    }

    /**
     * 获取包含指定注解类型的方法列表
     *
     * @param targetClass
     * @param annoClass
     * @return
     */
    public static List<Method> getMethodsContainsAnnotation(Object targetClass, Class annoClass) {
        Method[] declaredMethods = targetClass.getClass().getDeclaredMethods();
        List<Method> ret = new ArrayList<>();
        for (Method declaredMethod : declaredMethods) {
            Annotation annotation = declaredMethod.getAnnotation(annoClass);
            if (annotation != null) {
                ret.add(declaredMethod);
            }
        }
        return ret;
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
     * 获取方法参数上的注解（类型限定）
     * @param method
     * @param annotationClass
     * @return
     */
    public static <T extends Annotation> List<T> getMethodParamAnnotations(Method method, Class<T> annotationClass) {
        List<T> ret = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (Parameter parameter : parameters) {
            for (Annotation annotation : parameter.getAnnotations()) {
                if (annotation.annotationType() == annotationClass) {
                    ret.add((T) annotation);
                }
            }
        }
        return ret;
    }
}
