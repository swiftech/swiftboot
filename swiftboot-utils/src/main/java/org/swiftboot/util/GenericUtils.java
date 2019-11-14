package org.swiftboot.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Beta
 */
public class GenericUtils {


    /**
     * 获取一个类的泛型类型
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Class genericClass(Class<T> clazz) {
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass == null) {
            throw new RuntimeException("反射错误");
        }
        if (!(genericSuperclass instanceof ParameterizedType)) {
            // 如果存在继承，则向上取一级
            genericSuperclass = ((Class) genericSuperclass).getGenericSuperclass();
            if (genericSuperclass == null) {
                throw new RuntimeException("反射错误");
            }
            if (!(genericSuperclass instanceof ParameterizedType)) {
                throw new RuntimeException(
                        String.format("父类%s及其父类都没有指定泛型类型", clazz.getGenericSuperclass().getTypeName()));
            }
        }

        Class<T> genericClass = (Class<T>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        return genericClass;
    }
}
