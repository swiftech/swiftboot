package org.swiftboot.collections;

import org.swiftboot.util.Info;

import java.util.Locale;

public class R {

    static {
        register();// Make sure standalone app or unit test registered
    }

    public static void register() {
        org.swiftboot.util.R.register();
        Info.register("/swiftboot-collections", R.class);
    }

    public static final String COLLECTION_TYPE_NOT_SUPPORTED1 = "collection_type_not_supported1";
    public static final String QUEUE_SIZE_NOT_MATCH2 = "queue_size_not_match2";


    public static void main(String[] args) {
        R.register();
        Info.validateProperties(Locale.ENGLISH);
        Info.validateProperties(Locale.SIMPLIFIED_CHINESE);
    }
}
