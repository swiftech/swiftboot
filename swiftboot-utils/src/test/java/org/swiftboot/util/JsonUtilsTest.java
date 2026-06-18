package org.swiftboot.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author swiftech
 */
public class JsonUtilsTest {

    @Test
    void setByPath() throws JsonProcessingException {
        String json = """
                {
                    "foo": {
                        "bar": 124
                    }
                }
                """;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);
        JsonUtils.setByPath(jsonNode, "foo.bar", 9999);
        Assertions.assertEquals(9999, jsonNode.get("foo").get("bar").asInt());
        JsonUtils.setByPath(jsonNode, "foo.bar.car", 123);
        Assertions.assertEquals(9999, jsonNode.get("foo").get("bar").asInt());
    }

    @Test
    void jsonToMapSafe() {
        String json = """
                {
                    "foo":"bar"
                    "year": 2026
                }
                """;
        Map<String, ?> map = JsonUtils.jsonToMapSafe(json);
        System.out.println(map.get("foo"));
        System.out.println(map.get("year"));
    }

    @Test
    void jsonToObject() {
        MyJsonObject jsonObject = new MyJsonObject();
        try {
            String json = JsonUtils.object2Json(jsonObject);
            System.out.println(json);
            MyJsonObject jsonObject2 = JsonUtils.jsonTo(json, MyJsonObject.class);
            Assertions.assertEquals(jsonObject.getLocalDate(), jsonObject2.getLocalDate());
            Assertions.assertEquals(jsonObject.getLocalDateTime(), jsonObject2.getLocalDateTime());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void jsonToWithUnknownField() throws IOException {
        String json = """
                {
                    "foo": "bar",
                    "car": 124
                }
                """;
        JsonUtils.jsonTo(json, MyJsonObject2.class);
    }

    @Test
    void map2Json() {
        Map<String, Object> map = new HashMap<>() {
            {
                put("key1", "value1");
            }
        };
        String s = JsonUtils.object2JsonSafe(map);
        Assertions.assertEquals("{\"key1\":\"value1\"}", s);
    }

    /**
     *
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MyJsonObject2 {
        private String foo;

        public String getFoo() {
            return foo;
        }

        public void setFoo(String foo) {
            this.foo = foo;
        }
    }

    public static class MyJsonObject {
        private LocalDateTime localDateTime = LocalDateTime.now();
        private LocalDate localDate = LocalDate.now();

        public LocalDateTime getLocalDateTime() {
            return localDateTime;
        }

        public void setLocalDateTime(LocalDateTime localDateTime) {
            this.localDateTime = localDateTime;
        }

        public LocalDate getLocalDate() {
            return localDate;
        }

        public void setLocalDate(LocalDate localDate) {
            this.localDate = localDate;
        }
    }
}
