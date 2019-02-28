package org.swiftboot.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 使用java反射机制操作对象
 * 提供：
 * 1. 获取类及其父类定义的 Field
 * 2. 忽略 Field 的修饰符获取它的值
 * 3. 忽略修饰符调用私有方法
 * 4. 根据 field 类型获取 Field 定义
 * 5. 获取静态 Field
 *
 * @author Wang Yuxing
 */
public class BeanUtils {
    protected static final Logger logger = LoggerFactory.getLogger(BeanUtils.class);
//	static {
//		logger.setAdditivity(false);
//	}

    // 限制实例化
    private BeanUtils() {
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
        for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(propertyName);
            } catch (NoSuchFieldException e) {
                // Field不在当前类定义，继续向上转型
            }
        }
        throw new NoSuchFieldException("No such field: " + clazz.getName() + '.' + propertyName);
    }

    /**
     * 循环向上转型，获取对象声明的字段。
     *
     * @param clazz 类
     * @return 字段对象集合
     */
    public static Collection<Field> getAllFields(Class clazz) {

        Set<Field> ret = new HashSet<>();
        for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            Collections.addAll(ret, superClass.getDeclaredFields());
        }
        return ret;
    }

    /**
     * 循环向上转型，获取对象声明的字段，忽略指定名称的字段。
     *
     * @param clazz   类
     * @param ignores 不包含的字段
     * @return 字段对象集合
     */
    public static Collection<Field> getFieldsIgnore(Class clazz, Collection<String> ignores) {

        Set<Field> ret = new HashSet<>();
        for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            for (Field declaredField : superClass.getDeclaredFields()) {
                if (!ignores.contains(declaredField.getName())) {
                    ret.add(declaredField);
                }
            }
        }
        return ret;
    }

    /**
     *
     * @param clazz
     * @param annoClass
     * @return
     */
    public static Collection<Field> getFieldsIgnore(Class clazz, Class annoClass) {

        Set<Field> ret = new HashSet<>();
        for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            for (Field declaredField : superClass.getDeclaredFields()) {
                Annotation annotation = declaredField.getAnnotation(annoClass);
                if (annotation == null) {
                    ret.add(declaredField);
                }
            }
        }
        return ret;
    }

    /**
     *
     * @param clazz
     * @param annoClass
     * @return
     */
    public static Collection<Field> getFieldsIgnore(Class clazz, Class... annoClass) {

        Set<Field> ret = new HashSet<>();
        for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            field: for (Field declaredField : superClass.getDeclaredFields()) {
                for (Class aClass : annoClass) {
                    Annotation annotation = declaredField.getAnnotation(aClass);
                    if (annotation != null) {
                        continue field;
                    }
                }
                ret.add(declaredField);
            }
        }
        return ret;
    }

    /**
     * 暴力获取对象变量值，忽略private、protected修饰符的限制。
     *
     * @param object       对象
     * @param propertyName 属性名称
     * @return 属性的值
     * @throws NoSuchFieldException 没有该字段时抛出
     */
    public static Object forceGetProperty(Object object, String propertyName) throws NoSuchFieldException {

        Field field = getDeclaredField(object, propertyName);

        boolean accessible = field.isAccessible();
        field.setAccessible(true);

        Object result = null;
        try {
            result = field.get(object);
        } catch (IllegalAccessException e) {
            // logger.info("Can't get {}.{} value", object.getClass().getName(), propertyName);
            logger.info("Can't get " + object.getClass().getName() + "." + propertyName + " value");
        }
        field.setAccessible(accessible);
        return result;
    }

    /**
     * 暴力设置对象变量值，忽略private、protected修饰符的限制。
     *
     * @param object       对象
     * @param propertyName 属性名称
     * @param newValue     属性值
     * @throws NoSuchFieldException 没有该字段时抛出
     */
    public static void forceSetProperty(Object object, String propertyName, Object newValue)
            throws NoSuchFieldException {

        Field field = getDeclaredField(object, propertyName);
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            field.set(object, newValue);
        } catch (IllegalAccessException e) {
            logger.info("Can't set " + object.getClass().getName() + "." + propertyName + "");
        }
        field.setAccessible(accessible);
    }

    /**
     * 暴力调用对象函数，忽略private、protected修饰符的限制。
     *
     * @param object     对象
     * @param methodName 方法名
     * @param params     方法参数列表
     * @return 调用方法后的返回值
     * @throws NoSuchMethodException 没有该方法时抛出
     */
    public static Object invokePrivateMethod(Object object, String methodName, Object... params)
            throws NoSuchMethodException {


        Class[] types = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            types[i] = params[i].getClass();
        }

        Class clazz = object.getClass();
        Method method = null;
        for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                method = superClass.getDeclaredMethod(methodName, types);
                break;
            } catch (NoSuchMethodException e) {
                // 方法不在当前类定义,继续向上转型
            }
        }

        if (method == null)
            throw new NoSuchMethodException("No Such Method:" + clazz.getSimpleName() + methodName);

        boolean accessible = method.isAccessible();
        method.setAccessible(true);
        Object result = null;
        try {
            result = method.invoke(object, params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        method.setAccessible(accessible);
        return result;
    }

    /**
     * 按类型取得字段列表。
     *
     * @param object       对象
     * @param propertyType 字段类型
     * @return 字段列表
     */
    public static List<Field> getFieldsByType(Object object, Class propertyType) {
        List<Field> list = new ArrayList<>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().isAssignableFrom(propertyType)) {
                list.add(field);
            }
        }
        return list;
    }

    /**
     * 获取某个类中的所有静态字段
     *
     * @param clazz
     * @return
     */
    public static List<Field> getStaticFields(Class clazz) {
        List<Field> list = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                list.add(field);
            }
        }
        return list;
    }

    /**
     * 获取某个类中的指定类型静态字段
     *
     * @param clazz
     * @param propertyType
     * @return
     */
    public static List<Field> getStaticFieldsByType(Class clazz, Class propertyType) {
        List<Field> list = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers()) && field.getType().isAssignableFrom(propertyType)) {
                list.add(field);
            }
        }
        return list;
    }


    /**
     * 按属性名称获得属性的类型。
     *
     * @param clazz        对象
     * @param propertyName 属性名称
     * @return 属性类型
     * @throws NoSuchFieldException 没有该Field时抛出
     */
    public static Class getPropertyType(Class clazz, String propertyName) throws NoSuchFieldException {
        return getDeclaredField(clazz, propertyName).getType();
    }

    /**
     * 按照指定的类型获取属性值列表。
     * @param bean
     * @param fieldClass 属性值类型
     * @param <T> 返回的属性值类
     * @return
     */
    public static <T> List<T> getPropertiesByType(Object bean, Class<T> fieldClass) {
        List<Field> fieldsByType = getFieldsByType(bean.getClass(), fieldClass);

        List<T> ret = new ArrayList<>();
        for (Field field : fieldsByType) {
            field.setAccessible(true);
            try {
                T p = (T) field.get(bean);
                if (p != null) {
                    ret.add(p);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * 获得field的getter函数名称。
     *
     * @param clazz     对象
     * @param fieldName 属性名称
     * @return 函数名称
     * @throws NoSuchFieldException 没有该Field时抛出
     */
    public static String getGetterName(Class clazz, String fieldName) throws NoSuchFieldException {
        Class type = getPropertyType(clazz, fieldName);
        if (type.getSimpleName().toLowerCase().equals("boolean")) {
            return "is" + StringUtils.capitalize(fieldName);
        }
        else {
            return "get" + StringUtils.capitalize(fieldName);
        }
    }
}