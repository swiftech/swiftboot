package org.swiftboot.collections.map;

import org.apache.commons.lang3.Range;

import java.util.HashMap;
import java.util.Map;

/**
 * 用取值范围（Range）作为 Key 存储键值，但是直接用 Range 中的元素作为 key 来操作。
 *
 * @author swiftech
 * @param <K>
 * @param <V>
 */
public class IntRangeKeyMap<K extends Comparable, V> {
	Map<Range<K>, V> internalMap = new HashMap<>();

	/**
	 * 是否包含 key 的范围
	 *
	 * @param key
	 * @return
	 */
	public boolean containsKey(K key) {
		for (Range<K> rangeKey : internalMap.keySet()) {
			if (rangeKey.contains(key)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取范围中包含 key 的值
	 *
	 * @param key
	 * @return
	 */
	public V get(K key) {
		for (Range<K> rangeKey : internalMap.keySet()) {
			if (rangeKey.contains(key)) {
				return internalMap.get(rangeKey);
			}
		}
		return null;
	}

	/**
	 * 覆盖所有范围中包含 key 的值
	 *
	 * @param key
	 * @param value
	 */
	public void put(K key, V value) {
		for (Range<K> rangeKey : internalMap.keySet()) {
			if (rangeKey.contains(key)) {
				internalMap.put(rangeKey, value);
				return;
			}
		}
		internalMap.put(Range.is(key), value);
	}

	/**
	 * 如果所有范围中和 begin 到 to 之间有任何重叠，则合并两个范围作为新的 key 来存储值。
	 * 没有则新增一个键值对。
	 *
	 * @param begin
	 * @param to
	 * @param value
	 */
	public void put(K begin, K to, V value) {
		Range<K> key = Range.between(begin, to);
		for (Range<K> rangeKey : internalMap.keySet()) {
			// 合并两个key作为新的key并移除旧的
			if (rangeKey.isOverlappedBy(key)) {
				Range<K> newRange = Range.between(
						key.getMinimum().compareTo(rangeKey.getMinimum()) < 0 ? key.getMinimum() : rangeKey.getMinimum(),
						key.getMaximum().compareTo(rangeKey.getMaximum()) > 0 ? key.getMaximum() : rangeKey.getMaximum()
				);
				internalMap.put(newRange, value);
				internalMap.remove(rangeKey);
				return;
			}
		}
		internalMap.put(key, value);
	}

}
