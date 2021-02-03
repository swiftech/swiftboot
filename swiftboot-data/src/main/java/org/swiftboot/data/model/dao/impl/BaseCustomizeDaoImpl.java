package org.swiftboot.data.model.dao.impl;

import org.swiftboot.data.model.entity.IdPojo;
import org.swiftboot.data.model.entity.Persistent;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * 自定义 Jpa Dao 接口的基类
 *
 * @author swiftech
 **/
public abstract class BaseCustomizeDaoImpl<T extends IdPojo> {

    @PersistenceContext
    protected EntityManager entityManager;

    protected Class<Persistent> entityClass;

    /**
     * 创建一个单个字段查询的 CriteriaQuery 对象
     *
     * @param key
     * @param value
     * @return
     */
    protected CriteriaQuery makeCriteriaQuery(String key, Object value) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> q = (CriteriaQuery<T>) cb.createQuery(entityClass);
        Root<T> from = (Root<T>) q.from(entityClass);
        q.select(from).where(
                cb.equal(from.get(key), value)
        );
        return q;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
