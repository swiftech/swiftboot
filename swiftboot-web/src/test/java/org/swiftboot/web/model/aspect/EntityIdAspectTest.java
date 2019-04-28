package org.swiftboot.web.model.aspect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.swiftboot.util.JsonUtils;
import org.swiftboot.web.SwiftBootConfiguration;
import org.swiftboot.web.SwiftBootModelConfiguration;
import org.swiftboot.web.model.dao.CustomizedDao;
import org.swiftboot.web.model.dao.ParentDao;
import org.swiftboot.web.model.entity.ChildEntity;
import org.swiftboot.web.model.entity.CustomizedEntity;
import org.swiftboot.web.model.entity.ParentEntity;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Optional;

/**
 * @author swiftech
 **/
@ExtendWith(SpringExtension.class)
//@DataJpaTest
@SpringBootTest // 不能用 @DataJpaTest 否则 Aspect 无法生效
@Import({
        EntityIdAspectTestConfig.class,
        SwiftBootConfiguration.class,
        SwiftBootModelConfiguration.class
})
public class EntityIdAspectTest {

//    @Resource
//    TestEntityManager testEntityManager;

    @Resource
    private ParentDao parentDao;

    @Resource
    private CustomizedDao customizedDao;

    @BeforeEach
    public void setup() {
//        System.out.println(testEntityManager);
    }

    /**
     * 测试关联表的 ID 自动生成
     */
    @Test
    @Transactional
    public void testParentWithChildren() {
        System.out.println(parentDao);
        ParentEntity entity = new ParentEntity();
        entity.setName("君父");
        ChildEntity childEntity = new ChildEntity();
        childEntity.setName("臣子");
        entity.setItems(new HashSet<>());
        entity.getItems().add(childEntity);
        parentDao.save(entity);
        Optional<ParentEntity> optParent = parentDao.findById(entity.getId());
        if (optParent.isPresent()) {
            ParentEntity parentEntity = optParent.get();
            Assertions.assertEquals("君父", parentEntity.getName());
            for (ChildEntity item : parentEntity.getItems()) {
                Assertions.assertEquals("臣子", item.getName());
            }
            System.out.println(parentEntity);
            System.out.println(JsonUtils.object2PrettyJson(parentEntity));
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

        Optional<CustomizedEntity> optCE = customizedDao.findById(ce.getId());
        if (optCE.isPresent()) {
            CustomizedEntity find = optCE.get();
            Assertions.assertEquals("异类", find.getName());
            System.out.println(JsonUtils.object2PrettyJson(find));
        }
    }
}
