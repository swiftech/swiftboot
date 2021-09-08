package org.swiftboot.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.swiftboot.data.aspect.EntityIdAspectTestConfig;
import org.swiftboot.data.base.BaseTransactionalTest;
import org.swiftboot.data.base.TransactionTester;
import org.swiftboot.data.base.Asserts;
import org.swiftboot.data.base.Prepares;
import org.swiftboot.data.base.Tests;
import org.swiftboot.data.model.dao.BoolLogicalDeleteDao;
import org.swiftboot.data.model.entity.BoolLogicalDeleteEntity;
import org.swiftboot.data.model.entity.ParentEntity;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author swiftech
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Import(EntityIdAspectTestConfig.class)
@ActiveProfiles("id-mysql")
class BooleanLogicalDeleteExtendTest extends BaseTransactionalTest {

    @Resource
    BoolLogicalDeleteDao logicalDeleteDao;

    @Test
    void testNonDeleteLogicallyEntity() {
        ParentEntity nonDeleteLogicallyEntity = new ParentEntity();
        logicalDeleteDao.deleteLogically(nonDeleteLogicallyEntity.getId());
    }

    @Test
    void deleteLogicallySingle() {
        TransactionTester transactionTester = new TransactionTester(txManager);
        transactionTester
                .prepareData(prepares)
                .doTest((Tests<List<String>, List<String>>) ids -> {
                    logicalDeleteDao.deleteLogically(ids.get(0));
                    Optional<BoolLogicalDeleteEntity> opt = logicalDeleteDao.findById(ids.get(1));
                    opt.ifPresent(logicalDeleteEntity -> logicalDeleteDao.deleteLogically(logicalDeleteEntity));
                    return ids;
                }).doAssert(deletedAsserts)
                .doTest((Tests<List<String>, List<String>>) ids -> {
                    for (String id : ids) {
                        logicalDeleteDao.undeleteLogically(id);
                    }
                    return ids;
                }).doAssert(undeletedAsserts);
    }

    @Test
    void testDeleteLogicallyMulti() {
        TransactionTester transactionTester = new TransactionTester(txManager);
        transactionTester
                .prepareData(prepares)
                .doTest((Tests<List<String>, List<String>>) param -> {
                    logicalDeleteDao.deleteLogically(param.toArray(new String[]{}));
                    return param;
                }).doAssert(deletedAsserts)
                .doTest((Tests<List<String>, List<String>>) ids -> {
                    logicalDeleteDao.undeleteLogically(ids.toArray(new String[]{}));
                    return ids;
                }).doAssert(undeletedAsserts)
                .doTest((Tests<List<String>, List<String>>) ids -> {
                    List<BoolLogicalDeleteEntity> entities = logicalDeleteDao.findByIdIn(ids);
                    logicalDeleteDao.deleteLogically(entities);
                    return ids;
                }).doAssert(deletedAsserts)
                .doTest((Tests<List<String>, List<String>>) ids -> {
                    List<BoolLogicalDeleteEntity> entities = logicalDeleteDao.findByIdIn(ids);
                    logicalDeleteDao.undeleteLogically(entities);
                    return ids;
                }).doAssert(undeletedAsserts)
                .doTest((Tests<List<String>, List<String>>) ids -> {
                    List<BoolLogicalDeleteEntity> entities = logicalDeleteDao.findByIdIn(ids);
                    logicalDeleteDao.deleteLogically(entities.toArray(new BoolLogicalDeleteEntity[]{}));
                    return ids;
                }).doAssert(deletedAsserts)
                .doTest((Tests<List<String>, List<String>>) ids -> {
                    List<BoolLogicalDeleteEntity> entities = logicalDeleteDao.findByIdIn(ids);
                    logicalDeleteDao.undeleteLogically(entities.toArray(new BoolLogicalDeleteEntity[]{}));
                    return ids;
                }).doAssert(undeletedAsserts);
    }

    Prepares<List<String>> prepares = () -> {
        BoolLogicalDeleteEntity entity1 = new BoolLogicalDeleteEntity("删除前1");
        BoolLogicalDeleteEntity entity2 = new BoolLogicalDeleteEntity("删除前2");
        logicalDeleteDao.saveAll(Arrays.asList(entity1, entity2));
        return Arrays.asList(entity1.getId(), entity2.getId());
    };

    Asserts<List<String>> deletedAsserts = ids -> {
        for (String id : ids) {
            Optional<BoolLogicalDeleteEntity> opt = logicalDeleteDao.findById(id);
            Assertions.assertTrue(opt.isPresent());
            BoolLogicalDeleteEntity entity = opt.get();
            Assertions.assertTrue(entity.getIsDelete());
        }
    };

    Asserts<List<String>> undeletedAsserts = ids -> {
        for (String id : ids) {
            Optional<BoolLogicalDeleteEntity> opt = logicalDeleteDao.findById(id);
            Assertions.assertTrue(opt.isPresent());
            BoolLogicalDeleteEntity entity = opt.get();
            Assertions.assertFalse(entity.getIsDelete());
        }
    };

}