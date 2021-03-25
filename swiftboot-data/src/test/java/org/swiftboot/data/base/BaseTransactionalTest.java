package org.swiftboot.data.base;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

/**
 * @author swiftech
 */
public abstract class BaseTransactionalTest {

    @Resource
    protected PlatformTransactionManager txManager;

    
    protected void doTestInTransaction(TxTestCallback txTestCallback){
        TransactionTemplate tmpl = new TransactionTemplate(txManager);

        Object preparedData = tmpl.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus status) {
                return txTestCallback.prepareData();
            }
        });

        Object testedData = tmpl.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus status) {
                return txTestCallback.doTest(preparedData);
            }
        });

        tmpl.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus status) {
                txTestCallback.assertResult(testedData);
                return null;
            }
        });
    }


    public static abstract class TxTestCallback {
        /**
         * Prepare test data in new single transaction.
         */
        public Object prepareData(){
            return null;
        }

        /**
         * Do test in new single transaction.
         */
        public abstract Object doTest(Object param);

        /**
         * Assert the result in new single transaction. 
         */
        public abstract void assertResult(Object param);
    }
}
