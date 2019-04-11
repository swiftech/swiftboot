package org.swiftboot.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Allen 2019-04-01
 **/
public class BeanUtilsTest {

    @Test
    public void testGetFieldsIgnore() {
        Collection<Field> fieldsIgnore = BeanUtils.getFieldsIgnore(FooBarBean.class, JsonIgnore.class);
        List<Field> l = new ArrayList<>(fieldsIgnore);
        Assertions.assertEquals(1, fieldsIgnore.size());
        Assertions.assertEquals("field1", l.get(0).getName());
    }

}
