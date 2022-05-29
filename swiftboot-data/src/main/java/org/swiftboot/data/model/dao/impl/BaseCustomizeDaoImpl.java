package org.swiftboot.data.model.dao.impl;

import org.apache.commons.lang3.StringUtils;
import org.swiftboot.data.model.entity.IdPersistable;

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
public abstract class BaseCustomizeDaoImpl<T extends IdPersistable> {

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
        CriteriaQuery<T> cq = (CriteriaQuery<T>) cb.createQuery(entityClass);
        Root<T> from = (Root<T>) cq.from(entityClass);
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
