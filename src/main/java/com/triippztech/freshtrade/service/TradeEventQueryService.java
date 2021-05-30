package com.triippztech.freshtrade.service;

import com.triippztech.freshtrade.domain.*; // for static metamodels
import com.triippztech.freshtrade.domain.TradeEvent;
import com.triippztech.freshtrade.repository.TradeEventRepository;
import com.triippztech.freshtrade.service.criteria.TradeEventCriteria;
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
 * Service for executing complex queries for {@link TradeEvent} entities in the database.
 * The main input is a {@link TradeEventCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TradeEvent} or a {@link Page} of {@link TradeEvent} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TradeEventQueryService extends QueryService<TradeEvent> {

    private final Logger log = LoggerFactory.getLogger(TradeEventQueryService.class);

    private final TradeEventRepository tradeEventRepository;

    public TradeEventQueryService(TradeEventRepository tradeEventRepository) {
        this.tradeEventRepository = tradeEventRepository;
    }

    /**
     * Return a {@link List} of {@link TradeEvent} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TradeEvent> findByCriteria(TradeEventCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TradeEvent> specification = createSpecification(criteria);
        return tradeEventRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TradeEvent} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TradeEvent> findByCriteria(TradeEventCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TradeEvent> specification = createSpecification(criteria);
        return tradeEventRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TradeEventCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TradeEvent> specification = createSpecification(criteria);
        return tradeEventRepository.count(specification);
    }

    /**
     * Function to convert {@link TradeEventCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TradeEvent> createSpecification(TradeEventCriteria criteria) {
        Specification<TradeEvent> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), TradeEvent_.id));
            }
            if (criteria.getEventName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEventName(), TradeEvent_.eventName));
            }
            if (criteria.getEventDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEventDescription(), TradeEvent_.eventDescription));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), TradeEvent_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), TradeEvent_.endDate));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), TradeEvent_.isActive));
            }
            if (criteria.getLocationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLocationId(),
                            root -> root.join(TradeEvent_.location, JoinType.LEFT).get(Location_.id)
                        )
                    );
            }
            if (criteria.getItemsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getItemsId(), root -> root.join(TradeEvent_.items, JoinType.LEFT).get(Item_.id))
                    );
            }
            if (criteria.getReservationsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getReservationsId(),
                            root -> root.join(TradeEvent_.reservations, JoinType.LEFT).get(Reservation_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
