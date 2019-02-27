package org.github.swiftech.swiftboot.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * 依赖 Jackson 库
 *
 * @author swiftech 2015-02-16
 */
public class JsonUtils {

    static Logger log = LoggerFactory.getLogger(JsonUtils.class);

    /**
     * 按照格式"xxx.xxx.xxx"递归选中 JSON 树中的子节点
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
            throw new RuntimeException("No data found for: " + ArrayUtils.toString(keys));
        }
        return _select(curNode, keys, idx);
    }

    /**
     * @param jsonResult
     * @return
     * @throws IOException
     */
    public static Map jsonToMap(String jsonResult) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonResult, new TypeReference<Map>() {
        });
    }

    /**
     * @param jsonResult
     * @param type
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T jsonTo(String jsonResult, Class<T> type) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonResult, type);
    }

    public static <T> T jsonTo(String jsonResult, TypeReference type) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonResult, type);
    }

    public static String mapToJson(Map map) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(map);
    }

    /**
     * 把对象转换为 JSON 字符串
     * @param obj
     * @return
     * @throws IOException
     */
    public static String object2Json(Object obj) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

    /**
     * 转换为格式化过的 JSON 格式
     * @param obj
     * @return
     * @throws IOException
     */
    public static String object2PrettyJson(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
    }
}

