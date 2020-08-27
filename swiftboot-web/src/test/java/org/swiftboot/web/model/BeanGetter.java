package org.swiftboot.web.model;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * allen
 */
@Component
public class BeanGetter {

    private ApplicationContext applicationContext;

    //    @Transactional
    public void bl(String daoName) throws ClassNotFoundException {
        Class<?> daoClass = null;
        daoClass = Class.forName(daoName);
        if (daoClass == null) {
            throw new RuntimeException();
        }
        applicationContext.getBean(daoClass);
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("### Application Context in BeLoad: " + applicationContext);
        this.applicationContext = applicationContext;
    }

}
