package com.triippztech.freshtrade.service;

import com.triippztech.freshtrade.domain.*; // for static metamodels
import com.triippztech.freshtrade.domain.ItemToken;
import com.triippztech.freshtrade.repository.ItemTokenRepository;
import com.triippztech.freshtrade.service.criteria.ItemTokenCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ItemToken} entities in the database.
 * The main input is a {@link ItemTokenCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ItemToken} or a {@link Page} of {@link ItemToken} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ItemTokenQueryService extends QueryService<ItemToken> {

    private final Logger log = LoggerFactory.getLogger(ItemTokenQueryService.class);

    private final ItemTokenRepository itemTokenRepository;

    public ItemTokenQueryService(ItemTokenRepository itemTokenRepository) {
        this.itemTokenRepository = itemTokenRepository;
    }

    /**
     * Return a {@link List} of {@link ItemToken} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ItemToken> findByCriteria(ItemTokenCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ItemToken> specification = createSpecification(criteria);
        return itemTokenRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ItemToken} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ItemToken> findByCriteria(ItemTokenCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ItemToken> specification = createSpecification(criteria);
        return itemTokenRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ItemTokenCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ItemToken> specification = createSpecification(criteria);
        return itemTokenRepository.count(specification);
    }

    /**
     * Function to convert {@link ItemTokenCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ItemToken> createSpecification(ItemTokenCriteria criteria) {
        Specification<ItemToken> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ItemToken_.id));
            }
            if (criteria.getTokenName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTokenName(), ItemToken_.tokenName));
            }
            if (criteria.getTokenCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTokenCode(), ItemToken_.tokenCode));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), ItemToken_.createdDate));
            }
            if (criteria.getUpdatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedDate(), ItemToken_.updatedDate));
            }
            if (criteria.getOwnerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOwnerId(), root -> root.join(ItemToken_.owner, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getItemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getItemId(), root -> root.join(ItemToken_.item, JoinType.LEFT).get(Item_.id))
                    );
            }
        }
        return specification;
    }
}
