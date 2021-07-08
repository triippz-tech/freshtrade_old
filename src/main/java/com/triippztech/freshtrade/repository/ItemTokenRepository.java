package com.triippztech.freshtrade.repository;

import com.triippztech.freshtrade.domain.ItemToken;
import com.triippztech.freshtrade.domain.Reservation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ItemToken entity.
 */
@Repository
public interface ItemTokenRepository extends JpaRepository<ItemToken, Long>, JpaSpecificationExecutor<ItemToken> {
    @Query(
        value = "select distinct itemToken from ItemToken itemToken left join fetch itemToken.owner",
        countQuery = "select count(distinct itemToken) from ItemToken itemToken"
    )
    Page<ItemToken> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct itemToken from ItemToken itemToken left join fetch itemToken.owner")
    List<ItemToken> findAllWithEagerRelationships();

    @Query("select itemToken from ItemToken itemToken left join fetch itemToken.owner where itemToken.id =:id")
    Optional<ItemToken> findOneWithEagerRelationships(@Param("id") Long id);

    List<ItemToken> findAllByReservation(Reservation reservation);
}
