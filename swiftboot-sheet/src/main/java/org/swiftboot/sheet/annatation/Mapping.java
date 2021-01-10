package org.swiftboot.sheet.annatation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author allen
 */
@Target({ElementType.FIELD})
@Retention(RUNTIME)
@Documented
public @interface Mapping {

    String value();
}
