package org.swiftboot.data.model.id;

import org.swiftboot.util.IdUtils;
import org.swiftboot.data.model.entity.IdPersistable;

/**
 * UUID ID 生成器（32字节无连接符）
 *
 * @author swiftech
 **/
public class UuidIdGenerator implements IdGenerator<IdPersistable> {

    @Override
    public String generate(IdPersistable object) {
        return IdUtils.makeUUID();
    }

    @Override
    public String generate(String bizName) {
        return IdUtils.makeUUID();
    }
}
