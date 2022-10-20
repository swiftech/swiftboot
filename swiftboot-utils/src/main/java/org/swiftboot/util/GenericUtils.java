package org.swiftboot.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Generic type utils
 *
 * @author swiftech
 */
public class GenericUtils {

    /**
     * Get generic type of one class's all ancestor classes.
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Class<T> ancestorGenericClass(Class<T> clazz) {
        ParameterizedType genericSuperclass = firstParameterizedType(clazz);
        Class<T> genericClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
        return genericClass;
    }

    /**
     * Get generic type of one class's direct parent class.
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Class<T> parentGenericClass(Class<T> clazz) {
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass == null) {
            throw new RuntimeException(Info.get(GenericUtils.class, R.NO_GENERIC_SUPER_CLASS));
        }
        if (genericSuperclass instanceof ParameterizedType) {
            Class<T> genericClass = (Class<T>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
            return genericClass;
        }
        else {
            throw new RuntimeException(
                    Info.get(GenericUtils.class, R.NO_GENERIC_SUPER_CLASS, clazz.getGenericSuperclass().getTypeName()));
        }
    }


    /**
     * 通过继承关系向上查找第一个 ParameterizedType
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> ParameterizedType firstParameterizedType(Class<T> clazz) {
        if (clazz == null) {
            return null;
        }
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass == null) {
            throw new RuntimeException(Info.get(GenericUtils.class, R.NO_GENERIC_CLASS_TO_ANCESTOR1, clazz.getName()));
        }
        if (genericSuperclass instanceof ParameterizedType) {
            return (ParameterizedType) genericSuperclass;
        }
        Class clazz2 = (Class) genericSuperclass;
        return firstParameterizedType(clazz2);
    }

    /**
     * 通过继承关系向上查找第一个类型为 interfaceClass 接口的 ParameterizedType
     *
     * @param clazz
     * @param interfaceClass
     * @param <T>
     * @return
     */
    public static <T> ParameterizedType firstParameterizedType(Class<T> clazz, Class<?> interfaceClass) {
        if (clazz == null) {
            return null;
        }
        Type[] interfaces = clazz.getGenericInterfaces();
        if (interfaces.length == 0) {
            Type genericSuperclass = clazz.getGenericSuperclass();
            System.out.println(genericSuperclass);
            return firstParameterizedType((Class<T>) genericSuperclass, interfaceClass);
        }
        for (Type anInterface : interfaces) {
            if (anInterface instanceof ParameterizedType) {
                return (ParameterizedType) anInterface;
            }
            else if (anInterface == interfaceClass) {
                return firstParameterizedType((Class<T>) anInterface, interfaceClass);
            }
        }
        Type genericSuperclass = clazz.getGenericSuperclass();
        return firstParameterizedType((Class<T>) genericSuperclass, interfaceClass);
    }


    /**
     * 获取一个类的父类（及其父类）的泛型类型
     *
     * @param clazz
     * @param <T>
     * @return
     * @deprecated ancestorGenericClass()
     */
    public static <T> Class genericClass(Class<T> clazz) {
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass == null) {
            throw new RuntimeException(Info.get(GenericUtils.class, R.NO_GENERIC_SUPER_CLASS));
        }
        if (!(genericSuperclass instanceof ParameterizedType)) {
            // 如果存在继承，则向上取一级
            genericSuperclass = ((Class) genericSuperclass).getGenericSuperclass();
            if (genericSuperclass == null) {
                throw new RuntimeException(
                        Info.get(GenericUtils.class, R.NO_GENERIC_CLASS_TO_PARENT1, clazz.getGenericSuperclass().getTypeName()));
            }
            if (!(genericSuperclass instanceof ParameterizedType)) {
                throw new RuntimeException(
                        Info.get(GenericUtils.class, R.NO_GENERIC_CLASS_TO_PARENT1, clazz.getGenericSuperclass().getTypeName()));
            }
        }
        Class<T> genericClass = (Class<T>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        return genericClass;
    }
}
