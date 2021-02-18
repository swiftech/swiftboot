package org.swiftboot.data.model.id;

import org.swiftboot.util.IdUtils;
import org.swiftboot.data.model.entity.IdPersistable;

/**
 * 默认 ID 生成器
 *
 * @author swiftech
 **/
public class DefaultIdGenerator implements IdGenerator<IdPersistable> {

    @Override
    public String generate(IdPersistable object) {
        return IdUtils.makeUUID();
    }
}
