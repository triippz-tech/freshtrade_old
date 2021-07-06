package com.triippztech.freshtrade.repository;

import com.triippztech.freshtrade.domain.Location;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Location entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocationRepository extends JpaRepository<Location, Long>, JpaSpecificationExecutor<Location> {
    List<Location> findAllByShortNameContainingIgnoreCaseOrderByShortName(String query, Pageable pageable);
}
