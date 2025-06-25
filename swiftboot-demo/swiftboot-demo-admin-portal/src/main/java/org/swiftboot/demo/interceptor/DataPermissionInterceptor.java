package org.swiftboot.demo.interceptor;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Interceptor to handle data permissions.
 * TODO
 *
 * @author swiftech
 * @since 2.0.2
 */
public class DataPermissionInterceptor extends EmptyInterceptor {

    private static final Logger log = LoggerFactory.getLogger(DataPermissionInterceptor.class);

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        System.out.println("onSave()");
        log.info("onSave()");
        return false;
    }

    @Override
    public int[] findDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        log.info("findDirty");
        return null;
    }

    @Override
    public Boolean isTransient(Object entity) {
        log.info("isTransient");
        return null;
    }
}
