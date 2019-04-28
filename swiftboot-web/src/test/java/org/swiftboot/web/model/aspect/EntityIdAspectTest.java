package org.swiftboot.web.model.aspect;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.swiftboot.web.SwiftbootWebConfig;
import org.swiftboot.web.model.dao.CustomizedDao;
import org.swiftboot.web.model.dao.ParentDao;
import org.swiftboot.web.model.entity.CustomizedEntity;
import org.swiftboot.web.model.entity.ParentEntity;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * TODO 无法执行 Aspect ，所以目前无法测试
 *
 * @author Allen 2019-04-17
 **/
@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import(SwiftbootWebConfig.class)
public class EntityIdAspectTest {

    @Resource
    TestEntityManager testEntityManager;

    @Resource
    private ParentDao parentDao;

    @Resource
    private CustomizedDao customizedDao;

    @BeforeEach
    public void setup() {
        System.out.println(testEntityManager);
    }

    /**
     * 测试关联表的 ID 自动生成
     */
    @Test
    public void testParentWithChildren() {
        System.out.println(parentDao);
        ParentEntity entity = new ParentEntity();
        entity.setName("君父");
        parentDao.save(entity);
        Optional<ParentEntity> optParent = parentDao.findById("hello");
        if (optParent.isPresent()) {
            System.out.println(optParent.get());
        }
    }

    /**
     * 测试未继承 BaseIdEntity 的实体类
     */
    @Test
    public void testIdPojo() {
        CustomizedEntity ce = new CustomizedEntity();
        ce.setName("异类");
        customizedDao.save(ce);

    }
}
