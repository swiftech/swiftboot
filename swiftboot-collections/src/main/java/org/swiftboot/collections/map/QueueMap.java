package org.swiftboot.collections.map;

import org.swiftboot.collections.R;
import org.swiftboot.util.Info;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 带有队列功能的 Map，所有的 Key 有序存储在队列中
 *
 * @param <K> 健类型
 * @param <V> 值的类型
 * @author swiftech
 */
public class QueueMap<K extends Serializable, V extends Serializable> extends LinkedHashMap<K, V> {

    /**
     * 元素最大数量，超出最大数量则删除队列头的元素，0 表示不限制，默认为 0
     */
    private int max = 0;

    // Key的队列，用来处理Map的访问
    protected LinkedList<K> keyQ = new LinkedList<>();

    public QueueMap() {
        // For Jackson Mapper
    }

    public QueueMap(Map<K, V> map) {
        for (K k : map.keySet()) {
            V v = map.get(k);
//            Long newKey = Long.parseLong(k.toString());
            this.put(k, v);
        }
    }

    public QueueMap(int max) {
        super(max);
        this.max = max;
    }

    public LinkedList<K> getKeyQ() {
        return keyQ;
    }

    /**
     * 增加新的键值对，如果超出最大长度，则把最前面的元素移除。
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public V put(K key, V value) {
        if (keyQ.size() != this.size()) {
            throw new IllegalStateException(Info.get(QueueMap.class, R.QUEUE_SIZE_NOT_MATCH2, keyQ.size(), this.size()));
        }
        // 处理重复情况
        if (this.containsKey(key)) {
            return super.put(key, value);
        }
        keyQ.add(key);
        if (max > 0) {
            int offset = keyQ.size() - max;
            if (offset > 0) {
                for (int i = 0; i < offset; i++) {
                    K head = keyQ.poll();
                    this.remove(head);
                }
            }
        }
        return super.put(key, value);
    }

    /**
     * 获取队列中最后若干个元素，不够则返回全部
     *
     * @param count
     * @return
     */
    public Map<K, V> getLast(int count) {
        LinkedList<K> keys = (LinkedList<K>) getKeyQ().clone();
        LinkedHashMap<K, V> result = new LinkedHashMap<>();

        int len = keys.size();
        List<K> subKeys = keys.subList(Math.max(0, len - count), len);
        for (K key : subKeys) {
            result.put(key, this.get(key));
        }
        return result;
    }

    /**
     * 队列尾部的值
     *
     * @return
     */
    public V tailValue() {
        K k = keyQ.getLast();
        return this.get(k);
    }

    /**
     * 设置集合元素最大数量
     *
     * @param max
     */
    public void setMax(int max) {
        this.max = max;
    }
}
