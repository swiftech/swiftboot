package org.swiftboot.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * 通过反射机制操作对象
 * 提供：
 * 1. 获取类及其父类定义的 Field
 * 2. 忽略 Field 的修饰符获取它的值
 * 3. 忽略修饰符调用私有方法
 * 4. 根据 field 类型获取 Field 定义
 * 5. 获取静态 Field
 *
 * @author swiftech
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
     * @param object     对象
     * @param fieldClass 属性类型
     * @return 字段对象
     * @throws NoSuchFieldException 没有该字段时抛出
     */
    public static List<Field> getDeclaredFields(Object object, Class<?> fieldClass) throws NoSuchFieldException {
        return getDeclaredFields(object.getClass(), fieldClass);
    }

    /**
     * 循环向上转型，获取对象声明的字段。
     *
     * @param clazz      类
     * @param fieldClass 属性类型
     * @return 字段对象
     * @throws NoSuchFieldException 没有该字段时抛出
     */
    public static List<Field> getDeclaredFields(Class<?> clazz, Class<?> fieldClass) throws NoSuchFieldException {
        List<Field> ret = new LinkedList<>();
        for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                for (Field declaredField : superClass.getDeclaredFields()) {
                    if (declaredField.getType() == fieldClass) {
                        ret.add(declaredField);
                    }
                }
            } catch (Exception e) {
                // Field不在当前类定义，继续向上转型
            }
        }
        if (ret.isEmpty()) {
            throw new NoSuchFieldException(Info.get(BeanUtils.class, R.NO_FIELD_BY_TYPE2, fieldClass, clazz.getName()));
        }
        else {
            return ret;
        }
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
    public static Field getDeclaredField(Class<?> clazz, String propertyName) throws NoSuchFieldException {
        for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(propertyName);
            } catch (NoSuchFieldException e) {
                // Field不在当前类定义，继续向上转型
            }
        }
        throw new NoSuchFieldException(Info.get(BeanUtils.class, R.NO_FIELD2, clazz.getName(), propertyName));
    }

    /**
     * 循环向上转型，获取对象声明的字段。
     *
     * @param clazz 类
     * @return 字段对象集合
     */
    public static Collection<Field> getAllFields(Class<?> clazz) {
        Set<Field> ret = new HashSet<>();
        for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
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
    public static Collection<Field> getFieldsIgnore(Class<?> clazz, Collection<String> ignores) {
        Set<Field> ret = new HashSet<>();
        for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            for (Field declaredField : superClass.getDeclaredFields()) {
                if (!ignores.contains(declaredField.getName())) {
                    ret.add(declaredField);
                }
            }
        }
        return ret;
    }

    /**
     * 获取除了 annoClasses 指定的注解修饰之外的 {@link Field} 集合
     *
     * @param clazz
     * @param annoClasses
     * @return
     */
    public static Collection<Field> getFieldsIgnore(Class<?> clazz, Class<? extends Annotation>... annoClasses) {
        Set<Field> ret = new HashSet<>();
        for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            field:
            for (Field declaredField : superClass.getDeclaredFields()) {
                for (Class<? extends Annotation> aClass : annoClasses) {
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
     * 强制获取指定类型的对象属性变量值列表，忽略 private、protected 修饰符的限制，如果值为 null 则不添加到返回结果中
     *
     * @param object     对象
     * @param fieldClass 属性类型
     * @return 属性值列表
     * @throws NoSuchFieldException 没有该字段时抛出
     */
    public static <T extends Object> List<T> forceGetProperties(Object object, Class<T> fieldClass) throws NoSuchFieldException {
        List<T> ret = new LinkedList<>();

        for (Field declaredField : getDeclaredFields(object, fieldClass)) {
            Object item = forceGetProperty(object, declaredField);
            if (item != null) {
                ret.add((T) item);
            }
        }
        return ret;
    }

    /**
     * Force to get all(includes super classes) properties from target bean object, ignoring the private, protected modifier.
     * Static, Transient and Final properties will not be returned.
     *
     * @param object
     * @return
     */
    public static Map<String, Object> forceGetProperties(Object object) {
        Map<String, Object> ret = new LinkedHashMap<>();
        for (Field field : getAllFields(object.getClass())) {
            if ((field.getModifiers() & (Modifier.STATIC | Modifier.TRANSIENT)) != 0) {
                continue;
            }
            Object item = forceGetProperty(object, field);
            if (item != null) {
                ret.put(field.getName(), item);
            }
        }
        return ret;
    }

    /**
     * 强制获取对象变量值，忽略 private、protected 修饰符的限制。
     *
     * @param object       对象
     * @param propertyName 属性名称
     * @return 属性的值
     * @throws NoSuchFieldException 没有该字段时抛出
     */
    public static Object forceGetProperty(Object object, String propertyName) throws NoSuchFieldException {
        Field field = getDeclaredField(object, propertyName);
        return forceGetProperty(object, field);
    }

    /**
     * 强制获取对象变量值，忽略 private、protected 修饰符的限制。
     *
     * @param object 对象
     * @param field  属性域
     * @return 属性的值
     */
    public static Object forceGetProperty(Object object, Field field) {
        if (field == null || object == null) {
            throw new IllegalArgumentException(Info.get(BeanUtils.class, R.PARAMS_REQUIRED));
        }

        boolean accessible = field.isAccessible();
        field.setAccessible(true);

        Object result = null;
        try {
            result = field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(Info.get(BeanUtils.class, R.GET_VALUE_FAIL1, field.getName()));
        }
        field.setAccessible(accessible);
        return result;
    }

    /**
     * 强制设置对象变量值，忽略private、protected修饰符的限制。
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
            logger.info(Info.get(BeanUtils.class, R.SET_VALUE_FAIL2, object.getClass().getName(), propertyName));
        }
        field.setAccessible(accessible);
    }

    /**
     * 强制设置对象变量值，忽略private、protected修饰符的限制。
     *
     * @param object   对象
     * @param field    属性
     * @param newValue 属性值
     */
    public static void forceSetProperty(Object object, Field field, Object newValue) {
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            field.set(object, newValue);
        } catch (IllegalAccessException e) {
            logger.info(Info.get(BeanUtils.class, R.SET_VALUE_FAIL2, object.getClass().getName(), field.getName()));
        }
        field.setAccessible(accessible);
    }

    /**
     * 强制设置对象变量值，忽略private、protected修饰符的限制。
     * 根据变量的类型强制转换
     *
     * @param bean      对象
     * @param fieldName 属性名
     * @param value     属性值
     * @throws NoSuchFieldException
     * @throws ParseException
     * @since 1.1
     */
    public static void forceSetPropertyFromString(Object bean, String fieldName, String value)
            throws NoSuchFieldException, ParseException {
        Field field = BeanUtils.getDeclaredField(bean, fieldName);
        forceSetPropertyFromString(bean, field, value);
    }

    /**
     * 强制设置对象变量值，忽略private、protected修饰符的限制。
     * 根据变量的类型强制转换
     *
     * @param bean  对象
     * @param field 属性
     * @param value 属性值
     * @throws ParseException
     * @since 1.1
     */
    public static void forceSetPropertyFromString(Object bean, Field field, String value)
            throws ParseException {
        if (field.getType() == Integer.class) {
            BeanUtils.forceSetProperty(bean, field, Integer.parseInt(value));
        }
        else if (field.getType() == Long.class) {
            BeanUtils.forceSetProperty(bean, field, Long.parseLong(value));
        }
        else if (field.getType() == Float.class) {
            BeanUtils.forceSetProperty(bean, field, Float.parseFloat(value));
        }
        else if (field.getType() == Double.class) {
            BeanUtils.forceSetProperty(bean, field, Double.parseDouble(value));
        }
        else if (field.getType() == BigDecimal.class) {
            BeanUtils.forceSetProperty(bean, field, BigDecimal.valueOf(Double.parseDouble(value)));
        }
        else if (field.getType() == Date.class) {
            BeanUtils.forceSetProperty(bean, field, FastDateFormat.getInstance().parse(value));
        }
        else if (field.getType() == Boolean.class) {
            BeanUtils.forceSetProperty(bean, field, Boolean.valueOf(value));
        }
        else {
            BeanUtils.forceSetProperty(bean, field, value);
        }
    }

    /**
     * Force set bean's properties from a map by name and class type, ignoring the private, protected modifier.
     * Static, Transient and Final properties will not be returned.
     *
     * @param bean
     * @param data
     */
    public static void forceSetProperties(Object bean, Map<String, Object> data) {
        for (Field field : getAllFields(bean.getClass())) {
            if ((field.getModifiers() & (Modifier.STATIC | Modifier.TRANSIENT | Modifier.FINAL)) != 0) {
                continue;
            }
            Object v = data.get(field.getName());
            if (v == null || (!field.getType().isPrimitive() && v.getClass() != field.getType())) {
                continue;
            }
            forceSetProperty(bean, field, v);
        }
    }

    /**
     * 强制调用对象函数，忽略private、protected修饰符的限制。
     *
     * @param object     对象
     * @param methodName 方法名
     * @param params     方法参数列表
     * @return 调用方法后的返回值
     * @throws NoSuchMethodException 没有该方法时抛出
     */
    public static Object forceInvokeMethod(Object object, String methodName, Object... params)
            throws NoSuchMethodException {
        Class<?>[] types = new Class<?>[params.length];
        for (int i = 0; i < params.length; i++) {
            types[i] = params[i].getClass();
        }

        Class<?> clazz = object.getClass();
        Method method = null;
        for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                method = superClass.getDeclaredMethod(methodName, types);
                break;
            } catch (NoSuchMethodException e) {
                // 方法不在当前类定义,继续向上转型
            }
        }

        if (method == null)
            throw new NoSuchMethodException(Info.get(BeanUtils.class, R.NO_FIELD2, clazz.getSimpleName(), methodName));

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

    public static List<Field> getDeclaredFieldsByAnnotation(Class<?> targetClass, Class<? extends Annotation> annoClass) {
        List<Field> ret = new ArrayList<>();
        for (Class<?> superClass = targetClass; superClass != Object.class; superClass = superClass.getSuperclass()) {
            for (Field field : superClass.getDeclaredFields()) {
                if (field.getAnnotation(annoClass) == null) {
                    continue;
                }
                ret.add(field);
            }
        }
        return ret;
    }

    /**
     * 按类型取得字段列表。
     *
     * @param object       对象
     * @param propertyType 字段类型
     * @return 字段列表
     */
    public static List<Field> getDeclaredFieldsByType(Object object, Class<?> propertyType) {
        List<Field> list = new ArrayList<>();
        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            for (Field field : superClass.getDeclaredFields()) {
                if (propertyType.isAssignableFrom(field.getType())) {
                    list.add(field);
                }
            }
        }
        return list;
    }

    /**
     * 按类型取得字段列表，忽略 annoClasses 指定的注解修饰的
     *
     * @param object
     * @param propertyType 过滤的属性类型
     * @param annoClasses  过滤的注解类型
     * @return
     */
    public static List<Field> getDeclaredFieldsByTypeIgnore(Object object, Class<?> propertyType, Class<? extends Annotation>... annoClasses) {
        List<Field> list = new ArrayList<>();
        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            NEXT_FIELD:
            for (Field field : superClass.getDeclaredFields()) {
                for (Class<? extends Annotation> annoClass : annoClasses) {
                    Annotation annotation = field.getAnnotation(annoClass);
                    if (annotation != null) {
                        continue NEXT_FIELD;
                    }
                }
                if (propertyType.isAssignableFrom(field.getType())) {
                    list.add(field);
                }
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
    public static List<Field> getStaticFields(Class<?> clazz) {
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
    public static List<Field> getStaticFieldsByType(Class<?> clazz, Class<?> propertyType) {
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
    public static Class<?> getPropertyType(Class<?> clazz, String propertyName) throws NoSuchFieldException {
        return getDeclaredField(clazz, propertyName).getType();
    }

    /**
     * 按照指定的类型获取属性值列表。
     *
     * @param bean
     * @param fieldClass 属性值类型
     * @param <T>        返回的属性值类
     * @return
     */
    public static <T> List<T> getPropertiesByType(Object bean, Class<T> fieldClass) {
        List<Field> fieldsByType = getDeclaredFieldsByType(bean.getClass(), fieldClass);

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
    public static String getGetterName(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Class<?> type = getPropertyType(clazz, fieldName);
        if (type.getSimpleName().equalsIgnoreCase("boolean")) {
            return "is" + StringUtils.capitalize(fieldName);
        }
        else {
            return "get" + StringUtils.capitalize(fieldName);
        }
    }
}