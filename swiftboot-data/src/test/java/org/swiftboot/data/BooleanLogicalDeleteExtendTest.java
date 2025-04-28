package org.swiftboot.data;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.swiftboot.data.aspect.EntityIdAspectTestConfig;
import org.swiftboot.data.base.TransactionTester;
import org.swiftboot.data.repository.BoolLogicalDeleteRepository;
import org.swiftboot.data.model.entity.BoolLogicalDeleteEntity;
import org.swiftboot.data.model.entity.ParentEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author swiftech
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Import(EntityIdAspectTestConfig.class)
@ActiveProfiles("id-h2")
//@ActiveProfiles("id-pg")
//@ActiveProfiles("id-mysql")
class BooleanLogicalDeleteExtendTest {

    @Resource
    protected PlatformTransactionManager txManager;

    @Resource
    private BoolLogicalDeleteRepository logicalDeleteDao;

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
                .doTest((Function<List<String>, List<String>>) ids -> {
                    logicalDeleteDao.deleteLogically(ids.get(0));
                    Optional<BoolLogicalDeleteEntity> opt = logicalDeleteDao.findById(ids.get(1));
                    opt.ifPresent(logicalDeleteEntity -> logicalDeleteDao.deleteLogically(logicalDeleteEntity));
                    return ids;
                }).doAssert(deletedAsserts)
                .doTest((Function<List<String>, List<String>>) ids -> {
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
                .doTest((Function<List<String>, List<String>>) param -> {
                    logicalDeleteDao.deleteLogically(param.toArray(new String[]{}));
                    return param;
                }).doAssert(deletedAsserts)
                .doTest((Function<List<String>, List<String>>) ids -> {
                    logicalDeleteDao.undeleteLogically(ids.toArray(new String[]{}));
                    return ids;
                }).doAssert(undeletedAsserts)
                .doTest((Function<List<String>, List<String>>) ids -> {
                    List<BoolLogicalDeleteEntity> entities = logicalDeleteDao.findByIdIn(ids);
                    logicalDeleteDao.deleteLogically(entities);
                    return ids;
                }).doAssert(deletedAsserts)
                .doTest((Function<List<String>, List<String>>) ids -> {
                    List<BoolLogicalDeleteEntity> entities = logicalDeleteDao.findByIdIn(ids);
                    logicalDeleteDao.undeleteLogically(entities);
                    return ids;
                }).doAssert(undeletedAsserts)
                .doTest((Function<List<String>, List<String>>) ids -> {
                    List<BoolLogicalDeleteEntity> entities = logicalDeleteDao.findByIdIn(ids);
                    logicalDeleteDao.deleteLogically(entities.toArray(new BoolLogicalDeleteEntity[]{}));
                    return ids;
                }).doAssert(deletedAsserts)
                .doTest((Function<List<String>, List<String>>) ids -> {
                    List<BoolLogicalDeleteEntity> entities = logicalDeleteDao.findByIdIn(ids);
                    logicalDeleteDao.undeleteLogically(entities.toArray(new BoolLogicalDeleteEntity[]{}));
                    return ids;
                }).doAssert(undeletedAsserts);
    }

    Supplier<List<String>> prepares = () -> {
        BoolLogicalDeleteEntity entity1 = new BoolLogicalDeleteEntity("删除前1");
        BoolLogicalDeleteEntity entity2 = new BoolLogicalDeleteEntity("删除前2");
        logicalDeleteDao.saveAll(Arrays.asList(entity1, entity2));
        return Arrays.asList(entity1.getId(), entity2.getId());
    };

    Consumer<List<String>> deletedAsserts = ids -> {
        for (String id : ids) {
            Optional<BoolLogicalDeleteEntity> opt = logicalDeleteDao.findById(id);
            Assertions.assertTrue(opt.isPresent());
            BoolLogicalDeleteEntity entity = opt.get();
            Assertions.assertTrue(entity.getIsDelete());
        }
    };

    Consumer<List<String>> undeletedAsserts = ids -> {
        for (String id : ids) {
            Optional<BoolLogicalDeleteEntity> opt = logicalDeleteDao.findById(id);
            Assertions.assertTrue(opt.isPresent());
            BoolLogicalDeleteEntity entity = opt.get();
            Assertions.assertFalse(entity.getIsDelete());
        }
    };

}