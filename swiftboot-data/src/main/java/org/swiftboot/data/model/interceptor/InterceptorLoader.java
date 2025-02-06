package org.swiftboot.data.model.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Loader for loading customized interceptors.
 *
 * @author swiftech
 * @see InterceptorRegisterBean
 * @since 2.0.2
 */
public class InterceptorLoader implements ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(InterceptorLoader.class);

    private ApplicationContext applicationContext;

    @Resource
    private InterceptorProxy interceptorProxy;

    @PostConstruct
    public void init() {
        log.trace("InterceptorLoader init user's Hibernate interceptor");
        Map<String, InterceptorRegisterBean> beans = this.applicationContext.getBeansOfType(InterceptorRegisterBean.class);

        // find all registered interceptors.
        List<InterceptorRegisterBean<Interceptor>> list = new ArrayList<>();
        for (String beanName : beans.keySet()) {
            InterceptorRegisterBean<Interceptor> regBean = beans.get(beanName);
            if (StringUtils.isBlank(regBean.getInterceptorName())) {
                regBean.setInterceptorName(beanName);
            }
            list.add(regBean);
        }

        // append interceptors to proxy by order.
        list.sort(Comparator.comparingInt(InterceptorRegisterBean::getOrder));
        for (InterceptorRegisterBean<?> regBean : list) {
            log.debug(String.format("Register interceptor: %s(%d) - %s",
                    regBean.getInterceptorName(), regBean.getOrder(), regBean));
            interceptorProxy.addInterceptor(regBean.getInterceptor());
        }
        interceptorProxy.printDebugInfo();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
