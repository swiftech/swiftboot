package org.swiftboot.data.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Generic repository that handles entity classes of uncertain types
 *
 * @author swiftech
 * @since 3.1.5
 */
@Repository
public class GenericRepository {

    @PersistenceContext
    protected EntityManager entityManager;

    /**
     * 根据 ID 查找任意实体
     * @param entityClass 实体的类类型 (例如 User.class)
     * @param id 实体的主键
     */
    public <T, ID> Optional<T> findById(Class<T> entityClass, ID id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(entityManager.find(entityClass, id));
    }

    /**
     * 保存或更新任意实体
     * 如果实体没有 ID，则执行 insert；如果有 ID，则执行 update
     */
    public <T> T save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity must not be null");
        }
        // 利用 JPA 的 merge 方法，它会自动判断是 persist(新增) 还是 merge(更新)
        return entityManager.merge(entity);
    }

    /**
     * 查询某个实体类型的全表数据
     */
    public <T> List<T> findAll(Class<T> entityClass) {
        String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
        return entityManager.createQuery(jpql, entityClass).getResultList();
    }

    /**
     * 根据 ID 删除任意实体
     */
    public <T, ID> void deleteById(Class<T> entityClass, ID id) {
        findById(entityClass, id).ifPresent(entity -> entityManager.remove(entity));
    }
}
