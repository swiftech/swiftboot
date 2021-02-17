package org.swiftboot.data.model.interceptor;

import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Register {@code TimeInterceptor} to Hibernate.
 *
 * @author swiftech
 * @see TimeInterceptor
 * @since 2.0.0
 */
public class TimeInterceptorRegisterBean implements HibernatePropertiesCustomizer {

    @Resource
    private TimeInterceptor interceptor;

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put("hibernate.session_factory.interceptor", interceptor);
        hibernateProperties.put("hibernate.ejb.interceptor", interceptor);
    }
}
