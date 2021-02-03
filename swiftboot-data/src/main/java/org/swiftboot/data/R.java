package org.swiftboot.data;

import org.swiftboot.util.Info;

/**
 * @author swiftech
 * @since 1.3
 */
public class R implements Info.Resource {

    public static final String PARAM_NOT_IMPLEMENT_INTERFACE2 = "param_not_implement_interface2";
    public static final String PARAM_NOT_EXTEND_CLASS2 = "param_not_extend_class2";
    public static final String CONVERT_TO_JSON_FAIL = "convert_to_json_fail";


    public static void main(String[] args) {
        Info.sources = getResourceClasses();
        Info.validateForAllLocale();
    }

    public static Class<?>[] getResourceClasses() {
        return new Class<?>[]{
                R.class,
                org.swiftboot.collections.R.class,
                org.swiftboot.util.R.class
        };
    }

//    public static void main(String[] args) {
//        R.register();
//        Info.validateProperties(Locale.ENGLISH);
//        Info.validateProperties(Locale.SIMPLIFIED_CHINESE);
//    }
}
