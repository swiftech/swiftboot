package org.swiftboot.collections;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author swiftech
 **/
public class MapUtils {


    /**
     * 将key-value字符串转换成Map，key不能有重复
     *
     * @param content 格式: k1:v1,k2:v2,k3:v3
     * @return
     */
    public static Map<String, String> parseKeyValueString(String content) {
        return parseKeyValueString(content, ',', ':');
    }

    /**
     * 将key-value字符串转换成Map，key不能有重复
     *
     * @param content
     * @param pairSeparator
     * @param kvSeparator
     * @return
     */
    public static Map<String, String> parseKeyValueString(String content, Character pairSeparator, Character kvSeparator) {
        Map<String, String> ret = new HashMap<>();
        String[] all = StringUtils.split(content, pairSeparator);
        for (String entry : all) {
            String[] kv = StringUtils.split(entry.trim(), kvSeparator);
            if (ArrayUtils.isEmpty(kv) || kv.length < 2) {
                continue;
            }
            ret.put(kv[0].trim(), kv[1].trim());
        }
        return ret;
    }



}
