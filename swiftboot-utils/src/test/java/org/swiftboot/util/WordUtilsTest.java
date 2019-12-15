package org.swiftboot.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * allen
 */
public class WordUtilsTest {

    @Test
    public void test() {
        String[] words = new String[]{"ABCDE", "12345"};
        Assertions.assertEquals("abcd1234", WordUtils.joinWords(words, 8));

        words = new String[]{"ABC", "123456"};
        Assertions.assertEquals("abc12345", WordUtils.joinWords(words, 8));

        words = new String[]{"ABC", "123"};
        Assertions.assertEquals("abc123", WordUtils.joinWords(words, 8));

        words = new String[]{"ABCDEF", "123"};
        Assertions.assertEquals("abcde123", WordUtils.joinWords(words, 8));
    }
}
