package org.swiftboot.util.pref;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.swiftboot.util.BeanUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Objects;

/**
 * @author allen
 */
class PreferenceManagerTest {
    static int vi = 35234;
    static long vl = 235235234;
    static boolean vb = true;
    static float vf = 1235f;
    static double vd = 2323f;
    static String vs = "string_value";
    static byte[] vbs = vs.getBytes();
    static MyStrClass vms = new MyStrClassExt("my", "pref");
    static MyIntClass vmi = new MyIntClass((byte) 'a');

    @BeforeAll
    public static void setup() {
        PreferenceManager.getInstance(PreferenceManagerTest.class);
        PreferenceManager.getInstance().addConverter(MyStrClass.class, new StringConverter<MyStrClass>() {
            @Override
            public MyStrClass deserialize(String prefString) {
                String[] split = StringUtils.split(prefString, ",");
                return new MyStrClassExt(split[0].trim(), split[1].trim());
            }

            @Override
            public String serialize(MyStrClass valueObject) {
                return valueObject.getParam1() + "," + valueObject.getParam2();
            }

        });
        PreferenceManager.getInstance().addConverter(MyIntClass.class, new IntegerConverter<MyIntClass>() {
            @Override
            public MyIntClass deserialize(Integer prefValue) {
                return new MyIntClass(prefValue.byteValue());
            }

            @Override
            public Integer serialize(MyIntClass valueObject) {
                return (int) valueObject.getB();
            }

        });
    }

    @Test
    void savePreference() {
        String key = String.valueOf(System.currentTimeMillis());
        System.out.println("Key: " + key);
        MyStrClass pref = new MyStrClassExt("saved", "preference");
        MyStrClass def = new MyStrClass("default", "value");
        PreferenceManager.getInstance().savePreference(key, pref);
        PreferenceManager.getInstance().savePreference(key, null); // null shouldn't work

        MyStrClass myStrClass = PreferenceManager.getInstance().getPreference(key, MyStrClass.class);
        Assertions.assertEquals("saved", myStrClass.getParam1());
        Assertions.assertEquals("preference", myStrClass.getParam2());

        // test not exist key
        myStrClass = PreferenceManager.getInstance().getPreference("no_exist_key", MyStrClass.class);
        Assertions.assertNull(myStrClass);

        // test default value
        myStrClass = (MyStrClass) PreferenceManager.getInstance().getPreference("no_exist_key", def);
        Assertions.assertEquals(myStrClass, def);
        Assertions.assertSame(myStrClass, def);

        // test default value
        myStrClass = PreferenceManager.getInstance().getPreference("no_exist_key", MyStrClass.class, def);
        Assertions.assertEquals(myStrClass, def);
        Assertions.assertSame(myStrClass, def);

        PreferenceManager.getInstance().removePreference(key);
    }

