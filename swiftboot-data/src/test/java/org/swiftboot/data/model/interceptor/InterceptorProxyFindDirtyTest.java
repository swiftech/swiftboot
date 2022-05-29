package org.swiftboot.data.model.interceptor;

import org.hibernate.EmptyInterceptor;
import org.hibernate.Interceptor;
import org.hibernate.type.Type;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

/**
 * @author swiftech
 */
public class InterceptorProxyFindDirtyTest {
    Interceptor nullInterceptor = new EmptyInterceptor() {
        @Override
        public int[] findDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
            return null;
        }
    };

    Interceptor emptyInterceptor = new EmptyInterceptor() {
        @Override
        public int[] findDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
            return new int[]{};
        }
    };

    Interceptor arrayInterceptor = new EmptyInterceptor() {
        @Override
        public int[] findDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
            return new int[]{1, 3, 5};
        }
    };

    Interceptor arrayInterceptor2 = new EmptyInterceptor() {
        @Override
        public int[] findDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
            return new int[]{2, 4};
        }
    };

    @Test
    public void testFindDirty() {
        InterceptorProxy interceptorProxy = new InterceptorProxy();
        interceptorProxy.addInterceptor(nullInterceptor);
        int[] expectNull = interceptorProxy.findDirty(null, null, null, null, null, null);
        Assertions.assertNull(expectNull);

        interceptorProxy.addInterceptor(emptyInterceptor);
        int[] expectEmpty = interceptorProxy.findDirty(null, null, null, null, null, null);
        Assertions.assertNotNull(expectEmpty);
        Assertions.assertArrayEquals(new int[]{}, expectEmpty);

        interceptorProxy.addInterceptor(arrayInterceptor);
        int[] expectNonEmpty = interceptorProxy.findDirty(null, null, null, null, null, null);
        Assertions.assertNotNull(expectNonEmpty);
        Assertions.assertArrayEquals(new int[]{1, 3, 5}, expectNonEmpty);

        interceptorProxy.addInterceptor(arrayInterceptor2);
        int[] expectMerge = interceptorProxy.findDirty(null, null, null, null, null, null);
        Assertions.assertNotNull(expectMerge);
        Assertions.assertArrayEquals(new int[]{1, 3, 5, 2, 4}, expectMerge);
    }

}
