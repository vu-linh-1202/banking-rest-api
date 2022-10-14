package com.hcl.bankingrestapi.general.service;

import com.hcl.bankingrestapi.general.entity.BaseAdditionalFields;
import com.hcl.bankingrestapi.general.entity.BaseEntity;
import com.hcl.bankingrestapi.general.enums.GenErrorMessage;
import com.hcl.bankingrestapi.general.exception.ItemNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public abstract class BaseEntityService<E extends BaseEntity, D extends JpaRepository<E, Long>> {

    private final D dao;
    private static final Integer DEFAULT_PAGE = 0;
    private static final Integer DEFAULT_SIZE = 10;

    public D getDao() {
        return dao;
    }

    public List<E> findAll() {
        return dao.findAll();
    }

    public Optional<E> findById(Long id) {
        return dao.findById(id);
    }

    public E save(E entity) {
        setAdditionalFields(entity);
        entity = dao.save(entity);
        return entity;
    }

    /**
     * @param entity
     */
    private void setAdditionalFields(E entity) {
        BaseAdditionalFields baseAdditionalFields = entity.getBaseAdditionalFields();
        if (baseAdditionalFields == null) {
            baseAdditionalFields = new BaseAdditionalFields();
            entity.setBaseAdditionalFields(baseAdditionalFields);
        }
        if (entity.getId() == null) {
            baseAdditionalFields.setCreateDate(new Date());
        }
        baseAdditionalFields.setUpdateDate(new Date());
    }

    public void delete(E entity) {
        dao.delete(entity);
    }

    public E getByIdWithControl(Long id) {
        Optional<E> entityOptional = findById(id);
        E entity;
        if (entityOptional.isPresent()) {
            entity = entityOptional.get();
        } else {
            throw new ItemNotFoundException(GenErrorMessage.ITEM_NOT_FOUND);
        }
        return entity;
    }

    protected PageRequest getPageRequest(Optional<Integer> pageOptional,
                                         Optional<Integer> sizeOptional) {
        Integer page = getPage(pageOptional);
        Integer size = getSize(sizeOptional);

        PageRequest pageRequest = PageRequest.of(page, size);
        return pageRequest;
    }

    /**
     * @param sizeOptional
     * @return
     */
    protected Integer getSize(Optional<Integer> sizeOptional) {
        Integer size = DEFAULT_SIZE;
        if (sizeOptional.isPresent()) {
            size = sizeOptional.get();
        }
        return size;
    }

    /**
     * @param pageOptional
     * @return
     */
    private Integer getPage(Optional<Integer> pageOptional) {
        Integer page = DEFAULT_PAGE;
        if (pageOptional.isPresent()) {
            page = pageOptional.get();
        }
        return page;
    }

    public boolean existById(Long id) {
        return dao.existsById(id);
    }
}
