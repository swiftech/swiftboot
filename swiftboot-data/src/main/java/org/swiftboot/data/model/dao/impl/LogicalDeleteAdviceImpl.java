package org.swiftboot.data.model.dao.impl;

import org.swiftboot.data.model.dao.LogicalDeleteAdvice;
import org.swiftboot.data.model.entity.LogicalDeletePersistable;

/**
 * @author allen
 */
public class LogicalDeleteAdviceImpl<E extends LogicalDeletePersistable<?>> implements LogicalDeleteAdvice<E> {

    @Override
    public void deleteLogically(E baseEntity) {

    }
}
