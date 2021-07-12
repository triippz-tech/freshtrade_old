package com.triippztech.freshtrade.repository;

import com.triippztech.freshtrade.domain.TradeEvent;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TradeEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TradeEventRepository extends JpaRepository<TradeEvent, UUID>, JpaSpecificationExecutor<TradeEvent> {
    List<TradeEvent> findAllByEventNameContainingIgnoreCaseOrderByEventNameAsc(String query, Pageable pageable);
}
