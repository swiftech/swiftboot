package org.swiftboot.service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 序列化和反序列化工具类
 *
 * @author swiftech
 */
public class SerializeUtils {

    private static final Logger logger = LoggerFactory.getLogger(SerializeUtils.class);

    /**
     * 反序列化
     *
     * @param bytes
     * @return
     */
    public static Object deserialize(byte[] bytes) {
        Object result = null;
        if (isEmpty(bytes)) {
            return null;
        }
        try (ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes)) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(byteStream)) {
                try {
                    result = objectInputStream.readObject();
                } catch (ClassNotFoundException ex) {
                    throw new Exception("Failed to deserialize object type", ex);
                }
            } catch (Throwable ex) {
                throw new Exception("Failed to deserialize", ex);
            }
        } catch (Exception e) {
            logger.error("Failed to deserialize", e);
        }
        return result;
    }

    public static boolean isEmpty(byte[] data) {
        return (data == null || data.length == 0);
    }

    /**
     * 序列化
     *
     * @param object
     * @return
     */
    public static byte[] serialize(Object object) {
        byte[] result = null;
        if (object == null) {
            return new byte[0];
        }
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream(256)) {
            if (!(object instanceof Serializable)) {
                throw new IllegalArgumentException(
                        String.format("%s requires a Serializable payload but received an object of type [%s]",
                                SerializeUtils.class.getSimpleName(), object.getClass().getName()));
            }
            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteStream)) {
                objectOutputStream.writeObject(object);
                objectOutputStream.flush();
                result = byteStream.toByteArray();
            } catch (Throwable ex) {
                throw new Exception("Failed to serialize", ex);
            }
        } catch (Exception ex) {
            logger.error("Failed to serialize", ex);
        }
        return result;
    }
}
