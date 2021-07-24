package com.triippztech.freshtrade.service;

import com.triippztech.freshtrade.domain.Reservation;
import com.triippztech.freshtrade.domain.User;
import com.triippztech.freshtrade.repository.ItemTokenRepository;
import com.triippztech.freshtrade.repository.ReservationRepository;
import com.triippztech.freshtrade.security.AuthoritiesConstants;
import com.triippztech.freshtrade.service.dto.reservation.CancelReservationDTO;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
import org.hibernate.Hibernate;
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

    public static class ReservationServiceException extends RuntimeException {

        private String clientMessage;

        public ReservationServiceException(String message) {
            super(message);
        }

        public ReservationServiceException(String message, String clientMessage) {
            super(message);
            this.clientMessage = clientMessage;
        }

        public String getClientMessage() {
            return clientMessage;
        }
    }

    private final Logger log = LoggerFactory.getLogger(ReservationService.class);

    private final ReservationRepository reservationRepository;

    private final ItemTokenRepository itemTokenRepository;

    public ReservationService(ReservationRepository reservationRepository, ItemTokenRepository itemTokenRepository) {
        this.reservationRepository = reservationRepository;
        this.itemTokenRepository = itemTokenRepository;
    }

    /**
     * Save a reservation.
     *
     * @param reservation the entity to save.
     * @return the persisted entity.
     */
    public Reservation save(Reservation reservation) {
        log.debug("Request to save Reservation : {}", reservation);
        reservation.setUpdatedDate(ZonedDateTime.now());
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

    public Reservation cancelReservation(CancelReservationDTO reservationCancel, User user, String authority) {
        var foundRes = findOne(reservationCancel.getId());
        if (foundRes.isEmpty()) throw new ReservationServiceException("Reservation not found");

        if (authority.equals(AuthoritiesConstants.SELLER)) {
            if (!foundRes.get().getSeller().getId().equals(user.getId())) throw new ReservationServiceException(
                "You are not the owner of this reservation"
            );
        } else if (authority.equals(AuthoritiesConstants.BUYER)) {
            if (!foundRes.get().getBuyer().getId().equals(user.getId())) throw new ReservationServiceException(
                "You are not the buyer of this reservation"
            );
        } else {
            throw new ReservationServiceException("You are not authorized to do that");
        }

        // remove the tokens
        itemTokenRepository.findAllByReservation(foundRes.get()).forEach(itemTokenRepository::delete);

        // update the res
        var res = foundRes.get();
        res.setIsActive(false);
        res.setIsCancelled(true);
        res.setCancellationNote(reservationCancel.getCancellationNote());

        // Add thread to send email to seller/buyer
        return save(res);
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

    /**
     * Redeems a reservation for a buyer
     * @param reservationNumber the reservation number of the reservation to redeem
     * @return Reservation
     */
    public Reservation redeemReservation(String reservationNumber, User user) {
        log.debug("Request to redeem Reservation : {}", reservationNumber);
        var foundRes = reservationRepository.findByReservationNumber(reservationNumber);
        if (foundRes.isEmpty()) throw new ReservationServiceException("Reservation not found with given Reservation Number");

        if (!foundRes.get().getBuyer().getId().equals(user.getId())) throw new ReservationServiceException(
            "You are not the buyer of this reservation"
        );

        if (!foundRes.get().getIsActive()) throw new ReservationServiceException("This reservation is no longer active");

        // remove the tokens
        itemTokenRepository.findAllByReservation(foundRes.get()).forEach(itemTokenRepository::delete);

        // update the res
        var res = foundRes.get();
        res.setIsActive(false);
        res.setIsCancelled(false);

        // Add thread to send email to seller/buyer that item was successfully redeemed
        var reserv = save(res);
        Hibernate.initialize(reserv.getItem().getImages());
        return reserv;
    }
}
