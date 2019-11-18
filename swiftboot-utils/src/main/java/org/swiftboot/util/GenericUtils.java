package org.swiftboot.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * TODO: recursively
 */
public class GenericUtils {


    /**
     * 获取一个类的父类（及其父类）的泛型类型
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Class genericClass(Class<T> clazz) {
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass == null) {
            throw new RuntimeException(Info.get(GenericUtils.class, "no_super_class"));
        }
        if (!(genericSuperclass instanceof ParameterizedType)) {
            // 如果存在继承，则向上取一级
            genericSuperclass = ((Class) genericSuperclass).getGenericSuperclass();
            if (genericSuperclass == null) {
                throw new RuntimeException(
                        Info.get(GenericUtils.class,"no_generic_class_to_parent1", clazz.getGenericSuperclass().getTypeName()));
            }
            if (!(genericSuperclass instanceof ParameterizedType)) {
                throw new RuntimeException(
                        Info.get(GenericUtils.class,"no_generic_class_to_parent1", clazz.getGenericSuperclass().getTypeName()));
            }
        }

        Class<T> genericClass = (Class<T>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        return genericClass;
    }
}
