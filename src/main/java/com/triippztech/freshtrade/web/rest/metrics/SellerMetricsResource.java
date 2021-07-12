package com.triippztech.freshtrade.web.rest.metrics;

import com.triippztech.freshtrade.service.UserService;
import com.triippztech.freshtrade.service.dto.metrics.SellerKPIsDTO;
import com.triippztech.freshtrade.service.dto.metrics.TopSellingItemsDTO;
import com.triippztech.freshtrade.service.metrics.SellerMetricsService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/metrics/seller")
public class SellerMetricsResource {

    public SellerMetricsResource(SellerMetricsService sellerMetricsService, UserService userService) {
        this.sellerMetricsService = sellerMetricsService;
        this.userService = userService;
    }

    private static class SellerMetricsResourceException extends RuntimeException {

        private SellerMetricsResourceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(SellerMetricsResource.class);

    private final SellerMetricsService sellerMetricsService;

    private final UserService userService;

    /**
     * {@code GET  /top-selling} : get the top selling items for the current user (Seller).
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of top selling items in body.
     */
    @GetMapping("/top-selling")
    public ResponseEntity<List<TopSellingItemsDTO>> getTopSellingItems() {
        log.debug("REST request to get find top selling items for Seller");
        var user = userService
            .getUserWithAuthorities()
            .orElseThrow(() -> new SellerMetricsResourceException("You are not authorized to do perform that action"));
        var tsi = sellerMetricsService.findTopSellingItems(user);
        return ResponseEntity.ok().body(tsi);
    }

    /**
     * {@code GET  /kpis} : Get the Seller's (current User) Sales KPIs (Seller).
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the Sales KPIs in body.
     */
    @GetMapping("/kpis")
    public ResponseEntity<SellerKPIsDTO> getSellerKPIs() {
        log.debug("REST request to get find KPIs for Seller");
        var user = userService
            .getUserWithAuthorities()
            .orElseThrow(() -> new SellerMetricsResourceException("You are not authorized to do perform that action"));
        var kpis = sellerMetricsService.getSellerKPIs(user);
        return ResponseEntity.ok().body(kpis);
    }
}
