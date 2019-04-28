package org.swiftboot.web.model.id;

import org.swiftboot.util.IdUtils;
import org.swiftboot.web.model.entity.IdPojo;

/**
 * 默认 ID 生成器
 *
 * @author Allen 2019-04-16
 **/
public class DefaultIdGenerator implements IdGenerator<IdPojo> {

    @Override
    public String generate(IdPojo object) {
        return IdUtils.makeUUID();
    }
}
