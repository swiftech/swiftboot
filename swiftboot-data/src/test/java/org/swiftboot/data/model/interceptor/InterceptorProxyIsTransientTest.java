package org.swiftboot.data.model.interceptor;

import org.hibernate.EmptyInterceptor;
import org.hibernate.Interceptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author swiftech
 */
public class InterceptorProxyIsTransientTest {


    Interceptor nullInterceptor = new EmptyInterceptor() {
        @Override
        public Boolean isTransient(Object entity) {
            return null;
        }
    };

    Interceptor falseInterceptor = new EmptyInterceptor() {
        @Override
        public Boolean isTransient(Object entity) {
            return Boolean.FALSE;
        }
    };

    Interceptor trueInterceptor = new EmptyInterceptor() {
        @Override
        public Boolean isTransient(Object entity) {
            return Boolean.TRUE;
        }
    };

    @Test
    public void testIsTransient() {
        InterceptorProxy interceptorProxy = new InterceptorProxy();
        interceptorProxy.addInterceptor(nullInterceptor);
        Assertions.assertNull(interceptorProxy.isTransient(null));

        interceptorProxy.addInterceptor(falseInterceptor);
        Assertions.assertEquals(Boolean.FALSE, interceptorProxy.isTransient(null));

        interceptorProxy.addInterceptor(trueInterceptor);
        Assertions.assertEquals(Boolean.TRUE, interceptorProxy.isTransient(null));

    }
}
