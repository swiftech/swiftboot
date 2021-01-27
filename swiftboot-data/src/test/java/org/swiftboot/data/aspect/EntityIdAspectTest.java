package org.swiftboot.data.aspect;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.swiftboot.util.JsonUtils;
import org.swiftboot.data.model.dao.CustomizedDao;
import org.swiftboot.data.model.dao.ParentDao;
import org.swiftboot.data.model.entity.ChildEntity;
import org.swiftboot.data.model.entity.CustomizedEntity;
import org.swiftboot.data.model.entity.ParentEntity;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Optional;

/**
 * @author swiftech
 **/
@ExtendWith(SpringExtension.class)
//@DataJpaTest
@SpringBootTest // 不能用 @DataJpaTest 否则 Aspect 无法生效
@Import(EntityIdAspectTestConfig.class)
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
        entity.setItems(new ArrayList<>());
        entity.getItems().add(childEntity);
        parentDao.save(entity);
        // Assertions
        Optional<ParentEntity> optParent = parentDao.findById(entity.getId());
        Assertions.assertNotNull(optParent);
        Assertions.assertTrue(optParent.isPresent());
        ParentEntity parentEntity = optParent.get();
        Assertions.assertNotEquals(null, parentEntity);
        Assertions.assertFalse(StringUtils.isBlank(parentEntity.getId()));
        Assertions.assertEquals("君父", parentEntity.getName());
        for (ChildEntity item : parentEntity.getItems()) {
            Assertions.assertFalse(StringUtils.isBlank(item.getId()));
            Assertions.assertEquals("臣子", item.getName());
        }
        System.out.println(JsonUtils.object2PrettyJson(parentEntity));
        // assert auto update time
        parentEntity.setName("逊位");
        parentDao.save(parentEntity);
        Optional<ParentEntity> optUpdatedEntity = parentDao.findById(parentEntity.getId());
        Assertions.assertTrue(optUpdatedEntity.isPresent());
        ParentEntity updatedEntity = optUpdatedEntity.get();
        Assertions.assertNotNull(updatedEntity);
        Assertions.assertNotNull(updatedEntity.getUpdateTime());
        Assertions.assertEquals("逊位", updatedEntity.getName());
        System.out.println(parentEntity);
        System.out.println(JsonUtils.object2PrettyJson(parentEntity));
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
