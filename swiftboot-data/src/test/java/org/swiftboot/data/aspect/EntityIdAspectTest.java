package org.swiftboot.data.aspect;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
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
import org.swiftboot.data.model.id.IdGenerator;
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
@ActiveProfiles("id-h2")
public class EntityIdAspectTest {

    private final Logger log = LoggerFactory.getLogger(EntityIdAspectTest.class);

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

    @Resource
    private IdGenerator<ChildEntity> idGenerator;

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
        ParentEntity parent = new ParentEntity();
        parent.setName("君父");
        ChildEntity childEntity = new ChildEntity();
        childEntity.setName("臣子");
        parent.setItems(new ArrayList<>());
        parent.getItems().add(childEntity);
        parentDao.save(parent);
        System.out.println(parent);
        // Assertions
        Optional<ParentEntity> optParent = parentDao.findById(parent.getId());
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

        log.info(" == Create new parent entity == ");
        String parentId = "child20210307142722543tyvlvjaddz";
        String modifyChildId = "child20210307142642072sbjonsfjvw";
        tmpl.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                ParentEntity entity = new ParentEntity(parentId, "Saved Parent");
                parentDao.save(entity);
            }
        });

        log.info(" == Find parent entity and add children to it in another session == ");
        if (true) {
            tmpl.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    Optional<ParentEntity> optParent = parentDao.findById(parentId);
                    if (optParent.isPresent()) {
                        ParentEntity parentEntity = optParent.get();
//                    entityManager.detach(parentEntity);
                        //log.trace("query before adding entity 1");
                        //Optional<ParentEntity> hello1 = parentDao.findByName("hello");// add this to test weired auto flush before save()
                        ChildEntity childEntity1 = new ChildEntity("10001", "New Child 1");
//                    childEntity1.setParent(parentEntity);
                        childEntity1.setParent(parentEntity);
                        parentEntity.getItems().add(childEntity1);
                        //log.trace("query before adding entity 2");
                        //Optional<ParentEntity> hello2 = parentDao.findByName("hello");// add this to test weired auto flush before save()
                        ChildEntity childEntity2 = new ChildEntity(modifyChildId, "New Child 2");
                        childEntity2.setParent(parentEntity);
                        parentEntity.getItems().add(childEntity2);
                        parentDao.save(parentEntity);
                    }
                }
            });
        }

        log.info(" == Find and add new child ==");
        tmpl.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                Optional<ParentEntity> optParent = parentDao.findById(parentId);
                if (optParent.isPresent()) {
                    ParentEntity parentEntity = optParent.get();
                    log.trace("Clear existed children " + parentEntity.getItems().size());
                    parentEntity.getItems().clear();
//                    log.trace("query before adding entity 3");
                    Optional<ParentEntity> hello1 = parentDao.findByName("hello");// add this to test weired auto flush before save()
                    ChildEntity childEntity3 = new ChildEntity("New Child 3");
                    parentEntity.getItems().add(childEntity3);
                    log.trace("query before adding Detached entity 2");
                    Optional<ParentEntity> hello2 = parentDao.findByName("hello");// add this to test weired auto flush before save()
                    ChildEntity modifiedChild2 = new ChildEntity(modifyChildId, "Modified Child 2");
                    parentEntity.getItems().add(modifiedChild2);
                    for (ChildEntity item : parentEntity.getItems()) {
                        System.out.println(item);
                        System.out.println(item.getParent());
                    }
                    parentDao.save(parentEntity);
                }
            }
        });


        // Check result
        log.info(" == Check result == ");
        tmpl.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                Optional<ParentEntity> optParent = parentDao.findById(parentId);
                if (optParent.isPresent()) {
                    ParentEntity parentEntity = optParent.get();
                    System.out.println(JsonUtils.object2PrettyJson(parentEntity));
                    Assertions.assertEquals(2, parentEntity.getItems().size());
                }
            }
        });
    }

}
