package com.triippztech.freshtrade.web.rest;

import com.triippztech.freshtrade.domain.Reservation;
import com.triippztech.freshtrade.repository.ReservationRepository;
import com.triippztech.freshtrade.service.ReservationQueryService;
import com.triippztech.freshtrade.service.ReservationService;
import com.triippztech.freshtrade.service.UserService;
import com.triippztech.freshtrade.service.criteria.ReservationCriteria;
import com.triippztech.freshtrade.service.dto.reservation.CancelReservationDTO;
import com.triippztech.freshtrade.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.triippztech.freshtrade.domain.Reservation}.
 */
@RestController
@RequestMapping("/api")
public class ReservationResource {

    private static class ReservationResourceException extends RuntimeException {

        private ReservationResourceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(ReservationResource.class);

    private static final String ENTITY_NAME = "reservation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReservationService reservationService;

    private final ReservationRepository reservationRepository;

    private final ReservationQueryService reservationQueryService;

    private final UserService userService;

    public ReservationResource(
        ReservationService reservationService,
        ReservationRepository reservationRepository,
        ReservationQueryService reservationQueryService,
        UserService userService
    ) {
        this.reservationService = reservationService;
        this.reservationRepository = reservationRepository;
        this.reservationQueryService = reservationQueryService;
        this.userService = userService;
    }

    /**
     * {@code POST  /reservations} : Create a new reservation.
     *
     * @param reservation the reservation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reservation, or with status {@code 400 (Bad Request)} if the reservation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/reservations")
    public ResponseEntity<Reservation> createReservation(@Valid @RequestBody Reservation reservation) throws URISyntaxException {
        log.debug("REST request to save Reservation : {}", reservation);
        if (reservation.getId() != null) {
            throw new BadRequestAlertException("A new reservation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Reservation result = reservationService.save(reservation);
        return ResponseEntity
            .created(new URI("/api/reservations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /reservations/:id} : Updates an existing reservation.
     *
     * @param id the id of the reservation to save.
     * @param reservation the reservation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reservation,
     * or with status {@code 400 (Bad Request)} if the reservation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reservation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/reservations/{id}")
    public ResponseEntity<Reservation> updateReservation(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody Reservation reservation
    ) throws URISyntaxException {
        log.debug("REST request to update Reservation : {}, {}", id, reservation);
        if (reservation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reservation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reservationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Reservation result = reservationService.save(reservation);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reservation.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /reservations/:id} : Partial updates given fields of an existing reservation, field will ignore if it is null
     *
     * @param id the id of the reservation to save.
     * @param reservation the reservation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reservation,
     * or with status {@code 400 (Bad Request)} if the reservation is not valid,
     * or with status {@code 404 (Not Found)} if the reservation is not found,
     * or with status {@code 500 (Internal Server Error)} if the reservation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/reservations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Reservation> partialUpdateReservation(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody Reservation reservation
    ) throws URISyntaxException {
        log.debug("REST request to partial update Reservation partially : {}, {}", id, reservation);
        if (reservation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reservation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reservationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Reservation> result = reservationService.partialUpdate(reservation);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reservation.getId().toString())
        );
    }

    /**
     * {@code GET  /reservations} : get all the reservations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reservations in body.
     */
    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> getAllReservations(ReservationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Reservations by criteria: {}", criteria);
        Page<Reservation> page = reservationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reservations/seller} : get all the reservations for the current user.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reservations in body.
     */
    @GetMapping("/reservations/seller")
    public ResponseEntity<List<Reservation>> getCurrentUserReservations(ReservationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Reservations by criteria: {}", criteria);
        var user = userService
            .getUserWithAuthorities()
            .orElseThrow(() -> new ReservationResourceException("You are not authorized to do perform that action"));
        Page<Reservation> page = reservationQueryService.findByCriteria(user, criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code PUT  /reservations/:id/seller/cancel} : Updates an existing reservation.
     *
     * @param id the id of the reservation to save.
     * @param reservationCancel the reservation to cancel.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reservation,
     * or with status {@code 400 (Bad Request)} if the reservation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reservation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/reservations/{id}/seller/cancel")
    public ResponseEntity<Reservation> sellerCancelReservation(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody CancelReservationDTO reservationCancel
    ) throws URISyntaxException {
        log.debug("REST request to cancel Reservation : {}, {}", id, reservationCancel);
        if (reservationCancel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reservationCancel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reservationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        var user = userService
            .getUserWithAuthorities()
            .orElseThrow(() -> new ReservationResourceException("You are not authorized to do perform that action"));

        Reservation result = reservationService.cancelReservation(reservationCancel, user);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reservationCancel.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /reservations/count} : count all the reservations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/reservations/count")
    public ResponseEntity<Long> countReservations(ReservationCriteria criteria) {
        log.debug("REST request to count Reservations by criteria: {}", criteria);
        return ResponseEntity.ok().body(reservationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /reservations/:id} : get the "id" reservation.
     *
     * @param id the id of the reservation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reservation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/reservations/{id}")
    public ResponseEntity<Reservation> getReservation(@PathVariable UUID id) {
        log.debug("REST request to get Reservation : {}", id);
        Optional<Reservation> reservation = reservationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reservation);
    }

    /**
     * {@code DELETE  /reservations/:id} : delete the "id" reservation.
     *
     * @param id the id of the reservation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable UUID id) {
        log.debug("REST request to delete Reservation : {}", id);
        reservationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
