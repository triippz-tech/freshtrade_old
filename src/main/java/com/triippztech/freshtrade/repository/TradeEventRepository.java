package com.triippztech.freshtrade.repository;

import com.triippztech.freshtrade.domain.TradeEvent;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TradeEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TradeEventRepository extends JpaRepository<TradeEvent, UUID>, JpaSpecificationExecutor<TradeEvent> {}
