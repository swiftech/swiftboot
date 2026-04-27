package org.swiftboot.util;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class I18nHelperTest {

    @BeforeAll
    public static void setup() {
        I18nHelper.getInstance().setLocale(Locale.SIMPLIFIED_CHINESE);
        I18nHelper.getInstance().addBundle("i18n/messages");
        I18nHelper.getInstance().reloadAllBundles();
    }

    @Test
    void get() {
        System.out.println(I18nHelper.getInstance().get("msg.single"));
    }

    @Test
    void getWithParams() {
        System.out.println(I18nHelper.getInstance().get("msg.params", "arg0", "arg1"));
        System.out.println(I18nHelper.getInstance().get("msg.params.error", "arg0", "arg1"));
    }
}