package org.swiftboot.data.model.interceptor;

import org.hibernate.EmptyInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.data.model.entity.IdPersistable;
import org.swiftboot.data.model.id.IdPopulator;

import javax.annotation.Resource;
import java.util.Iterator;

/**
 * @author allen
 * @since 2.0.1
 */
public class IdInterceptor extends EmptyInterceptor {

    private final Logger log = LoggerFactory.getLogger(IdInterceptor.class);

    @Resource
    private IdPopulator idPopulater;

    @Override
    public void preFlush(Iterator entities) {
        log.trace("preFlush()");
        while (entities.hasNext()) {
            Object e = entities.next();
            if (e instanceof IdPersistable) {
                IdPersistable entity = (IdPersistable) e;
                idPopulater.populate(entity);
            }
        }
    }
}
