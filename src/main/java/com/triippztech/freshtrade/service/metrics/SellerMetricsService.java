package com.triippztech.freshtrade.service.metrics;

import com.triippztech.freshtrade.domain.User;
import com.triippztech.freshtrade.repository.*;
import com.triippztech.freshtrade.service.dto.metrics.SellerKPIsDTO;
import com.triippztech.freshtrade.service.dto.metrics.TopSellingItemsDTO;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SellerMetricsService {

    private static class SellerMetricsServiceException extends RuntimeException {

        private SellerMetricsServiceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(SellerMetricsService.class);

    private final ItemRepository itemRepository;

    private final ReservationRepository reservationRepository;

    private final ItemTokenRepository itemTokenRepository;

    private final LocationRepository locationRepository;

    private final TradeEventRepository tradeEventRepository;

    public SellerMetricsService(
        ItemRepository itemRepository,
        ReservationRepository reservationRepository,
        ItemTokenRepository itemTokenRepository,
        LocationRepository locationRepository,
        TradeEventRepository tradeEventRepository
    ) {
        this.itemRepository = itemRepository;
        this.reservationRepository = reservationRepository;
        this.itemTokenRepository = itemTokenRepository;
        this.locationRepository = locationRepository;
        this.tradeEventRepository = tradeEventRepository;
    }

    public List<TopSellingItemsDTO> findTopSellingItems(User user) {
        log.debug("Request to find top selling items for User: {}", user);
        return itemRepository.findTopSellingItemsForUser(user);
    }

    public SellerKPIsDTO getSellerKPIs(User seller) {
        log.debug("Request to find KPIs for Seller: {}", seller);

        SellerKPIsDTO sellerKPIs = new SellerKPIsDTO();

        // Total Rev
        sellerKPIs.setTotalRevenue(reservationRepository.getTotalSellerRevenue(seller));
        // Monthly Rev
        var firstOfMonth = java.util.Date.from(ZonedDateTime.now().withDayOfMonth(1).toInstant());
        var lastOfMonth = java.util.Date.from(ZonedDateTime.now().with(TemporalAdjusters.lastDayOfMonth()).toInstant());
        sellerKPIs.setMonthlyRevenue(reservationRepository.getTotalSellerRevenueCurrentMonth(seller, firstOfMonth, lastOfMonth));
        // Unique Buyers
        sellerKPIs.setUniqueBuyers(reservationRepository.getUniqueBuyers(seller));
        // Returning Buyers
        var result = reservationRepository.getReturningBuyers(seller);
        sellerKPIs.setReturningBuyers(result.size());
        // 7D Reservations
        sellerKPIs.setPending7DReservations(reservationRepository.get7DReservations(seller, ZonedDateTime.now().plus(7, ChronoUnit.DAYS)));
        // Total Active Reservations (open)
        sellerKPIs.setTotalActiveReservations(reservationRepository.countAllBySellerAndIsActiveIsTrueAndIsCancelledIsFalse(seller));
        // Total Completed Reservations
        sellerKPIs.setTotalCompletedReservations(reservationRepository.countAllBySellerAndIsActiveIsFalseAndIsCancelledIsFalse(seller));
        // Total Cancelled Reservations
        sellerKPIs.setTotalCancellations(reservationRepository.countAllBySellerAndIsActiveIsFalseAndIsCancelledIsTrue(seller));

        return sellerKPIs;
    }
}
