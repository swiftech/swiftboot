package org.swiftboot.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author swiftech 2019-04-01
 **/
public class BeanUtilsTest {

    @Test
    public void testGetFieldsIgnore() {
        Collection<Field> fieldsIgnore = BeanUtils.getFieldsIgnore(FooBarBean.class, JsonIgnore.class);
        List<Field> l = new ArrayList<>(fieldsIgnore);
        Assertions.assertEquals(2, fieldsIgnore.size());
        Assertions.assertEquals("field1", l.get(0).getName());
        Assertions.assertEquals("field0", l.get(1).getName());
    }

    @Test
    public void forceGetProperties() {
        Map<String, Object> map = BeanUtils.forceGetProperties(new FooBarBean());
        Assertions.assertEquals(3, map.size());
        Assertions.assertEquals("field 0", map.get("field0"));
        Assertions.assertEquals("field 1", map.get("field1"));
        Assertions.assertEquals("field 2", map.get("field2"));
    }

    @Test
    void forceSetProperties() {
        FooBarBean bean = new FooBarBean();
        Map<String, Object> map = new LinkedHashMap<>();
//        map.put("field0", "xxx0"); // missing one on purpose
        map.put("field1", "xxx1");
        map.put("field2", "xxx2");
        BeanUtils.forceSetProperties(bean, map);

        map = BeanUtils.forceGetProperties(bean);
        Assertions.assertEquals(3, map.size());
        Assertions.assertEquals("field 0", map.get("field0"));
        Assertions.assertEquals("xxx1", map.get("field1"));
        Assertions.assertEquals("xxx2", map.get("field2"));
    }

}
