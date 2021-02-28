package org.swiftboot.data.aspect;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.swiftboot.data.model.dao.ChildDao;
import org.swiftboot.data.model.dao.CustomizedDao;
import org.swiftboot.data.model.dao.ParentDao;
import org.swiftboot.data.model.entity.ChildEntity;
import org.swiftboot.data.model.entity.CustomizedEntity;
import org.swiftboot.data.model.entity.ParentEntity;
import org.swiftboot.util.JsonUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
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
    private ChildDao childDao;

    @Resource
    private CustomizedDao customizedDao;

    @Resource
    private EntityManager entityManager;

    @Resource
    private PlatformTransactionManager txManager;

    @BeforeEach
    public void setup() {
//        System.out.println(testEntityManager);
    }


    /**
     * 测试实现 IdPersistable 接口的实体类
     */
    @Test
    public void testIdPersistable() {
        CustomizedEntity ce = new CustomizedEntity();
        ce.setName("异类");
        Optional<CustomizedEntity> opt = customizedDao.findByName("同类");
        customizedDao.save(ce);

        Optional<CustomizedEntity> optCE = customizedDao.findById(ce.getId());
        if (optCE.isPresent()) {
            CustomizedEntity found = optCE.get();
            Assertions.assertTrue(StringUtils.isNotBlank(found.getId()));
            Assertions.assertEquals("异类", found.getName());
            System.out.println(JsonUtils.object2PrettyJson(found));
        }
    }

    /**
     * 测试关联表的 ID 自动生成
     */
    @Test
    @Transactional
    public void testCreateParentWithChildren() {
        System.out.println(parentDao);
        ParentEntity entity = new ParentEntity();
        entity.setName("君父");
        ChildEntity childEntity = new ChildEntity();
        childEntity.setName("臣子");
        entity.setItems(new ArrayList<>());
        entity.getItems().add(childEntity);
        Optional<ParentEntity> xxx = parentDao.findByName("xxx"); // add this to test weired auto flush before save()
        parentDao.save(entity);
        System.out.println(entity.toString());
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
    }

    /**
     * 测试给已经存在父实体新增子实体时的 ID 填充
     */
    @Test
    public void testEditParentWithNewChildren() {
        TransactionTemplate tmpl = new TransactionTemplate(txManager);

        System.out.println("Create new parent entity");
        String pid = "1234567890";
        tmpl.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                ParentEntity entity = new ParentEntity(pid, "Saved Parent");
                parentDao.save(entity);
            }
        });

        System.out.println("Find and modify parent entity in another session");
        tmpl.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                Optional<ParentEntity> optParent = parentDao.findById(pid);
                if (optParent.isPresent()){
                    ParentEntity parentEntity = optParent.get();
                    parentEntity.getItems().clear();
//                    entityManager.detach(parentEntity);
                    ChildEntity childEntity = new ChildEntity("New Child 1");
                    parentEntity.getItems().add(childEntity);
                    Optional<ParentEntity> hello = parentDao.findByName("hello");// add this to test weired auto flush before save()
                    parentDao.save(parentEntity);
                }
            }
        });

        tmpl.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                Optional<ParentEntity> optParent = parentDao.findById(pid);
                if (optParent.isPresent()){
                    System.out.println(JsonUtils.object2PrettyJson(optParent.get()));
                }
            }
        });
    }

}
