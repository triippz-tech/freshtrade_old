package com.triippztech.freshtrade.service;

import com.triippztech.freshtrade.domain.*; // for static metamodels
import com.triippztech.freshtrade.domain.Reservation;
import com.triippztech.freshtrade.repository.ReservationRepository;
import com.triippztech.freshtrade.service.criteria.ItemCriteria;
import com.triippztech.freshtrade.service.criteria.ReservationCriteria;
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
import tech.jhipster.service.filter.LongFilter;

/**
 * Service for executing complex queries for {@link Reservation} entities in the database.
 * The main input is a {@link ReservationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Reservation} or a {@link Page} of {@link Reservation} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReservationQueryService extends QueryService<Reservation> {

    private final Logger log = LoggerFactory.getLogger(ReservationQueryService.class);

    private final ReservationRepository reservationRepository;

    public ReservationQueryService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    /**
     * Return a {@link List} of {@link Reservation} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Reservation> findByCriteria(ReservationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Reservation> specification = createSpecification(criteria);
        return reservationRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Reservation} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Reservation> findByCriteria(ReservationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Reservation> specification = createSpecification(criteria);
        return reservationRepository.findAll(specification, page);
    }

    /**
     * Return a {@link Page} of {@link Reservation} which matches the criteria from the database.
     * @param user The current user
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Reservation> findByCriteriaSeller(User user, ReservationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);

        if (criteria == null) {
            criteria = new ReservationCriteria();
        }

        var lf = new LongFilter();
        lf.setEquals(user.getId());
        criteria.setSellerId(lf);

        final Specification<Reservation> specification = createSpecification(criteria);
        var found = reservationRepository.findAll(specification, page);
        return new PageImpl<>(
            found
                .getContent()
                .stream()
                .peek(
                    key -> {
                        Hibernate.initialize(key.getBuyer());
                        Hibernate.initialize(key.getTokens());
                    }
                )
                .collect(Collectors.toList()),
            page,
            found.getTotalElements()
        );
    }

    /**
     * Return a {@link Page} of {@link Reservation} which matches the criteria from the database.
     * @param user The current user
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Reservation> findByCriteriaBuyer(User user, ReservationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);

        if (criteria == null) {
            criteria = new ReservationCriteria();
        }

        var lf = new LongFilter();
        lf.setEquals(user.getId());
        criteria.setBuyerId(lf);

        final Specification<Reservation> specification = createSpecification(criteria);
        var found = reservationRepository.findAll(specification, page);
        return new PageImpl<>(
            found
                .getContent()
                .stream()
                .peek(
                    key -> {
                        Hibernate.initialize(key.getBuyer());
                        Hibernate.initialize(key.getTokens());
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
    public long countByCriteria(ReservationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Reservation> specification = createSpecification(criteria);
        return reservationRepository.count(specification);
    }

    /**
     * Function to convert {@link ReservationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Reservation> createSpecification(ReservationCriteria criteria) {
        Specification<Reservation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Reservation_.id));
            }
            if (criteria.getReservationNumber() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getReservationNumber(), Reservation_.reservationNumber));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), Reservation_.isActive));
            }
            if (criteria.getIsCancelled() != null) {
                specification = specification.and(buildSpecification(criteria.getIsCancelled(), Reservation_.isCancelled));
            }
            if (criteria.getPickupTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPickupTime(), Reservation_.pickupTime));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Reservation_.createdDate));
            }
            if (criteria.getUpdatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedDate(), Reservation_.updatedDate));
            }
            if (criteria.getSellerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSellerId(), root -> root.join(Reservation_.seller, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getBuyerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBuyerId(), root -> root.join(Reservation_.buyer, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getEventId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEventId(), root -> root.join(Reservation_.event, JoinType.LEFT).get(TradeEvent_.id))
                    );
            }
        }
        return specification;
    }
}
