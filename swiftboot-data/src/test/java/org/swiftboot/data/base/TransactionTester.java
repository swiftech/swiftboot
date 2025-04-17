package org.swiftboot.data.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 简化一个 @Test 中需要多个事务的处理。
 * 基于 Spring 的事务管理实现
 *
 * @author swiftech
 */
public class TransactionTester {

    private static final Logger log = LoggerFactory.getLogger(TransactionTester.class);

    private final PlatformTransactionManager txManager;

    private final Map<String, Object> context = new HashMap<>();

    public TransactionTester(PlatformTransactionManager txManager) {
        this.txManager = txManager;
    }

    /**
     * 准备数据
     *
     * @param prepares
     * @param param
     * @param <T>
     * @param <R>
     * @return
     */
    public <T, R> TransactionTester prepareData(Function<? super T, ? extends R> prepares, T param) {
        log.trace("prepare data");
        TransactionTemplate tmpl = new TransactionTemplate(txManager);
        Object prepared = tmpl.execute((TransactionCallback<Object>) status -> prepares.apply(param));
        context.put("prepared", prepared);
        return this;
    }

    public <R> TransactionTester prepareData(Supplier<? extends R> prepares) {
        log.trace("prepare data");
        TransactionTemplate tmpl = new TransactionTemplate(txManager);
        Object prepared = tmpl.execute((TransactionCallback<Object>) status -> prepares.get());
        context.put("prepared", prepared);
        return this;
    }

    public <T, R> TransactionTester doTest(Function<? super T, ? extends R> tests) {
        log.trace("run tests");
        TransactionTemplate tmpl = new TransactionTemplate(txManager);
        Object tested = tmpl.execute((TransactionCallback<Object>) status -> tests.apply((T) context.get("prepared")));
        context.put("tested", tested);
        return this;
    }

    public <R> TransactionTester doTest(Supplier<? extends R> tests) {
        log.trace("run tests");
        TransactionTemplate tmpl = new TransactionTemplate(txManager);
        Object tested = tmpl.execute((TransactionCallback<Object>) status -> tests.get());
        context.put("tested", tested);
        return this;
    }

    public <T, R> TransactionTester doAssert(Function<? super T, ? extends R> asserts) {
        log.trace("assert test result");
        TransactionTemplate tmpl = new TransactionTemplate(txManager);
        tmpl.execute((TransactionCallback<Object>) status -> asserts.apply((T) context.get("tested")));
        return this;
    }

    public <T> TransactionTester doAssert(Consumer<? super T> asserts) {
        log.trace("assert test result");
        TransactionTemplate tmpl = new TransactionTemplate(txManager);
        tmpl.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                asserts.accept((T) context.get("tested"));
            }
        });
        return this;
    }

    public <T> void disposeData(Consumer<? super T> disposes) {
        log.trace("dispose test data");
        TransactionTemplate tmpl = new TransactionTemplate(txManager);
        tmpl.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                disposes.accept((T) context.get("prepared"));
            }
        });
    }

}
