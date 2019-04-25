package org.swiftboot.web.model.id;

import org.swiftboot.util.IdUtils;
import org.swiftboot.web.model.entity.Persistent;

/**
 * 默认 ID 生成器
 *
 * @author Allen 2019-04-16
 **/
public class DefaultIdGenerator implements IdGenerator<Persistent> {

    @Override
    public String generate(Persistent object) {
        return IdUtils.makeUUID();
    }
}
