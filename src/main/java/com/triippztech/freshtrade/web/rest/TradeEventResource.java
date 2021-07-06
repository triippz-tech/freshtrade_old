package com.triippztech.freshtrade.web.rest;

import com.triippztech.freshtrade.domain.Location;
import com.triippztech.freshtrade.domain.TradeEvent;
import com.triippztech.freshtrade.repository.TradeEventRepository;
import com.triippztech.freshtrade.service.TradeEventQueryService;
import com.triippztech.freshtrade.service.TradeEventService;
import com.triippztech.freshtrade.service.criteria.TradeEventCriteria;
import com.triippztech.freshtrade.web.rest.errors.BadRequestAlertException;
import io.swagger.annotations.ApiOperation;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.triippztech.freshtrade.domain.TradeEvent}.
 */
@RestController
@RequestMapping("/api")
public class TradeEventResource {

    private final Logger log = LoggerFactory.getLogger(TradeEventResource.class);

    private static final String ENTITY_NAME = "tradeEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TradeEventService tradeEventService;

    private final TradeEventRepository tradeEventRepository;

    private final TradeEventQueryService tradeEventQueryService;

    public TradeEventResource(
        TradeEventService tradeEventService,
        TradeEventRepository tradeEventRepository,
        TradeEventQueryService tradeEventQueryService
    ) {
        this.tradeEventService = tradeEventService;
        this.tradeEventRepository = tradeEventRepository;
        this.tradeEventQueryService = tradeEventQueryService;
    }

    /**
     * {@code POST  /trade-events} : Create a new tradeEvent.
     *
     * @param tradeEvent the tradeEvent to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tradeEvent, or with status {@code 400 (Bad Request)} if the tradeEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/trade-events")
    public ResponseEntity<TradeEvent> createTradeEvent(@Valid @RequestBody TradeEvent tradeEvent) throws URISyntaxException {
        log.debug("REST request to save TradeEvent : {}", tradeEvent);
        if (tradeEvent.getId() != null) {
            throw new BadRequestAlertException("A new tradeEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TradeEvent result = tradeEventService.save(tradeEvent);
        return ResponseEntity
            .created(new URI("/api/trade-events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /trade-events/:id} : Updates an existing tradeEvent.
     *
     * @param id the id of the tradeEvent to save.
     * @param tradeEvent the tradeEvent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tradeEvent,
     * or with status {@code 400 (Bad Request)} if the tradeEvent is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tradeEvent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/trade-events/{id}")
    public ResponseEntity<TradeEvent> updateTradeEvent(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody TradeEvent tradeEvent
    ) throws URISyntaxException {
        log.debug("REST request to update TradeEvent : {}, {}", id, tradeEvent);
        if (tradeEvent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tradeEvent.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tradeEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TradeEvent result = tradeEventService.save(tradeEvent);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tradeEvent.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /trade-events/:id} : Partial updates given fields of an existing tradeEvent, field will ignore if it is null
     *
     * @param id the id of the tradeEvent to save.
     * @param tradeEvent the tradeEvent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tradeEvent,
     * or with status {@code 400 (Bad Request)} if the tradeEvent is not valid,
     * or with status {@code 404 (Not Found)} if the tradeEvent is not found,
     * or with status {@code 500 (Internal Server Error)} if the tradeEvent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/trade-events/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TradeEvent> partialUpdateTradeEvent(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody TradeEvent tradeEvent
    ) throws URISyntaxException {
        log.debug("REST request to partial update TradeEvent partially : {}, {}", id, tradeEvent);
        if (tradeEvent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tradeEvent.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tradeEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TradeEvent> result = tradeEventService.partialUpdate(tradeEvent);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tradeEvent.getId().toString())
        );
    }

    /**
     * {@code GET  /trade-events} : get all the tradeEvents.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tradeEvents in body.
     */
    @GetMapping("/trade-events")
    public ResponseEntity<List<TradeEvent>> getAllTradeEvents(TradeEventCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TradeEvents by criteria: {}", criteria);
        Page<TradeEvent> page = tradeEventQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /trade-events/count} : count all the tradeEvents.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/trade-events/count")
    public ResponseEntity<Long> countTradeEvents(TradeEventCriteria criteria) {
        log.debug("REST request to count TradeEvents by criteria: {}", criteria);
        return ResponseEntity.ok().body(tradeEventQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /trade-events/:id} : get the "id" tradeEvent.
     *
     * @param id the id of the tradeEvent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tradeEvent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/trade-events/{id}")
    public ResponseEntity<TradeEvent> getTradeEvent(@PathVariable UUID id) {
        log.debug("REST request to get TradeEvent : {}", id);
        Optional<TradeEvent> tradeEvent = tradeEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tradeEvent);
    }

    /**
     * {@code DELETE  /trade-events/:id} : delete the "id" tradeEvent.
     *
     * @param id the id of the tradeEvent to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/trade-events/{id}")
    public ResponseEntity<Void> deleteTradeEvent(@PathVariable UUID id) {
        log.debug("REST request to delete TradeEvent : {}", id);
        tradeEventService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/trade-events?query=:query} : search for the trade events
     * to the query.
     *
     * @param query    the query of the trade events search.
     * @return the result of the search.
     */
    @GetMapping("/_search/trade-events")
    @ApiOperation(value = "Searches for Trade Events based on a query string. Utilizes pagination", response = ResponseEntity.class)
    public ResponseEntity<List<TradeEvent>> searchTradeEvents(@RequestParam String query) {
        log.debug("REST request to search for a page of TradeEvents for query {}", query);
        List<TradeEvent> tradeEvents = tradeEventService.search(query);
        return ResponseEntity.ok().body(tradeEvents);
    }
}
