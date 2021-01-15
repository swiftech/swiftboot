package org.swiftboot.data.model.id;

import org.apache.commons.lang3.StringUtils;
import org.swiftboot.data.model.entity.IdPojo;
import org.swiftboot.util.IdUtils;
import org.swiftboot.util.WordUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 实体类 ID 生成器
 *
 * @author swiftech
 **/
public class EntityIdGenerator implements IdGenerator<IdPojo> {

    public static final int MIN_CODE_LEN = 2;
    public static final int MAX_CODE_LEN = 6;

    // 类和业务代码映射缓存
    private final Map<Class<? extends IdPojo>, String> classCodeMapping = new HashMap<>();

    @Override
    public String generate(IdPojo object) {
        // 自动根据业务对象名称生成业务对象代码，
        Class<? extends IdPojo> entityClass = object.getClass();
        String code = null;
        if (classCodeMapping.containsKey(entityClass)) {
            code = classCodeMapping.get(entityClass);
        }
        else {
            String simpleEntityName = entityClass.getSimpleName();
            String bizName = StringUtils.substringBefore(simpleEntityName, "Entity");
            if (bizName.length() < MIN_CODE_LEN) {
                code = StringUtils.rightPad(bizName.toLowerCase(), MIN_CODE_LEN, "entity");
            }
            else {
                code = camelToShort(bizName, MAX_CODE_LEN);
            }
            classCodeMapping.put(entityClass, code);
        }
        return IdUtils.makeID(code);
    }

    private String camelToShort(String camel, int len) {
        String[] words = StringUtils.splitByCharacterTypeCamelCase(camel);
        return WordUtils.joinWords(words, len);
    }


}
