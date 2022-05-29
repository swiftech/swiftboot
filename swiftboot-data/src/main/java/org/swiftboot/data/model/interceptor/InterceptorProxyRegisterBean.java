package org.swiftboot.data.model.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Register {@link InterceptorProxy} to Hibernate.
 *
 * @author swiftech
 * @see InterceptorProxy
 * @see IdInterceptor
 * @see TimeInterceptor
 * @see InterceptorLoader
 * @since 2.0.0
 */
public class InterceptorProxyRegisterBean implements HibernatePropertiesCustomizer {

    Logger log = LoggerFactory.getLogger(InterceptorProxyRegisterBean.class);

    @Resource
    private InterceptorProxy interceptorProxy;

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        log.trace("HibernatePropertiesCustomizer goes");
        hibernateProperties.put("hibernate.session_factory.interceptor", interceptorProxy);
        hibernateProperties.put("hibernate.ejb.interceptor", interceptorProxy);
    }
}
