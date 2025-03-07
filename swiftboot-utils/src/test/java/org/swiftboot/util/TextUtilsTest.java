package org.swiftboot.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author swiftech
 */
class TextUtilsTest {

    @Test
    void lastIndexOf() {

        // simple
        Assertions.assertEquals(0, TextUtils.lastIndexOf("###", "##", 1));
        Assertions.assertEquals(1, TextUtils.lastIndexOf("###", "##", 2));
        Assertions.assertEquals(0, TextUtils.lastIndexOf("###", "###", 2));
        Assertions.assertEquals(-1, TextUtils.lastIndexOf("###", "##", 0));
        // complex
        Assertions.assertEquals(3, TextUtils.lastIndexOf("__###__", "##", 4));

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

    @Test
    void joinList() {
        List<String> list = new ArrayList<String>() {
            {
                add("foo");
                add(null);
                add("");
                add(" ");
                add("bar");
            }
        };
        Assertions.assertEquals("foo,bar", TextUtils.join(list, ","));
    }

    @Test
    void joinArray() {
        String[] array = new String[]{"foo", null, "", " ", "bar"};
        Assertions.assertEquals("foo,bar", TextUtils.join(array, ","));
    }
}