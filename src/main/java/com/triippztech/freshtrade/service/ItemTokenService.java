package com.triippztech.freshtrade.service;

import com.triippztech.freshtrade.domain.ItemToken;
import com.triippztech.freshtrade.repository.ItemTokenRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ItemToken}.
 */
@Service
@Transactional
public class ItemTokenService {

    private final Logger log = LoggerFactory.getLogger(ItemTokenService.class);

    private final ItemTokenRepository itemTokenRepository;

    public ItemTokenService(ItemTokenRepository itemTokenRepository) {
        this.itemTokenRepository = itemTokenRepository;
    }

    /**
     * Save a itemToken.
     *
     * @param itemToken the entity to save.
     * @return the persisted entity.
     */
    public ItemToken save(ItemToken itemToken) {
        log.debug("Request to save ItemToken : {}", itemToken);
        return itemTokenRepository.save(itemToken);
    }

    /**
     * Partially update a itemToken.
     *
     * @param itemToken the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ItemToken> partialUpdate(ItemToken itemToken) {
        log.debug("Request to partially update ItemToken : {}", itemToken);

        return itemTokenRepository
            .findById(itemToken.getId())
            .map(
                existingItemToken -> {
                    if (itemToken.getTokenName() != null) {
                        existingItemToken.setTokenName(itemToken.getTokenName());
                    }
                    if (itemToken.getTokenCode() != null) {
                        existingItemToken.setTokenCode(itemToken.getTokenCode());
                    }
                    if (itemToken.getCreatedDate() != null) {
                        existingItemToken.setCreatedDate(itemToken.getCreatedDate());
                    }
                    if (itemToken.getUpdatedDate() != null) {
                        existingItemToken.setUpdatedDate(itemToken.getUpdatedDate());
                    }

                    return existingItemToken;
                }
            )
            .map(itemTokenRepository::save);
    }

    /**
     * Get all the itemTokens.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ItemToken> findAll(Pageable pageable) {
        log.debug("Request to get all ItemTokens");
        return itemTokenRepository.findAll(pageable);
    }

    /**
     * Get all the itemTokens with eager load of many-to-many relationships.
     * @param pageable {@link Pageable}
     * @return the list of entities.
     */
    public Page<ItemToken> findAllWithEagerRelationships(Pageable pageable) {
        return itemTokenRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one itemToken by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ItemToken> findOne(Long id) {
        log.debug("Request to get ItemToken : {}", id);
        return itemTokenRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the itemToken by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ItemToken : {}", id);
        itemTokenRepository.deleteById(id);
    }
}
