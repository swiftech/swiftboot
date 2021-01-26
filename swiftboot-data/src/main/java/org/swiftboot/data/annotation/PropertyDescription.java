package org.swiftboot.data.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 描述实体类属性
 *
 * @author swiftech
 **/
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertyDescription {

    /**
     * 属性描述
     * @return
     */
    String value() default "";

    /**
     * 属性备注
     * @return
     */
    String notes()  default "";

    /**
     * 属性值样例
     * @return
     */
    String example() default "";
}
