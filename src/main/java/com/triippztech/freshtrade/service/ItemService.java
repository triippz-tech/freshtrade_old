package com.triippztech.freshtrade.service;

import com.triippztech.freshtrade.domain.Item;
import com.triippztech.freshtrade.domain.ItemToken;
import com.triippztech.freshtrade.domain.User;
import com.triippztech.freshtrade.repository.ItemRepository;
import com.triippztech.freshtrade.service.dto.AdminUserDTO;
import com.triippztech.freshtrade.service.dto.UserDTO;
import com.triippztech.freshtrade.service.dto.item.ItemDetailDTO;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Item}.
 */
@Service
@Transactional
public class ItemService {

    private static class ItemServiceException extends RuntimeException {

        private ItemServiceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(ItemService.class);

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    /**
     * Save a item.
     *
     * @param item the entity to save.
     * @return the persisted entity.
     */
    public Item save(Item item) {
        log.debug("Request to save Item : {}", item);
        return itemRepository.save(item);
    }

    /**
     * Partially update a item.
     *
     * @param item the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Item> partialUpdate(Item item) {
        log.debug("Request to partially update Item : {}", item);

        return itemRepository
            .findById(item.getId())
            .map(
                existingItem -> {
                    if (item.getPrice() != null) {
                        existingItem.setPrice(item.getPrice());
                    }
                    if (item.getQuantity() != null) {
                        existingItem.setQuantity(item.getQuantity());
                    }
                    if (item.getName() != null) {
                        existingItem.setName(item.getName());
                    }
                    if (item.getDetails() != null) {
                        existingItem.setDetails(item.getDetails());
                    }
                    if (item.getItemCondition() != null) {
                        existingItem.setItemCondition(item.getItemCondition());
                    }
                    if (item.getIsActive() != null) {
                        existingItem.setIsActive(item.getIsActive());
                    }
                    if (item.getCreatedDate() != null) {
                        existingItem.setCreatedDate(item.getCreatedDate());
                    }
                    if (item.getUpdatedDate() != null) {
                        existingItem.setUpdatedDate(item.getUpdatedDate());
                    }

                    return existingItem;
                }
            )
            .map(itemRepository::save);
    }

    /**
     * Get all the items.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Item> findAll(Pageable pageable) {
        log.debug("Request to get all Items");
        return itemRepository.findAll(pageable);
    }

    /**
     * Get all the items with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Item> findAllWithEagerRelationships(Pageable pageable) {
        return itemRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one item by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Item> findOne(UUID id) {
        log.debug("Request to get Item : {}", id);
        return itemRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the item by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        log.debug("Request to delete Item : {}", id);
        itemRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<ItemDetailDTO> findOneWithAvailableTokens(UUID id) {
        log.debug("Request to get Item : {} with available tokens", id);
        var found = itemRepository.findOneWithEagerRelationships(id);
        if (found.isEmpty()) return Optional.empty();

        if (found.get().getTokens().isEmpty()) return Optional.of(new ItemDetailDTO(found.get(), found.get().getQuantity()));

        if (found.get().getTokens().size() == found.get().getQuantity()) return Optional.of(new ItemDetailDTO(found.get(), 0));

        var availableTokens = found.get().getQuantity() - found.get().getTokens().size();
        return Optional.of(new ItemDetailDTO(found.get(), availableTokens));
    }

    @Transactional(readOnly = true)
    public ItemDetailDTO reserveItem(UUID id, Integer quantity, User user) throws ItemServiceException {
        var foundItem = findOne(id);
        if (foundItem.isEmpty()) throw new ItemServiceException("Item with ID: " + id + " was not found");

        // Check if any tokens are available
        if (foundItem.get().getTokens().size() == foundItem.get().getQuantity()) throw new ItemServiceException(
            "Item with ID: " + id + " is unavailable"
        );

        // Check if there enough tokens to fulfill the request
        var availableTokens = foundItem.get().getQuantity() - foundItem.get().getTokens().size();
        if (quantity > availableTokens) throw new ItemServiceException(
            String.format("Unable to reserve %d %s(s), only %d available", quantity, foundItem.get().getName(), availableTokens)
        );

        // Create x number of tokens and assign them to the user
        List<ItemToken> tokens = new ArrayList<>();
        for (int i = 1; i <= quantity; i++) {
            //            tokens.add(generateToken());
        }
        return null;
    }
}
