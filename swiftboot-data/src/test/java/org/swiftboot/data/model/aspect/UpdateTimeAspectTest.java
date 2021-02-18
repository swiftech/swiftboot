package org.swiftboot.data.model.aspect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.swiftboot.data.model.entity.ParentEntity;

/**
 * 仅测试 UpdateTimeAspect
 *
 * @author allen
 */
public class UpdateTimeAspectTest {

    @Test
    public void testNowTime() {
        UpdateTimeAspect updateTimeAspect = new UpdateTimeAspect();
        Object nowTime = updateTimeAspect.nowByParameterizedType(ParentEntity.class);
        Assertions.assertEquals(Long.class, nowTime.getClass());
        System.out.println(nowTime);
    }
}
