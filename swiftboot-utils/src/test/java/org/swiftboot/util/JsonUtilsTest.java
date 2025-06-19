package org.swiftboot.util;

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
    void testJsonToObject() {
        JsonObject jsonObject = new JsonObject();
        try {
            String json = JsonUtils.object2Json(jsonObject);
            System.out.println(json);
            JsonObject jsonObject2 = JsonUtils.jsonTo(json, JsonObject.class);
            Assertions.assertEquals(jsonObject.getLocalDate(), jsonObject2.getLocalDate());
            Assertions.assertEquals(jsonObject.getLocalDateTime(), jsonObject2.getLocalDateTime());
        } catch (IOException e) {
            e.printStackTrace();
        }
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


    public static class JsonObject {
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
