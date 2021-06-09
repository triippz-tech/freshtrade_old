package com.triippztech.freshtrade.service;

import com.triippztech.freshtrade.domain.*; // for static metamodels
import com.triippztech.freshtrade.domain.Item;
import com.triippztech.freshtrade.repository.ItemRepository;
import com.triippztech.freshtrade.service.criteria.ItemCriteria;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.criteria.JoinType;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Item} entities in the database.
 * The main input is a {@link ItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Item} or a {@link Page} of {@link Item} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ItemQueryService extends QueryService<Item> {

    private final Logger log = LoggerFactory.getLogger(ItemQueryService.class);

    private final ItemRepository itemRepository;

    public ItemQueryService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    /**
     * Return a {@link List} of {@link Item} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Item> findByCriteria(ItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Item> specification = createSpecification(criteria);
        return itemRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Item} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Item> findByCriteria(ItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Item> specification = createSpecification(criteria);
        return itemRepository.findAll(specification, page);
    }

    /**
     * Return a {@link Page} of {@link Item} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Item> findByCriteriaEagerLoad(ItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Item> specification = createSpecification(criteria);
        var found = itemRepository.findAll(specification, page);
        return new PageImpl<>(
            found
                .getContent()
                .stream()
                .map(
                    key -> {
                        Hibernate.initialize(key.getCategories());
                        Hibernate.initialize(key.getImages());
                        Hibernate.initialize(key.getLocation());
                        Hibernate.initialize(key.getTradeEvent());
                        return key;
                    }
                )
                .collect(Collectors.toList()),
            page,
            found.getTotalElements()
        );
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Item> specification = createSpecification(criteria);
        return itemRepository.count(specification);
    }

    /**
     * Function to convert {@link ItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Item> createSpecification(ItemCriteria criteria) {
        Specification<Item> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Item_.id));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Item_.price));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), Item_.quantity));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Item_.name));
            }
            if (criteria.getItemCondition() != null) {
                specification = specification.and(buildSpecification(criteria.getItemCondition(), Item_.itemCondition));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), Item_.isActive));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Item_.createdDate));
            }
            if (criteria.getUpdatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedDate(), Item_.updatedDate));
            }
            if (criteria.getImagesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getImagesId(), root -> root.join(Item_.images, JoinType.LEFT).get(Image_.id))
                    );
            }
            if (criteria.getTokensId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTokensId(), root -> root.join(Item_.tokens, JoinType.LEFT).get(ItemToken_.id))
                    );
            }
            if (criteria.getOwnerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOwnerId(), root -> root.join(Item_.owner, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getLocationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getLocationId(), root -> root.join(Item_.location, JoinType.LEFT).get(Location_.id))
                    );
            }
            if (criteria.getTradeEventId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTradeEventId(),
                            root -> root.join(Item_.tradeEvent, JoinType.LEFT).get(TradeEvent_.id)
                        )
                    );
            }
            if (criteria.getCategoriesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCategoriesId(), root -> root.join(Item_.categories, JoinType.LEFT).get(Category_.id))
                    );
            }
            if (criteria.getCategorySlug() != null) {
                specification =
                    specification.and(buildReferringEntitySpecification(criteria.getCategorySlug(), Item_.categories, Category_.slug));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Item_.users, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
