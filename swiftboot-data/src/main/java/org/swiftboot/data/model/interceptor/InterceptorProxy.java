package org.swiftboot.data.model.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.CallbackException;
import org.hibernate.Interceptor;
import org.hibernate.Transaction;
import org.hibernate.metamodel.RepresentationMode;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.collections.ArrayUtils;
import org.swiftboot.util.BooleanUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Proxy of Hibernate interceptors
 *
 * @author swiftech
 * @since 2.0.1
 */
public class InterceptorProxy implements Interceptor, Serializable {

    private static final Logger log = LoggerFactory.getLogger(InterceptorProxy.class);

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
        if (log.isTraceEnabled()) log.trace("Proxy onLoad() %s".formatted(entity));
        boolean isDirty = false;
        for (Interceptor interceptor : interceptors) {
            isDirty = interceptor.onLoad(entity, id, state, propertyNames, types) || isDirty;
        }
        return isDirty;
    }

    /**
     *
     */
    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) throws CallbackException {
        if (log.isTraceEnabled()) log.trace("Proxy onFlushDirty() %s".formatted(entity));
        boolean isDirty = false;
        for (Interceptor interceptor : interceptors) {
            isDirty = interceptor.onFlushDirty(entity, id, currentState, previousState, propertyNames, types) || isDirty;
        }
        return isDirty;
    }

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) throws CallbackException {
        if (log.isTraceEnabled()) log.trace(String.format("Proxy onSave() %s", entity));
        boolean isDirty = false;
        for (Interceptor interceptor : interceptors) {
            isDirty = interceptor.onSave(entity, id, state, propertyNames, types) || isDirty;
        }
        return isDirty;
    }

    @Override
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) throws CallbackException {
        if (log.isTraceEnabled()) log.trace("Proxy onDelete() %s".formatted(entity));
        interceptors.forEach(interceptor -> interceptor.onDelete(entity, id, state, propertyNames, types));
    }

    @Override
    public void onCollectionRecreate(Object collection, Serializable key) throws CallbackException {
        if (log.isTraceEnabled()) log.trace("Proxy onCollectionRecreate() %s".formatted(key));
        interceptors.forEach(interceptor -> interceptor.onCollectionRecreate(collection, key));
    }

    @Override
    public void onCollectionRemove(Object collection, Serializable key) throws CallbackException {
        if (log.isTraceEnabled()) log.trace("Proxy onCollectionRemove() %s".formatted(key));
        interceptors.forEach(interceptor -> interceptor.onCollectionRemove(collection, key));
    }

    @Override
    public void onCollectionUpdate(Object collection, Serializable key) throws CallbackException {
        if (log.isTraceEnabled()) log.trace("Proxy onCollectionUpdate() %s".formatted(key));
        interceptors.forEach(interceptor -> interceptor.onCollectionUpdate(collection, key));
    }

    @Override
    public void preFlush(Iterator entities) throws CallbackException {
        if (log.isTraceEnabled()) log.trace("Proxy preFlush()");
        interceptors.forEach(interceptor -> interceptor.preFlush(entities));
    }

    @Override
    public void postFlush(Iterator entities) throws CallbackException {
        if (log.isTraceEnabled()) log.trace("Proxy postFlush()");
        interceptors.forEach(interceptor -> interceptor.postFlush(entities));
    }

    @Override
    public Boolean isTransient(Object entity) {
        if (log.isTraceEnabled()) log.trace("Proxy isTransient() %s".formatted(entity));
        List<Boolean> bools = interceptors.stream()
                .map(interceptor -> interceptor.isTransient(entity))
                .collect(Collectors.toList());
        Boolean ret = BooleanUtils.or(bools);
        log.trace(String.valueOf(ret));
        return ret;
    }

    @Override
    public int[] findDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        if (log.isTraceEnabled()) log.trace(String.format("Proxy findDirty() %s[%s]", entity, id));
        List<int[]> collect = interceptors.stream()
                .map(interceptor -> interceptor.findDirty(entity, id, currentState, previousState, propertyNames, types))
                .toList();
        int[] ret = null;
        for (int[] ints : collect) {
            ret = ArrayUtils.merge(ret, ints);
        }
        if (org.apache.commons.lang3.ArrayUtils.isNotEmpty(ret)) {
            if (log.isTraceEnabled()) log.trace("dirty: " + StringUtils.join(ret, ','));
        }
        else {
            if (log.isTraceEnabled()) log.trace("no dirty from client");
        }
        return ret;
    }

    @Override
    public Object instantiate(String entityName, RepresentationMode representationMode, Object id) throws CallbackException {
        if (log.isTraceEnabled()) {
            log.trace(String.format("Proxy instantiate() %s[%s]", entityName, id));
            log.trace("this method will never invokes any interceptors");
        }
        return null;
    }

    @Override
    public String getEntityName(Object object) throws CallbackException {
        if (log.isTraceEnabled()) {
            log.trace("Proxy getEntityName() " + object);
            log.trace("this method will never invokes any interceptors");
        }
        return null;
    }

    @Override
    public Object getEntity(String entityName, Serializable id) throws CallbackException {
        if (log.isTraceEnabled()) {
            log.trace(String.format("Proxy getEntity() %s[%s]", entityName, id));
            log.trace("this method will never invokes any interceptors");
        }
        return null;
    }

    @Override
    public void afterTransactionBegin(Transaction tx) {
        if (log.isTraceEnabled()) log.trace("Proxy afterTransactionBegin() ");
        interceptors.forEach(interceptor -> interceptor.afterTransactionBegin(tx));
    }

    @Override
    public void beforeTransactionCompletion(Transaction tx) {
        if (log.isTraceEnabled()) log.trace("Proxy beforeTransactionCompletion()");
        interceptors.forEach(interceptor -> interceptor.beforeTransactionCompletion(tx));
    }

    @Override
    public void afterTransactionCompletion(Transaction tx) {
        if (log.isTraceEnabled()) log.trace("Proxy afterTransactionCompletion()");
        interceptors.forEach(interceptor -> interceptor.afterTransactionCompletion(tx));
    }

//    @Override
//    public String onPrepareStatement(String sql) {
//        log.trace("Proxy onPrepareStatement()");
//        log.trace("this method will never invokes any interceptors");
//        return sql;
//    }

    void printDebugInfo() {
        if (log.isTraceEnabled()) log.trace("%d interceptors in total".formatted(interceptors.size()));
        for (Interceptor interceptor : interceptors) {
            if (log.isTraceEnabled()) log.trace(String.format("Interceptor: %s", interceptor));
        }
    }

}
