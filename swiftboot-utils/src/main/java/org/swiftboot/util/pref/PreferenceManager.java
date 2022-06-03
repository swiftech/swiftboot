package org.swiftboot.util.pref;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * A single wrapper class for Java Preference class.
 *
 * @author allen
 * @since 2.3
 */
public class PreferenceManager {

    /**
     * Class that identifies the application.
     */
    private static Class<?> ownerClass;

    private static PreferenceManager instance;

    private final Preferences prefs;

    private final Map<Class, Converter> converterMap = new HashMap<>();

    /**
     * prefix adds to the preference key with dot '.',
     * like "prefix.my_pref"
     */
    private String prefix;

    private PreferenceManager(Class clazz) {
        prefs = Preferences.userNodeForPackage(clazz);
    }

    public static synchronized PreferenceManager getInstance(Class ownerClass) {
        if (ownerClass == null) {
            throw new RuntimeException("Owner class represent the application must be provided");
        }
        PreferenceManager.ownerClass = ownerClass;
        return getInstance();
    }

    /**
     * Setup ownerClass before calling this method.
     *
     * @return
     */
    public static synchronized PreferenceManager getInstance() {
        if (instance == null) {
            if (ownerClass == null) {
                ownerClass = PreferenceManager.class;
            }
            System.out.println("Initialize preference for class: " + ownerClass);
            instance = new PreferenceManager(ownerClass);
        }
        return instance;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    private String absoluteKey(String key) {
        if (StringUtils.isNotBlank(prefix)) {
            return String.format("%s.%s", prefix, key);
        }
        return key;
    }

    /**
     * Add converter to user customized object.
     * The converter converts customized object to value that can be saved to preferences storage, and also reversely.
     *
     * @param clazz
     * @param converter
     */
    public void addConverter(Class clazz, Converter converter) {
        converterMap.put(clazz, converter);
    }

    /**
     * @param key
     * @param value
     */
    public void savePreference(String key, Object value) {
        if (value == null) return;
        key = absoluteKey(key);
        if (value instanceof Integer) {
            prefs.putInt(key, (Integer) value);
        }
        else if (value instanceof Long) {
            prefs.putLong(key, (Long) value);
        }
        else if (value instanceof Boolean) {
            prefs.putBoolean(key, (Boolean) value);
        }
        else if (value instanceof Double) {
            prefs.putDouble(key, (Double) value);
        }
        else if (value instanceof byte[]) {
            prefs.putByteArray(key, (byte[]) value);
        }
        else if (value instanceof List) {
            prefs.put(key, StringUtils.join((List) value, ","));
        }
        else {
            for (Class c : converterMap.keySet()) {
                if (c.isAssignableFrom(value.getClass())) {
                    Converter converter = converterMap.get(c);
                    Class saveAs = converter.getSaveAs();
                    System.out.println("Save as: " + saveAs);
                    Object serialized = converter.serialize(value);
                    if (saveAs == String.class) {
                        prefs.put(key, (String) serialized);
                        return;
                    }
                    else if (saveAs == Integer.class) {
                        prefs.putInt(key, (Integer) serialized);
                        return;
                    }
                    else if (saveAs == Long.class) {
                        prefs.putLong(key, (Long) serialized);
                        return;
                    }
                    else if (saveAs == byte[].class) {
                        prefs.putByteArray(key, (byte[]) serialized);
                        return;
                    }
                    else {
                        throw new RuntimeException(String.format("Can't save preference as data type %s", saveAs));
                    }
                }
            }
//            else {
//                prefs.put(key, String.valueOf(value));
//            }
        }
    }

    /**
     * Get preference by field of an object, if not exists, return the field value of object.
     *
     * @param key
     * @param field
     * @param target
     * @param <T>
     * @return
     * @throws IllegalAccessException
     */
    public <T> T getPreference(String key, Field field, Object target) throws IllegalAccessException {
        key = absoluteKey(key);
        Class<T> clazz = (Class<T>) field.getType();
        field.setAccessible(true);
        Object val = null;
        if (clazz == Integer.class || clazz == int.class) {
            val = prefs.getInt(key, field.getInt(target));
        }
        else if (clazz == Long.class || clazz == long.class) {
            val = prefs.getLong(key, field.getLong(target));
        }
        else if (clazz == Boolean.class || clazz == boolean.class) {
            val = prefs.getBoolean(key, field.getBoolean(target));
        }
        else if (clazz == Float.class || clazz == float.class) {
            val = prefs.getFloat(key, field.getFloat(target));
        }
        else if (clazz == Double.class || clazz == double.class) {
            val = prefs.getDouble(key, field.getDouble(target));
        }
        else if (clazz == byte[].class) {
            val = prefs.getByteArray(key, (byte[]) field.get(target));
        }
        else if (clazz == String.class) {
            val = prefs.get(key, (String) field.get(target));
        }
        else {
            for (Class c : converterMap.keySet()) {
                if (c.isAssignableFrom(clazz)) {
                    Converter converter = converterMap.get(clazz);
                    Class saveAs = converter.getSaveAs();
                    if (saveAs == String.class) {
                        String s = prefs.get(key, null);
                        val = s == null ? field.get(target) : (T) converter.deserialize(s);
                    }
                    else if (saveAs == Integer.class) {
                        int v = prefs.getInt(key, Integer.MIN_VALUE);
                        val = v == Integer.MIN_VALUE ? field.getInt(target) : (T) converter.deserialize(Integer.valueOf(v));
                    }
                    else if (saveAs == Long.class) {
                        long v = prefs.getLong(key, Long.MIN_VALUE);
                        val = v == Long.MIN_VALUE ? field.getLong(target) : (T) converter.deserialize(v);
                    }
                    else if (saveAs == byte[].class) {
                        byte[] v = prefs.getByteArray(key, null);
                        val = v == null ? (byte[]) field.get(target) : (T) converter.deserialize(v);
                    }
                    else {
                        throw new RuntimeException(String.format("Can't get preference as data type %s", saveAs));
                    }
                    break;
                }
            }
        }
        if (val == null) {
            return null;
        }
        return (T) val;
    }

    /**
     * Get preference by class type, if not exits, return defaultValue.
     *
     * @param key
     * @param clazz
     * @param defaultValue
     * @param <T>
     * @return
     */
    public <T> T getPreference(String key, Class<T> clazz, T defaultValue) {
        if (defaultValue == null) {
            throw new RuntimeException(String.format("default value for %s can't be null", key));
        }
        key = absoluteKey(key);
        Object val = null;
        if (clazz == Integer.class || clazz == int.class) {
            val = prefs.getInt(key, (Integer) defaultValue);
        }
        else if (clazz == Long.class || clazz == long.class) {
            val = prefs.getLong(key, (Long) defaultValue);
        }
        else if (clazz == Boolean.class || clazz == boolean.class) {
            val = prefs.getBoolean(key, (Boolean) defaultValue);
        }
        else if (clazz == Float.class || clazz == float.class) {
            val = prefs.getFloat(key, (Float) defaultValue);
        }
        else if (clazz == Double.class || clazz == double.class) {
            val = prefs.getDouble(key, (Double) defaultValue);
        }
        else if (clazz == byte[].class) {
            val = prefs.getByteArray(key, ((byte[]) defaultValue));
        }
        else if (clazz == String.class) {
            val = prefs.get(key, (String) defaultValue);
        }
        else {
            for (Class c : converterMap.keySet()) {
                if (c.isAssignableFrom(clazz)) {
                    Converter converter = converterMap.get(clazz);
                    Class saveAs = converter.getSaveAs();
                    if (saveAs == String.class) {
                        String s = prefs.get(key, null);
                        val = s == null ? defaultValue : (T) converter.deserialize(s);
                    }
                    else if (saveAs == Integer.class) {
                        int v = prefs.getInt(key, Integer.MIN_VALUE);
                        val = v == Integer.MIN_VALUE ? defaultValue : (T) converter.deserialize(Integer.valueOf(v));
                    }
                    else if (saveAs == Long.class) {
                        long v = prefs.getLong(key, Long.MIN_VALUE);
                        val = v == Long.MIN_VALUE ? defaultValue : (T) converter.deserialize(Long.valueOf(v));
                    }
                    else if (saveAs == byte[].class) {
                        byte[] v = prefs.getByteArray(key, null);
                        val = v == null ? defaultValue : (T) converter.deserialize(v);
                    }
                    else {
                        throw new RuntimeException(String.format("Can't get preference as data type %s", saveAs));
                    }
                    break;
                }
            }
        }
        if (val == null) {
            return defaultValue;
        }
        return (T) val;
    }

    /**
     * Get preference by class type, if not exists, return MIN_VALUE for integer/long or float/double type, empty bytes array for byte array type
     * false for boolean type, "" for string type, null for user customized object.
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getPreference(String key, Class<T> clazz) {
        key = absoluteKey(key);
        Object val = null;
        if (clazz == Integer.class || clazz == int.class) {
            val = (T) Integer.valueOf(prefs.getInt(key, Integer.MIN_VALUE));
        }
        else if (clazz == Long.class || clazz == long.class) {
            val = (T) Long.valueOf(prefs.getLong(key, Long.MIN_VALUE));
        }
        else if (clazz == Boolean.class || clazz == boolean.class) {
            val = (T) Boolean.valueOf(prefs.getBoolean(key, Boolean.FALSE));
        }
        else if (clazz == Float.class || clazz == float.class) {
            val = (T) Float.valueOf(prefs.getFloat(key, Float.MIN_VALUE));
        }
        else if (clazz == Double.class || clazz == double.class) {
            val = (T) Double.valueOf(prefs.getDouble(key, Double.MIN_VALUE));
        }
        else if (clazz == byte[].class) {
            val = (T) prefs.getByteArray(key, new byte[]{});
        }
        else if (clazz == String.class) {
            val = (T) prefs.get(key, ""); // as string
        }
        else {
            for (Class c : converterMap.keySet()) {
                if (clazz.isAssignableFrom(c)) {
                    Converter converter = converterMap.get(clazz);
                    Class saveAs = converter.getSaveAs();
                    if (saveAs == String.class) {
                        String v = prefs.get(key, null);
                        val = v == null ? null : (T) converter.deserialize(v);
                    }
                    else if (saveAs == Integer.class) {
                        int v = prefs.getInt(key, Integer.MIN_VALUE);
                        val = v == Integer.MIN_VALUE ? null : (T) converter.deserialize(Integer.valueOf(v));
                    }
                    else if (saveAs == Long.class) {
                        long v = prefs.getLong(key, Long.MIN_VALUE);
                        val = v == Long.MIN_VALUE ? null : (T) converter.deserialize(Long.valueOf(v));
                    }
                    else if (saveAs == byte[].class) {
                        byte[] v = prefs.getByteArray(key, null);
                        val = v == null ? null : (T) converter.deserialize(v);
                    }
                    else {
                        throw new RuntimeException(String.format("Can't get preference as data type %s", saveAs));
                    }
                    break;
                }
            }
        }
        if (val == null) {
            return null;
        }
        return (T) val;
    }

    /**
     * Get preference only by key, if not exists, return user provided default value.
     *
     * @param key
     * @param def
     * @param <T>
     * @return
     */
    public <T> Object getPreference(String key, T def) {
        if (def == null) {
            throw new RuntimeException(String.format("default value for %s can't be null", key));
        }
        key = absoluteKey(key);
        if (def instanceof Integer) {
            return prefs.getInt(key, (Integer) def);
        }
        else if (def instanceof Long) {
            return prefs.getLong(key, (Long) def);
        }
        else if (def instanceof Boolean) {
            return prefs.getBoolean(key, (Boolean) def);
        }
        else if (def instanceof Float) {
            return prefs.getFloat(key, (Float) def);
        }
        else if (def instanceof Double) {
            return prefs.getDouble(key, (Double) def);
        }
        else if (def instanceof byte[]) {
            return prefs.getByteArray(key, (byte[]) def);
        }
        else {
            for (Class clazz : converterMap.keySet()) {
                if (clazz.isAssignableFrom(def.getClass())) {
                    Converter converter = converterMap.get(clazz);
                    Class saveAs = converter.getSaveAs();
                    if (saveAs == String.class) {
                        String s = prefs.get(key, null);
                        return s == null ? def : (T) converter.deserialize(s);
                    }
                    else if (saveAs == Integer.class) {
                        int v = prefs.getInt(key, Integer.MIN_VALUE);
                        return v == Integer.MIN_VALUE ? def : (T) converter.deserialize(Integer.valueOf(v));
                    }
                    else if (saveAs == Long.class) {
                        long v = prefs.getLong(key, Long.MIN_VALUE);
                        return v == Long.MIN_VALUE ? def : (T) converter.deserialize(Long.valueOf(v));
                    }
                    else if (saveAs == byte[].class) {
                        byte[] v = prefs.getByteArray(key, null);
                        return v == null ? def : (T) converter.deserialize(v);
                    }
                    else {
                        throw new RuntimeException(String.format("Can't get preference as data type %s", saveAs));
                    }
                }
            }
            return prefs.get(key, String.valueOf(def));
        }
    }

    public void removePreference(String key) {
        prefs.remove(key);
    }

    public void flush() {
        try {
            prefs.flush();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }

}
