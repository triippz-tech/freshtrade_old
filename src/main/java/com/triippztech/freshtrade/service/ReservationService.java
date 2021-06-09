package com.triippztech.freshtrade.service;

import com.triippztech.freshtrade.domain.Item;
import com.triippztech.freshtrade.domain.Reservation;
import com.triippztech.freshtrade.domain.TradeEvent;
import com.triippztech.freshtrade.domain.User;
import com.triippztech.freshtrade.repository.ReservationRepository;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Reservation}.
 */
@Service
@Transactional
public class ReservationService {

    private final Logger log = LoggerFactory.getLogger(ReservationService.class);

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    /**
     * Save a reservation.
     *
     * @param reservation the entity to save.
     * @return the persisted entity.
     */
    public Reservation save(Reservation reservation) {
        log.debug("Request to save Reservation : {}", reservation);
        return reservationRepository.save(reservation);
    }

    /**
     * Partially update a reservation.
     *
     * @param reservation the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Reservation> partialUpdate(Reservation reservation) {
        log.debug("Request to partially update Reservation : {}", reservation);

        return reservationRepository
            .findById(reservation.getId())
            .map(
                existingReservation -> {
                    if (reservation.getReservationNumber() != null) {
                        existingReservation.setReservationNumber(reservation.getReservationNumber());
                    }
                    if (reservation.getIsActive() != null) {
                        existingReservation.setIsActive(reservation.getIsActive());
                    }
                    if (reservation.getIsCancelled() != null) {
                        existingReservation.setIsCancelled(reservation.getIsCancelled());
                    }
                    if (reservation.getCancellationNote() != null) {
                        existingReservation.setCancellationNote(reservation.getCancellationNote());
                    }
                    if (reservation.getPickupTime() != null) {
                        existingReservation.setPickupTime(reservation.getPickupTime());
                    }
                    if (reservation.getCreatedDate() != null) {
                        existingReservation.setCreatedDate(reservation.getCreatedDate());
                    }
                    if (reservation.getUpdatedDate() != null) {
                        existingReservation.setUpdatedDate(reservation.getUpdatedDate());
                    }

                    return existingReservation;
                }
            )
            .map(reservationRepository::save);
    }

    /**
     * Get all the reservations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Reservation> findAll(Pageable pageable) {
        log.debug("Request to get all Reservations");
        return reservationRepository.findAll(pageable);
    }

    /**
     * Get one reservation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Reservation> findOne(UUID id) {
        log.debug("Request to get Reservation : {}", id);
        return reservationRepository.findById(id);
    }

    /**
     * Delete the reservation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        log.debug("Request to delete Reservation : {}", id);
        reservationRepository.deleteById(id);
    }
}
