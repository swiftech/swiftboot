package org.swiftboot.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * 依赖 Jackson 库
 *
 * @author swiftech
 */
public class JsonUtils {

    static Logger log = LoggerFactory.getLogger(JsonUtils.class);

    /**
     * 按照格式 "xxx.xxx.xxx" 递归选中 JSON 树中的子节点
     *
     * @param rootNode
     * @param selector
     * @return
     */
    public static Object select(JsonNode rootNode, String selector) {
        String[] keys = StringUtils.split(selector, '.');

        log.debug("Select in " + rootNode.toString());

        try {
            JsonNode found = _select(rootNode, keys, 0);
            return found.textValue().trim();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static JsonNode _select(JsonNode curNode, String[] keys, int idx) {
        if (idx >= keys.length) {
            return curNode;// Already last one, just return null.
        }
        log.debug("  select " + keys[idx]);
        curNode = curNode.path(keys[idx++]);
        if (curNode.isNull() || curNode.isMissingNode()) {
            throw new RuntimeException(Info.get(JsonUtils.class, R.NO_DATA_FOUND1, ArrayUtils.toString(keys)));
        }
        return _select(curNode, keys, idx);
    }

    /**
     * JSON 格式字符串转换为 Map
     *
     * @param strJson
     * @return
     * @throws IOException
     */
    public static Map jsonToMap(String strJson) throws IOException {
        ObjectMapper mapper = getJava8ObjectMapper();
        return mapper.readValue(strJson, new TypeReference<Map>() {
        });
    }

    /**
     * JSON 格式字符串转换为指定类型对象
     *
     * @param strJson
     * @param type    目标对象类型
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T jsonTo(String strJson, Class<T> type) throws IOException {
        ObjectMapper mapper = getJava8ObjectMapper();
        return mapper.readValue(strJson, type);
    }

    /**
     * JSON 格式字符串转换为指定类型对象
     *
     * @param strJson
     * @param type    目标对象类型
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T jsonTo(String strJson, TypeReference<T> type) throws IOException {
        ObjectMapper mapper = getJava8ObjectMapper();
        return mapper.readValue(strJson, type);
    }

    /**
     * Map 转换为 JSON 格式字符串
     *
     * @param map
     * @return
     * @throws JsonProcessingException
     * @deprecated to object2Json
     */
    public static String mapToJson(Map map) throws JsonProcessingException {
        ObjectMapper mapper = getJava8ObjectMapper();
        return mapper.writeValueAsString(map);
    }

    /**
     * 把对象转换为 JSON 格式字符串
     *
     * @param obj
     * @return
     * @throws IOException
     */
    public static String object2Json(Object obj) throws IOException {
        ObjectMapper mapper = getJava8ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

    /**
     * 把对象转换为 JSON 格式字符串，错误的话不会抛出异常，而是返回错误信息
     *
     * @param obj
     * @return
     * @since 3.0
     */
    public static String object2JsonSafe(Object obj) {
        ObjectMapper mapper = getJava8ObjectMapper();
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return e.getLocalizedMessage();
        }
    }

    /**
     * 转换为格式化过的 JSON 格式字符串
     *
     * @param obj
     * @return
     */
    public static String object2PrettyJson(Object obj) {
        ObjectMapper mapper = getJava8ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
    }

    /**
     * As of the Java 8, the time implementation has been changed wit new classes.
     *
     * @return
     */
    public static ObjectMapper getJava8ObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}

