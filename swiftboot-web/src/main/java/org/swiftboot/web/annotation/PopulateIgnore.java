package org.swiftboot.web.annotation;

import org.swiftboot.web.request.BasePopulateRequest;
import org.swiftboot.web.dto.BasePopulateDto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注填充时忽略的类属性
 *
 * @author swiftech
 * @see BasePopulateDto
 * @see BasePopulateRequest
 **/
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PopulateIgnore {

    boolean value() default true;
}
