package org.swiftboot.web;

import org.swiftboot.util.Info;

public class R implements Info.Resource {

    public static final String REFLECT_TYPE_OF_ENTITY_FAIL = "reflect_type_of_entity_fail";
    public static final String CONSTRUCT_ENTITY_FAIL1 = "construct_entity_fail1";
    public static final String FIELD_REQUIRED_FOR_ENTITY2 = "field_required_for_entity2";
    public static final String POPULATE_FIELD_FAIL1 = "populate_field_fail1";
    public static final String NO_RESOURCE_FOR_ERR_CODE1 = "no_resource_for_err_code1";
    public static final String NO_MSG_FOR_CODE1 = "no_msg_for_code1";
    public static final String NO_PRE_DEFINED_MSG = "no_pre_defined_msg";
    public static final String REPEAT_MSG1 = "repeat_msg1";
    public static final String INIT_PRE_DEFINED_MSG1 = "init_pre_defined_msg1";
    public static final String INIT_USER_DEFINED_MSG1 = "init_user_defined_msg1";
    public static final String TOTAL_MSG_COUNT1 = "total_msg_count1";
    public static final String IGNORE_MSG1 = "ignore_msg1";
    public static final String NOT_FOUND_MSG1 = "not_found_msg1";
    public static final String CODE_EXIST2 = "code_exist2";
    public static final String INIT_FAIL = "init_fail";
    public static final String INIT_COUNT1 = "init_count1";
    public static final String VALIDATE_INI1T = "validate_ini1t";
    public static final String I18N_INIT_START = "i18n_init_start";
    public static final String I18N_INIT_DONE = "i18n_init_done";
    public static final String PARAM_NOT_IMPLEMENT_INTERFACE2 = "param_not_implement_interface2";
    public static final String PARAM_NOT_EXTEND_CLASS2 = "param_not_extend_class2";
    public static final String CONVERT_TO_JSON_FAIL = "convert_to_json_fail";
    public static final String REQUIRE_PARAMS = "require_params";
    public static final String REQUIRE_NO_PARAM_CONSTRUCTOR1 = "require_no_param_constructor1";
    public static final String REQUIRE_ENTITY = "require_entity";
    public static final String CONSTRUCT_FAIL2 = "construct_fail2";
    public static final String POPULATE_FROM_ENTITY1 = "populate_from_entity1";
    public static final String POPULATE_COLLECTION_FAIL1 = "populate_collection_fail1";
    public static final String REQUIRE_IMAGE_FILE1 = "require_image_file1";
    public static final String READ_FROM_INPUT_STREAM_FAIL = "read_from_input_stream_fail";
    public static final String PRODUCTION = "production";
    public static final String TESTING = "testing";
    public static final String DEVELOPMENT = "development";

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
