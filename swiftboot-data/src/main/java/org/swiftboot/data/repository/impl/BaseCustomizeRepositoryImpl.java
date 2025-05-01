package org.swiftboot.data.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.swiftboot.data.model.entity.IdPersistable;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

/**
 * 自定义 Jpa Repository 接口的基类
 *
 * @author swiftech
 **/
public abstract class BaseCustomizeRepositoryImpl<T extends IdPersistable> {

    @PersistenceContext
    protected EntityManager entityManager;

    protected Class<T> entityClass;

    /**
     * 创建一个单个字段查询的 CriteriaQuery 对象
     *
     * @param key
     * @param value
     * @return
     */
    protected CriteriaQuery<T> makeCriteriaQuery(String key, Object value) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> from = cq.from(entityClass);
        cq.select(from);
        if (StringUtils.isNotBlank(key) && value != null) {
            cq.where(
                    cb.equal(from.get(key), value)
            );
        }
        return cq;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
