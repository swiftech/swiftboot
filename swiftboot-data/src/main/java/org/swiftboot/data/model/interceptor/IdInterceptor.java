package org.swiftboot.data.model.interceptor;

import jakarta.annotation.Resource;
import org.hibernate.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.data.model.entity.IdPersistable;
import org.swiftboot.data.model.id.IdPopulator;

import java.util.Iterator;

/**
 * Interceptor for populating id to entities before Hibernate flushing them.
 *
 * @author swiftech
 * @since 2.0.1
 */
public class IdInterceptor implements Interceptor {

    private static final Logger log = LoggerFactory.getLogger(IdInterceptor.class);

    @Resource
    private IdPopulator idPopulator;

    @Override
    public void preFlush(Iterator entities) {
        if (log.isTraceEnabled()) log.trace("preFlush()");
        try {
            while (entities.hasNext()) {
                Object e = entities.next();
                if (e instanceof IdPersistable entity) {
                    idPopulator.populate(entity, false); // avoid re-populate id for children entities.
                }
            }
        } catch (Exception e) {
            log.warn("Exception throws when trying to set id for entities: " + e.getLocalizedMessage());
        }
    }
}
