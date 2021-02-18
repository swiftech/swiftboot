package org.swiftboot.data.model.id;


/**
 * ID 生成器
 *
 * @param <T> 参数类型
 * @author swiftech
 */
public interface IdGenerator<T> {

    /**
     * 按照给出的参数生成 ID
     * @param object
     * @return
     */
    String generate(T object);
}
