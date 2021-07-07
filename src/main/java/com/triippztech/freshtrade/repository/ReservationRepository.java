package com.triippztech.freshtrade.repository;

import com.triippztech.freshtrade.domain.Reservation;
import com.triippztech.freshtrade.domain.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.lang.Nullable;
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
}
