package org.swiftboot.data.model.interceptor;

import org.hibernate.Interceptor;
import org.springframework.core.Ordered;

/**
 * Register bean for Hibernate interceptors.
 *
 * @author swiftech
 * @see InterceptorProxy
 */
public class InterceptorRegisterBean<T extends Interceptor> implements Ordered {

    /**
     * Interceptor bean to be registered.
     */
    private Interceptor interceptor;

    /**
     * Order in all customized interceptors.
     */
    private int order = Ordered.LOWEST_PRECEDENCE;

    /**
     * Name of interceptor bean.
     */
    private String interceptorName;

    public InterceptorRegisterBean() {
    }

    public InterceptorRegisterBean(Interceptor interceptor) {
        this.interceptor = interceptor;
    }

    public Interceptor getInterceptor() {
        return interceptor;
    }

    public void setInterceptor(Interceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getInterceptorName() {
        return interceptorName;
    }

    public void setInterceptorName(String interceptorName) {
        this.interceptorName = interceptorName;
    }
}
