package com.triippztech.freshtrade.service;

import com.triippztech.freshtrade.domain.TradeEvent;
import com.triippztech.freshtrade.repository.TradeEventRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TradeEvent}.
 */
@Service
@Transactional
public class TradeEventService {

    private final Logger log = LoggerFactory.getLogger(TradeEventService.class);

    private final TradeEventRepository tradeEventRepository;

    public TradeEventService(TradeEventRepository tradeEventRepository) {
        this.tradeEventRepository = tradeEventRepository;
    }

    /**
     * Save a tradeEvent.
     *
     * @param tradeEvent the entity to save.
     * @return the persisted entity.
     */
    public TradeEvent save(TradeEvent tradeEvent) {
        log.debug("Request to save TradeEvent : {}", tradeEvent);
        return tradeEventRepository.save(tradeEvent);
    }

    /**
     * Partially update a tradeEvent.
     *
     * @param tradeEvent the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TradeEvent> partialUpdate(TradeEvent tradeEvent) {
        log.debug("Request to partially update TradeEvent : {}", tradeEvent);

        return tradeEventRepository
            .findById(tradeEvent.getId())
            .map(
                existingTradeEvent -> {
                    if (tradeEvent.getEventName() != null) {
                        existingTradeEvent.setEventName(tradeEvent.getEventName());
                    }
                    if (tradeEvent.getEventDescription() != null) {
                        existingTradeEvent.setEventDescription(tradeEvent.getEventDescription());
                    }
                    if (tradeEvent.getStartDate() != null) {
                        existingTradeEvent.setStartDate(tradeEvent.getStartDate());
                    }
                    if (tradeEvent.getEndDate() != null) {
                        existingTradeEvent.setEndDate(tradeEvent.getEndDate());
                    }
                    if (tradeEvent.getIsActive() != null) {
                        existingTradeEvent.setIsActive(tradeEvent.getIsActive());
                    }

                    return existingTradeEvent;
                }
            )
            .map(tradeEventRepository::save);
    }

    /**
     * Get all the tradeEvents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TradeEvent> findAll(Pageable pageable) {
        log.debug("Request to get all TradeEvents");
        return tradeEventRepository.findAll(pageable);
    }

    /**
     * Get one tradeEvent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TradeEvent> findOne(UUID id) {
        log.debug("Request to get TradeEvent : {}", id);
        return tradeEventRepository.findById(id);
    }

    /**
     * Delete the tradeEvent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        log.debug("Request to delete TradeEvent : {}", id);
        tradeEventRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<TradeEvent> search(String query) {
        return tradeEventRepository.findAllByEventNameContainingIgnoreCaseOrderByEventNameAsc(query, PageRequest.of(0, 20));
    }
}
