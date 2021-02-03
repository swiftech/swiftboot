package org.swiftboot.data.model.id;

import org.swiftboot.util.IdUtils;
import org.swiftboot.data.model.entity.IdPojo;

/**
 * 默认 ID 生成器
 *
 * @author swiftech
 **/
public class DefaultIdGenerator implements IdGenerator<IdPojo> {

    @Override
    public String generate(IdPojo object) {
        return IdUtils.makeUUID();
    }
}
