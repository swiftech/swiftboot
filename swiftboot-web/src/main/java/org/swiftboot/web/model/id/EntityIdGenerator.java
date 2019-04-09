package org.swiftboot.web.model.id;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.swiftboot.util.IdUtils;
import org.swiftboot.web.model.entity.BaseIdEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * 实体类 ID 生成器
 *
 * @author swiftech
 **/
public class EntityIdGenerator implements IdGenerator<BaseIdEntity> {

    public static final int MIN_CODE_LEN = 2;
    public static final int MAX_CODE_LEN = 6;

    // 类和业务代码映射缓存
    private Map<Class, String> classCodeMapping = new HashMap<>();

    @Override
    public String generate(BaseIdEntity object) {
        // 自动根据业务对象名称生成业务对象代码，
        Class<? extends BaseIdEntity> entityClass = object.getClass();
        String code = null;
        if (classCodeMapping.containsKey(entityClass)) {
            code = classCodeMapping.get(entityClass);
        }
        else {
            String simpleEntityName = entityClass.getSimpleName();
            String bizName = StringUtils.substringBefore(simpleEntityName, "Entity");
            String[] words = StringUtils.splitByCharacterTypeCamelCase(bizName);
            int a = MAX_CODE_LEN / words.length;
            int b = MAX_CODE_LEN % words.length;
            StringBuilder buf = new StringBuilder();
            for (int i = 0; i < words.length; i++) {
                int x = a + (i >= (words.length - b) ? 1 : 0);
                String word = words[i];
                buf.append(StringUtils.substring(word, 0, x).toLowerCase());
            }

            if (buf.length() < MIN_CODE_LEN) {
                code = buf.toString() + RandomStringUtils.randomAlphabetic(MIN_CODE_LEN - buf.length());
            }
            else if (buf.length() > MAX_CODE_LEN) {
                code = buf.substring(0, MAX_CODE_LEN); // 如果太长自动截掉
            }
            else {
                code = buf.toString();
            }
        }
        return IdUtils.makeID(code);
    }

}
