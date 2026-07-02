package org.swiftboot.web.dto;

import org.springframework.data.domain.Page;
import org.swiftboot.data.model.entity.IdPersistable;

import java.util.List;

/**
 * A base class for paginated list objects that supports automatic population from Spring Data JPA pagination result objects.
 *
 * <code>populateByEntity(E entity)</code> Automatically populate from Spring Data JPA pagination result objects, including nested objects.
 *
 * @param <T>
 * @param <E>
 * @since 3.1.5
 */
public class BasePopulatePageDto<T extends BasePopulateDto<E>, E extends IdPersistable> extends BasePopulateListDto<T, E> {

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 当前页码
     */
    private Integer page;

    /**
     * 每页大小
     */
    private Integer size;

    /**
     * Populate from paged entries.
     *
     * @param page
     */
    public void populateByEntities(Page<E> page) {
        List<E> list = page.getContent().stream().toList();
        super.populateByEntities(list);
        this.page = page.getNumber();
        this.size = page.getSize();
        this.total = page.getTotalElements();
    }

    /**
     * Populate from paged entries with more extra handling.
     *
     * @param page
     * @param populateHandler
     */
    public void populateByEntities(Page<E> page, PopulateHandler<T, E> populateHandler) {
        List<E> list = page.getContent().stream().toList();
        super.populateByEntities(list, populateHandler);
        this.page = page.getNumber();
        this.size = page.getSize();
        this.total = page.getTotalElements();
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
