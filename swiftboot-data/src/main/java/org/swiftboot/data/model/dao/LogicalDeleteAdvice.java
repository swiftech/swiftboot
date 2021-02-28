package org.swiftboot.data.model.dao;

import org.swiftboot.data.model.entity.LogicalDeletePersistable;

/**
 * @author allen
 */
public interface LogicalDeleteAdvice<E extends LogicalDeletePersistable<?>> {

    void deleteLogically(E e);
}
