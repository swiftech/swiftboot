package org.swiftboot.util;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

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
        if (countWordsTotalLength(words) <= len) {
            return StringUtils.join(words).toLowerCase();
        }
        int a = len / words.length;
        int b = len % words.length;
        StringBuilder buf = new StringBuilder();

        // 挨个字符找出每个单词需要截取的位置
        int total = 0;
        int[] subLens = new int[words.length];
        counting:
        for (int j = 0; j < len; j++) {
            for (int i = 0; i < words.length; i++) {
                int wordLen = words[i].length();
                if (j < wordLen) {
                    subLens[i] = j + 1;
                    if (++total == len) {
                        break counting;
                    }
                }
            }
        }

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            buf.append(StringUtils.substring(word, 0, subLens[i]).toLowerCase());
        }
        return buf.toString();
    }

    /**
     * 合并单词，用 separator 分隔，并给每个单词加上相同的左右填充字符串
     * @param words
     * @param separator
     * @param pad
     * @return
     */
    public static String joinWordsWithPad(List<String> words, String separator, String pad) {
        return joinWordsWithPad(words, separator, pad, pad);
    }

    /**
     * 合并单词，用 separator 分隔，并给每个单词加上左右填充字符串
     *
     * @param words
     * @param separator
     * @param leftPad
     * @param rightPad
     * @return
     */
    public static String joinWordsWithPad(List<String> words, String separator, String leftPad, String rightPad) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < words.size(); i++) {
            String word = words.get(i);
            buf.append(leftPad).append(word).append(rightPad);
            if (i < words.size() - 1) {
                buf.append(separator);
            }
        }
        return buf.toString();
    }


    /**
     * Count total length of all words in the array
     *
     * @param words
     * @return
     */
    public static int countWordsTotalLength(String[] words) {
        int ret = 0;
        for (String word : words) {
            ret += word.length();
        }
        return ret;
    }
}
