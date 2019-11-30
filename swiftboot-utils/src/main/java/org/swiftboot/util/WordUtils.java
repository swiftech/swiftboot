package org.swiftboot.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * 单词工具类 (Beta)
 *
 * @author swiftech
 */
public class WordUtils {

    /**
     * 从所有单词中取前面的字母合并，并不超过最大长度限制，如果字母总数低于最大长度，则直接返回所有的字母
     * 所有字母被转换为小写字符
     *
     * @param words
     * @param len
     * @return
     */
    public static String joinWords(String[] words, int len) {
        if (Arrays.stream(words).count() <= len) {
            return StringUtils.join(words).toLowerCase();
        }
        int a = len / words.length;
        int b = len % words.length;
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            int x = a + (i >= (words.length - b) ? 1 : 0);
            String word = words[i];
            buf.append(StringUtils.substring(word, 0, x).toLowerCase());
        }

        if (buf.length() < len) {
            return buf.toString() + RandomStringUtils.randomAlphabetic(len - buf.length());
        }
        else if (buf.length() > len) {
            return buf.substring(0, len); // 如果太长自动截掉
        }
        else {
            return buf.toString();
        }
    }
}
