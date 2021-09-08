package org.swiftboot.data.model.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.swiftboot.data.model.entity.LogicalDeletePersistable;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 基于 {@code Integer} 类型的逻辑删除扩展接口
 *
 * @author swiftech
 * @since 2.1.0
 */
public interface IntegerLogicalDeleteExtend<E extends LogicalDeletePersistable<Integer>> {

    @Query("update #{#entityName} e set e.isDelete = 1 where e.id = ?1")
    @Transactional
    @Modifying
    void deleteLogically(String id);

    @Transactional
    default void deleteLogically(E entity) {
        deleteLogically(entity.getId());
    }

    @Query("update #{#entityName} e set e.isDelete = 1 where e.id in ?1")
    @Transactional
    @Modifying
    void deleteLogically(String... ids);

    @Transactional
    default void deleteLogically(E... entities) {
        for (E entity : entities) {
            deleteLogically(entity);
        }
    }

    @Transactional
    default void deleteLogically(List<E> entities) {
        for (E entity : entities) {
            deleteLogically(entity);
        }
    }


    @Query("update #{#entityName} e set e.isDelete = 0 where e.id = ?1")
    @Transactional
    @Modifying
    void undeleteLogically(String id);

    @Transactional
    default void undeleteLogically(E entity) {
        undeleteLogically(entity.getId());
    }

    @Query("update #{#entityName} e set e.isDelete = 0 where e.id in ?1")
    @Transactional
    @Modifying
    void undeleteLogically(String... ids);

    @Transactional
    default void undeleteLogically(E... entities) {
        for (E entity : entities) {
            undeleteLogically(entity);
        }
    }

    @Transactional
    default void undeleteLogically(List<E> entities) {
        for (E entity : entities) {
            undeleteLogically(entity);
        }
    }
}
