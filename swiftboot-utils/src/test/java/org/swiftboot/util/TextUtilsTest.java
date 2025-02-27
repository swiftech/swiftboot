package org.swiftboot.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author swiftech
 */
class TextUtilsTest {

    @Test
    void lastIndexOf() {

        // simple
        Assertions.assertEquals(0, TextUtils.lastIndexOf("###", "##",  1));
        Assertions.assertEquals(1, TextUtils.lastIndexOf("###", "##",  2));
        Assertions.assertEquals(0, TextUtils.lastIndexOf("###", "###", 2));
        Assertions.assertEquals(-1, TextUtils.lastIndexOf("###", "##", 0));
        // complex
        Assertions.assertEquals(3, TextUtils.lastIndexOf("__###__", "##",  4));

    }

    @Test
    void removeQuotes() {
        Assertions.assertEquals("foobar", TextUtils.removeQuotes("'foobar'"));
        Assertions.assertEquals("foobar", TextUtils.removeQuotes("\"foobar\""));
        Assertions.assertEquals("foobar", TextUtils.removeQuotes("`foobar`"));

        Assertions.assertEquals("foo`bar", TextUtils.removeQuotes("`foo`bar`"));
        Assertions.assertEquals("foo'bar", TextUtils.removeQuotes("`foo'bar`"));

        Assertions.assertEquals("foobar", TextUtils.removeQuotes("'foobar`"));

    }
}