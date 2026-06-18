package org.swiftboot.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
     * Set json node value by path like 'a.b.c'
     *
     * @param node
     * @param path
     * @param value
     * @param <T>
     * @return
     */
    public static <T> JsonNode setByPath(JsonNode node, String path, T value) {
        String[] keys = StringUtils.split(path, '.');
        String latestKey = keys[keys.length - 1];
        JsonNode latestParentNode = _select(node, ArrayUtils.subarray(keys, 0, keys.length - 1), 0);
        if (latestParentNode.isObject()) {
            ObjectNode on = ((ObjectNode) latestParentNode);
            if (value instanceof String s) {
                on.put(latestKey, s);
            }
            else if (value instanceof Number) {
                on.put(latestKey, ((Number) value).longValue());
            }
            else if (value instanceof Boolean) {
                on.put(latestKey, ((Boolean) value).booleanValue());
            }
            else if (value instanceof Double) {
                on.put(latestKey, ((Double) value).doubleValue());
            }
            else if (value instanceof Float) {
                on.put(latestKey, ((Float) value).floatValue());
            }
            else if (value instanceof Integer) {
                on.put(latestKey, ((Integer) value).intValue());
            }
            else if (value instanceof Long) {
                on.put(latestKey, ((Long) value).longValue());
            }
            else if (value instanceof Integer) {
                on.put(latestKey, ((Integer) value).intValue());
            }
            else if (value instanceof Short) {
                on.put(latestKey, ((Short) value).shortValue());
            }
            else if (value instanceof Byte) {
                on.put(latestKey, ((Byte) value).byteValue());
            }
            else {
                throw new RuntimeException(Info.get(JsonUtils.class, R.NO_DATA_FOUND1, ArrayUtils.toString(keys)));
            }
        }
        return node;
    }

    /**
     * 按照格式为 "a.b.c" 的路径递归选中 JSON 树中的子节点。
     *
     * @param rootNode
     * @param path
     * @return
     */
    public static Object select(JsonNode rootNode, String path) {
        String[] keys = StringUtils.split(path, '.');

        log.debug("Select in " + rootNode.toString());

        try {
            JsonNode found = _select(rootNode, keys, 0);
            return found.textValue().trim();
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
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
     * Convert JSON to map, return empty map if fail.
     *
     * @param strJson
     * @return
     * @throws IOException
     */
    public static Map<String, ?> jsonToMapSafe(String strJson) {
        ObjectMapper mapper = getJava8ObjectMapper();
        try {
            return mapper.readValue(strJson, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            log.error(e.getLocalizedMessage());
            return Map.of();
        }
    }

    /**
     * JSON 格式字符串转换为 Map
     *
     * @param strJson
     * @return
     * @throws IOException
     */
    public static Map<String, ?> jsonToMap(String strJson) throws IOException {
        ObjectMapper mapper = getJava8ObjectMapper();
        return mapper.readValue(strJson, new TypeReference<>() {
        });
    }

    public static <T> T jsonToSafe(String strJson, Class<T> type) {
        try {
            return jsonTo(strJson, type);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
            return null;
        }
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

    public static <T> T jsonToSafe(String strJson, TypeReference<T> type) {
        try {
            return jsonTo(strJson, type);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
            return null;
        }
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
    public static String mapToJson(Map<String, ?> map) throws JsonProcessingException {
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
            log.error(e.getLocalizedMessage(), e);
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

