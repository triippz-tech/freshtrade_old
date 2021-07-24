package com.triippztech.freshtrade.repository;

import com.triippztech.freshtrade.domain.Reservation;
import com.triippztech.freshtrade.domain.User;
import com.triippztech.freshtrade.service.criteria.ReservationCriteria;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
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

    Page<Reservation> findAllBySeller(User user, Specification<Reservation> specification, Pageable page);

    /**
     * Gets the total revenue of a seller
     * @param user {@link User}
     * @return The total revenue
     */
    @Query(value = "SELECT SUM(res.totalPrice) FROM Reservation res" + " WHERE res.seller = :user")
    Double getTotalSellerRevenue(@Param("user") User user);

    /**
     *
     * @param user
     * @param startDate
     * @param endDate
     * @return
     */
    @Query(
        value = "SELECT SUM(res.totalPrice) FROM Reservation res" +
        " WHERE res.seller = :user" +
        " AND res.isActive = false" +
        " AND current_date BETWEEN :startDate AND :endDate"
    )
    Double getTotalSellerRevenueCurrentMonth(@Param("user") User user, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     *
     * @param user
     * @param sevenDaysLater
     * @return
     */
    @Query(
        value = "SELECT COUNT(res) FROM Reservation res" +
        " WHERE res.seller = :user" +
        " AND res.isActive = true" +
        " AND res.pickupTime BETWEEN current_date AND :sevenDaysLater"
    )
    Integer get7DReservations(@Param("user") User user, @Param("sevenDaysLater") ZonedDateTime sevenDaysLater);

    /**
     * All Reservations
     * @param user {@link User}
     * @return count of all reservations
     */
    Integer countAllBySeller(User user);

    /**
     * All Reservations that are active
     * @param user {@link User}
     * @return count of all reservations
     */
    Integer countAllBySellerAndIsActiveIsTrueAndIsCancelledIsFalse(User user);

    /**
     * All Reservations that are completed, NOT cancelled
     * @param user {@link User}
     * @return count of all reservations
     */
    Integer countAllBySellerAndIsActiveIsFalseAndIsCancelledIsFalse(User user);

    /**
     * All Reservations that are cancelled, NOT cancelled
     * @param user {@link User}
     * @return count of all reservations
     */
    Integer countAllBySellerAndIsActiveIsFalseAndIsCancelledIsTrue(User user);

    @Query(value = "SELECT COUNT(DISTINCT res.buyer) FROM Reservation res WHERE res.seller = :user")
    Integer getUniqueBuyers(@Param("user") User user);

    @Query(
        value = "SELECT COUNT(res) FROM Reservation res" + " WHERE res.seller = :user" + " GROUP BY res.buyer" + " HAVING COUNT(res) > 1"
    )
    List<Long> getReturningBuyers(@Param("user") User user);

    Boolean existsByReservationNumber(String reservationNumber);

    Optional<Reservation> findByReservationNumber(String reservationNumber);
}
