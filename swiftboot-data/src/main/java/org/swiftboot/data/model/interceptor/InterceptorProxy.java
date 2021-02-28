package org.swiftboot.data.model.interceptor;

import org.hibernate.CallbackException;
import org.hibernate.EntityMode;
import org.hibernate.Interceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Proxy of Hibernate interceptors
 *
 * @author swiftech
 * @since 2.0.1
 */
public class InterceptorProxy implements Interceptor, Serializable {

    private final Logger log = LoggerFactory.getLogger(InterceptorProxy.class);

    /**
     * All interceptors
     */
    private List<Interceptor> interceptors;

    public void addInterceptor(Interceptor interceptor) {
        if (interceptors == null) {
            interceptors = new ArrayList<>();
        }
        interceptors.add(interceptor);
    }

    @Override
    public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) throws CallbackException {
        log.trace("Proxy onLoad()");
        return false;
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) throws CallbackException {
        log.trace("Proxy onFlushDirty()");
        boolean isDirty = false;
        for (Interceptor interceptor : interceptors) {
            isDirty = isDirty || interceptor.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
        }
        return isDirty;
    }

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) throws CallbackException {
        log.trace("Proxy onSave()");
        boolean isDirty = false;
        for (Interceptor interceptor : interceptors) {
            isDirty = isDirty || interceptor.onSave(entity, id, state, propertyNames, types);
        }
        return isDirty;
    }

    @Override
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) throws CallbackException {
        log.trace("Proxy onDelete()");
    }

    @Override
    public void onCollectionRecreate(Object collection, Serializable key) throws CallbackException {
        log.trace("Proxy onCollectionRecreate()");
    }

    @Override
    public void onCollectionRemove(Object collection, Serializable key) throws CallbackException {
        log.trace("Proxy onCollectionRemove()");
    }

    @Override
    public void onCollectionUpdate(Object collection, Serializable key) throws CallbackException {
        log.trace("Proxy onCollectionUpdate()");
    }

    @Override
    public void preFlush(Iterator entities) throws CallbackException {
        log.trace("Proxy preFlush()");
        interceptors.forEach(interceptor -> interceptor.preFlush(entities));
    }

    @Override
    public void postFlush(Iterator entities) throws CallbackException {
        log.trace("Proxy postFlush()");
        interceptors.forEach(interceptor -> interceptor.postFlush(entities));
    }

    @Override
    public Boolean isTransient(Object entity) {
        log.trace("Proxy isTransient()");
        return null;
    }

    @Override
    public int[] findDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        log.trace("Proxy findDirty()");
        return null;
    }

    @Override
    public Object instantiate(String entityName, EntityMode entityMode, Serializable id) throws CallbackException {
        log.trace("Proxy instantiate()");
        return null;
    }

    @Override
    public String getEntityName(Object object) throws CallbackException {
        log.trace("Proxy getEntityName()");
        return null;
    }

    @Override
    public Object getEntity(String entityName, Serializable id) throws CallbackException {
        log.trace("Proxy getEntity()");
        return null;
    }

    @Override
    public void afterTransactionBegin(Transaction tx) {
        log.trace("Proxy afterTransactionBegin()");
    }

    @Override
    public void beforeTransactionCompletion(Transaction tx) {
        log.trace("Proxy beforeTransactionCompletion()");
    }

    @Override
    public void afterTransactionCompletion(Transaction tx) {
        log.trace("Proxy afterTransactionCompletion()");
    }

    @Override
    public String onPrepareStatement(String sql) {
        log.trace("Proxy onPrepareStatement()");
        return null;
    }

    public List<Interceptor> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(List<Interceptor> interceptors) {
        this.interceptors = interceptors;
    }
}