    @Test
    void getPreferenceDefault() {
        PreferenceManager pm = PreferenceManager.getInstance();

        // test get preference by key and class type with default value
        Assertions.assertEquals("", pm.getPreference("not_exists_key", String.class, ""));
        Assertions.assertEquals(Integer.MIN_VALUE, pm.getPreference("not_exists_key", Integer.class, Integer.MIN_VALUE));
        Assertions.assertEquals(Long.MIN_VALUE, pm.getPreference("not_exists_key", Long.class, Long.MIN_VALUE));
        Assertions.assertEquals(Float.MIN_VALUE, pm.getPreference("not_exists_key", Float.class, Float.MIN_VALUE));
        Assertions.assertEquals(Double.MIN_VALUE, pm.getPreference("not_exists_key", Double.class, Double.MIN_VALUE));
        Assertions.assertEquals(Boolean.TRUE, pm.getPreference("not_exists_key", Boolean.class, Boolean.TRUE));
        Assertions.assertArrayEquals(new byte[]{}, pm.getPreference("not_exists_key", byte[].class, new byte[]{}));
        Assertions.assertEquals(new MyStrClass("a", "b"), pm.getPreference("not_exists_key", MyStrClass.class, new MyStrClass("a", "b")));
        Assertions.assertEquals(new MyIntClass((byte) 'x'), pm.getPreference("not_exists_key", MyIntClass.class, new MyIntClass((byte) 'x')));

        Assertions.assertEquals("", pm.getPreference("not_exists_key", String.class));
        Assertions.assertEquals(Integer.MIN_VALUE, pm.getPreference("not_exists_key", Integer.class));
        Assertions.assertEquals(Long.MIN_VALUE, pm.getPreference("not_exists_key", Long.class));
        Assertions.assertEquals(Float.MIN_VALUE, pm.getPreference("not_exists_key", Float.class));
        Assertions.assertEquals(Double.MIN_VALUE, pm.getPreference("not_exists_key", Double.class));
        Assertions.assertEquals(Boolean.FALSE, pm.getPreference("not_exists_key", Boolean.class));
        Assertions.assertArrayEquals(new byte[]{}, pm.getPreference("not_exists_key", byte[].class));
        Assertions.assertNull(pm.getPreference("not_exists_key", MyStrClass.class));
        Assertions.assertNull(pm.getPreference("not_exists_key", MyIntClass.class));

        // test get preference by key with default value
        Assertions.assertEquals("", pm.getPreference("not_exists_key", ""));
        Assertions.assertEquals(Integer.MIN_VALUE, pm.getPreference("not_exists_key", Integer.MIN_VALUE));
        Assertions.assertEquals(Long.MIN_VALUE, pm.getPreference("not_exists_key", Long.MIN_VALUE));
        Assertions.assertEquals(Float.MIN_VALUE, pm.getPreference("not_exists_key", Float.MIN_VALUE));
        Assertions.assertEquals(Double.MIN_VALUE, pm.getPreference("not_exists_key", Double.MIN_VALUE));
        Assertions.assertEquals(Boolean.TRUE, pm.getPreference("not_exists_key", Boolean.TRUE));
        Assertions.assertArrayEquals(new byte[]{}, (byte[]) pm.getPreference("not_exists_key", new byte[]{}));
        Assertions.assertEquals(new MyStrClass("a", "b"), pm.getPreference("not_exists_key", new MyStrClass("a", "b")));
        Assertions.assertEquals(new MyIntClass((byte) 'x'), pm.getPreference("not_exists_key", new MyIntClass((byte) 'x')));

        // test default value from fields
        MyPrefClass myPrefClass = new MyPrefClass();
        Collection<Field> allFields = BeanUtils.getAllFields(myPrefClass.getClass());
        for (Field f : allFields) {
            try {
                Object expect = BeanUtils.getDeclaredField(this, f.getName()).get(this);
                Assertions.assertEquals(expect, pm.getPreference(f.getName(), f, myPrefClass));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Test
    void getPreference() {
        PreferenceManager pm = PreferenceManager.getInstance();
        pm.savePreference(String.class.toString(), vs);
        pm.savePreference(Integer.class.toString(), vi);
        pm.savePreference(Long.class.toString(), vl);
        pm.savePreference(Float.class.toString(), vf);
        pm.savePreference(Double.class.toString(), vd);
        pm.savePreference(byte[].class.toString(), vbs);
        pm.savePreference(Boolean.class.toString(), vb);
        pm.savePreference(MyStrClass.class.toString(), vms);
        pm.savePreference(MyIntClass.class.toString(), vmi);

        // test get preference by key and class type.
        Assertions.assertEquals(vs, pm.getPreference(String.class.toString(), String.class));
        Assertions.assertEquals(vi, pm.getPreference(Integer.class.toString(), Integer.class));
        Assertions.assertEquals(vl, pm.getPreference(Long.class.toString(), Long.class));
        Assertions.assertEquals(vf, pm.getPreference(Float.class.toString(), Float.class));
        Assertions.assertEquals(vd, pm.getPreference(Double.class.toString(), Double.class));
        Assertions.assertEquals(vb, pm.getPreference(Boolean.class.toString(), Boolean.class));
        Assertions.assertArrayEquals(vbs, pm.getPreference(byte[].class.toString(), byte[].class));
        Assertions.assertEquals(vms, pm.getPreference(MyStrClass.class.toString(), new MyStrClassExt()));

        // test get preference by key and class type with default value.
        Assertions.assertEquals(vs, pm.getPreference(String.class.toString(), String.class, ""));
        Assertions.assertEquals(vi, pm.getPreference(Integer.class.toString(), Integer.class, Integer.MIN_VALUE));
        Assertions.assertEquals(vl, pm.getPreference(Long.class.toString(), Long.class, Long.MIN_VALUE));
        Assertions.assertEquals(vf, pm.getPreference(Float.class.toString(), Float.class, Float.MIN_VALUE));
        Assertions.assertEquals(vd, pm.getPreference(Double.class.toString(), Double.class, Double.MIN_VALUE));
        Assertions.assertEquals(vb, pm.getPreference(Boolean.class.toString(), Boolean.class, Boolean.FALSE));
        Assertions.assertArrayEquals(vbs, pm.getPreference(byte[].class.toString(), byte[].class, new byte[]{}));
        Assertions.assertEquals(vms, pm.getPreference(MyStrClass.class.toString(), MyStrClass.class, new MyStrClass()));
        Assertions.assertEquals(vmi, PreferenceManager.getInstance().getPreference(MyIntClass.class.toString(), MyIntClass.class));

        // test get preference by key with default value.
        Assertions.assertEquals(vs, pm.getPreference(String.class.toString(), ""));
        Assertions.assertEquals(vi, pm.getPreference(Integer.class.toString(), Integer.MIN_VALUE));
        Assertions.assertEquals(vl, pm.getPreference(Long.class.toString(), Long.MIN_VALUE));
        Assertions.assertEquals(vf, pm.getPreference(Float.class.toString(), Float.MIN_VALUE));
        Assertions.assertEquals(vd, pm.getPreference(Double.class.toString(), Double.MIN_VALUE));
        Assertions.assertEquals(vb, pm.getPreference(Boolean.class.toString(), Boolean.TRUE));
        Assertions.assertArrayEquals(vbs, (byte[]) pm.getPreference(byte[].class.toString(), new byte[]{}));
        Assertions.assertEquals(vms, pm.getPreference(MyStrClass.class.toString(), new MyStrClass()));
        Assertions.assertEquals(vmi, PreferenceManager.getInstance().getPreference(MyIntClass.class.toString(), MyIntClass.class, new MyIntClass()));

    }

    private static class MyStrClassExt extends MyStrClass {
        public MyStrClassExt() {
        }

        public MyStrClassExt(String param1, String param2) {
            super(param1, param2);
        }

        @Override
        public String toString() {
            return "MyStrClassExt{" +
                    "param1='" + param1 + '\'' +
                    ", param2='" + param2 + '\'' +
                    '}';
        }
    }

    // saved as str
    private static class MyStrClass implements Serializable {
        String param1 = "def";
        String param2 = "ault";

        public MyStrClass() {
        }

        public MyStrClass(String param1, String param2) {
            this.param1 = param1;
            this.param2 = param2;
        }

        public String getParam1() {
            return param1;
        }

        public void setParam1(String param1) {
            this.param1 = param1;
        }

        public String getParam2() {
            return param2;
        }

        public void setParam2(String param2) {
            this.param2 = param2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MyStrClass that = (MyStrClass) o;
            return Objects.equals(param1, that.param1) && Objects.equals(param2, that.param2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(param1, param2);
        }

        @Override
        public String toString() {
            return "MyStrClass{" +
                    "param1='" + param1 + '\'' +
                    ", param2='" + param2 + '\'' +
                    '}';
        }
    }


    private static class MyIntClass implements Serializable {
        byte b;

        public MyIntClass() {
        }

        public MyIntClass(byte b) {
            this.b = b;
        }

        public byte getB() {
            return b;
        }

        public void setB(byte b) {
            this.b = b;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MyIntClass that = (MyIntClass) o;
            return b == that.b;
        }

        @Override
        public int hashCode() {
            return Objects.hash(b);
        }

        @Override
        public String toString() {
            return "MyIntClass{" +
                    "b=" + b +
                    '}';
        }
    }

    private static class MyPrefClass implements Serializable {
        int vi = PreferenceManagerTest.vi;
        long vl = PreferenceManagerTest.vl;
        float vf = PreferenceManagerTest.vf;
        double vd = PreferenceManagerTest.vd;
        byte[] vbs = PreferenceManagerTest.vbs;
        boolean vb = PreferenceManagerTest.vb;
        String vs = PreferenceManagerTest.vs;
    }
}