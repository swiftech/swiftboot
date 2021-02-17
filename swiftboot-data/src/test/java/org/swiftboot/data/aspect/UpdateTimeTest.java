package org.swiftboot.data.aspect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.swiftboot.data.model.dao.CustomizedDao;
import org.swiftboot.data.model.dao.ParentDao;
import org.swiftboot.data.model.dao.ParentDateTimeDao;
import org.swiftboot.data.model.entity.ChildDateTimeEntity;
import org.swiftboot.data.model.entity.ChildEntity;
import org.swiftboot.data.model.entity.ParentDateTimeEntity;
import org.swiftboot.data.model.entity.ParentEntity;
import org.swiftboot.data.model.interceptor.TimeInterceptor;
import org.swiftboot.util.JsonUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author swiftech
 **/
@ExtendWith(SpringExtension.class)
//@DataJpaTest
@SpringBootTest // 不能用 @DataJpaTest 否则 Aspect 无法生效
@Import(UpdateTimeTestConfig.class)
//@ActiveProfiles("mysql")
public class UpdateTimeTest {

//    @Resource
//    TestEntityManager testEntityManager;

    @Resource
    private ParentDao parentDao;

    @Resource
    private ParentDateTimeDao parentDateTimeDao;

    @Resource
    private CustomizedDao customizedDao;

    @Resource
    private PlatformTransactionManager txManager;

    @Resource
    private TimeInterceptor timeInterceptor;

    @BeforeEach
    public void setup() {
//        System.out.println(testEntityManager);
    }

    /**
     * 测试更新时间自动设置
     */
    @Test
    public void testParentWithChildrenInLong() {
        final String[] parentId = {null};

        TransactionTemplate tmpl = new TransactionTemplate(txManager);
        // Prepare data
        tmpl.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                System.out.println("Trans: " + status.isNewTransaction());
                ParentEntity entity = new ParentEntity();
                entity.setName("君父");
                ChildEntity childEntity = new ChildEntity();
                childEntity.setName("臣子");
                entity.setItems(new ArrayList<>());
                entity.getItems().add(childEntity);
                parentDao.save(entity);
                parentId[0] = entity.getId();
            }
        });

        // Update data
        TransactionTemplate tmpl2 = new TransactionTemplate(txManager);
        tmpl2.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                System.out.println("Trans: " + status.isNewTransaction());
                Optional<ParentEntity> optParent = parentDao.findById(parentId[0]);
                Assertions.assertNotNull(optParent);
                Assertions.assertTrue(optParent.isPresent());
                ParentEntity parentEntity = optParent.get();
                // assert auto update time
                parentEntity.setName("逊位");
                List<ChildEntity> items = parentEntity.getItems();
                Assertions.assertEquals(1, items.size());
                items.get(0).setName("告老还乡");
                parentDao.save(parentEntity);
                System.out.println("saved: ");
                System.out.println(JsonUtils.object2PrettyJson(parentEntity));
            }
        });

        // Assert updateTime;
        TransactionTemplate tmpl3 = new TransactionTemplate(txManager);
        tmpl3.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                System.out.println("Trans: " + status.isNewTransaction());
                Optional<ParentEntity> optParent = parentDao.findById(parentId[0]);
                Assertions.assertNotNull(optParent);
                Assertions.assertTrue(optParent.isPresent());
                ParentEntity parentEntity = optParent.get();
                System.out.println("verify: ");
                System.out.println(JsonUtils.object2PrettyJson(parentEntity));
                Assertions.assertNotNull(parentEntity);
                Assertions.assertNotNull(parentEntity.getUpdateTime());
                Assertions.assertEquals("逊位", parentEntity.getName());
                // assert child updateTime
                List<ChildEntity> items = parentEntity.getItems();
                Assertions.assertEquals(1, items.size());
                Assertions.assertNotNull(items.get(0).getUpdateTime());
            }
        });

    }


    /**
     * 测试更新时间自动设置
     */
    @Test
    public void testParentWithChildrenInDateTime() {
        final String[] parentId = {null};

        TransactionTemplate tmpl = new TransactionTemplate(txManager);
        // Prepare data
        tmpl.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                System.out.println("Trans: " + status.isNewTransaction());
                ParentDateTimeEntity entity = new ParentDateTimeEntity();
                entity.setName("君父");
                ChildDateTimeEntity childEntity = new ChildDateTimeEntity();
                childEntity.setName("臣子");
                entity.setItems(new ArrayList<>());
                entity.getItems().add(childEntity);
                parentDateTimeDao.save(entity);
                parentId[0] = entity.getId();
            }
        });

        // Update data
        TransactionTemplate tmpl2 = new TransactionTemplate(txManager);
        tmpl2.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                System.out.println("Trans: " + status.isNewTransaction());
                Optional<ParentDateTimeEntity> optParent = parentDateTimeDao.findById(parentId[0]);
                Assertions.assertNotNull(optParent);
                Assertions.assertTrue(optParent.isPresent());
                ParentDateTimeEntity parentEntity = optParent.get();
                // assert auto update time
                parentEntity.setName("逊位");
                List<ChildDateTimeEntity> items = parentEntity.getItems();
                Assertions.assertEquals(1, items.size());
                items.get(0).setName("告老还乡");
                parentDateTimeDao.save(parentEntity);
                System.out.println("saved: ");
                System.out.println(JsonUtils.object2PrettyJson(parentEntity));
            }
        });

        // Assert updateTime;
        TransactionTemplate tmpl3 = new TransactionTemplate(txManager);
        tmpl3.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                System.out.println("Trans: " + status.isNewTransaction());
                Optional<ParentDateTimeEntity> optParent = parentDateTimeDao.findById(parentId[0]);
                Assertions.assertNotNull(optParent);
                Assertions.assertTrue(optParent.isPresent());
                ParentDateTimeEntity parentEntity = optParent.get();
                System.out.println("verify: ");
                System.out.println(JsonUtils.object2PrettyJson(parentEntity));
                Assertions.assertNotNull(parentEntity);
                Assertions.assertNotNull(parentEntity.getUpdateTime());
                Assertions.assertEquals("逊位", parentEntity.getName());
                // assert child updateTime
                List<ChildDateTimeEntity> items = parentEntity.getItems();
                Assertions.assertEquals(1, items.size());
                Assertions.assertNotNull(items.get(0).getUpdateTime());
            }
        });

    }

    public static void main(String[] args) {
        System.out.println(new Date());
    }
}
