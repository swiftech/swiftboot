package org.swiftboot.util;

import org.swiftboot.util.Info.Resource;

/**
 * @author swiftech
 * @since 1.1
 */
public class R implements Resource {

    public static final String ID_FAILED1 = "id_failed1";
    public static final String ID_FAILED2 = "id_failed2";
    public static final String NO_FIELD_BY_TYPE2 = "no_field_by_type2";
    public static final String NO_FIELD2 = "no_field2";
    public static final String PARAMS_REQUIRED = "params_required";
    public static final String GET_VALUE_FAIL1 = "get_value_fail1";
    public static final String SET_VALUE_FAIL2 = "set_value_fail2";
    public static final String NO_GENERIC_SUPER_CLASS = "no_generic_super_class";
    public static final String NO_GENERIC_CLASS_TO_ANCESTOR1 = "no_generic_class_to_ancestor1";
    public static final String NO_GENERIC_CLASS_TO_PARENT1 = "no_generic_class_to_parent1";
    public static final String NO_DATA_FOUND1 = "no_data_found1";


    public static Class<?>[] getResourceClasses() {
        return new Class[]{
                R.class
        };
    }

    public static void main(String[] args) {
        System.out.println(Info.get(IdUtils.class, org.swiftboot.util.R.ID_FAILED1));;
//        Info.validateForAllLocale();
        System.out.println();
        System.out.println(Info.get(IdUtils.class, org.swiftboot.util.R.ID_FAILED1));;
    }
}
