package com.triippztech.freshtrade.repository;

import com.triippztech.freshtrade.domain.Reservation;
import com.triippztech.freshtrade.domain.User;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Reservation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID>, JpaSpecificationExecutor<Reservation> {
    @Query("select reservation from Reservation reservation where reservation.seller.login = ?#{principal.username}")
    List<Reservation> findBySellerIsCurrentUser();

    @Query("select reservation from Reservation reservation where reservation.buyer.login = ?#{principal.username}")
    List<Reservation> findByBuyerIsCurrentUser();

    Integer countReservationsByIsActiveIsFalseAndSeller(User user);

    @Query(
        "select count(reservation) from Reservation reservation where reservation.seller.login = ?#{principal.username} and reservation.isActive = false"
    )
    Integer countPastReservationsForUser();

    @Query(
        "select count(reservation) from Reservation reservation where reservation.seller.login = ?#{principal.username} and reservation.isActive = true"
    )
    Integer countReservationsByIsActiveIsTrueAndUser();

    @Query(
        "select count(reservation) from Reservation reservation where reservation.seller.login = ?#{principal.username} and reservation.isCancelled = true"
    )
    Integer countCancelledReservationForUser();

    @Query
    Integer countReservationsByPickupTimeIsBetweenAndSeller(ZonedDateTime now, ZonedDateTime endTime, User seller);
}
