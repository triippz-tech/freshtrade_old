package com.triippztech.freshtrade.service;

import com.triippztech.freshtrade.domain.*;
import com.triippztech.freshtrade.repository.ImageRepository;
import com.triippztech.freshtrade.repository.ItemRepository;
import com.triippztech.freshtrade.repository.ItemTokenRepository;
import com.triippztech.freshtrade.repository.ReservationRepository;
import com.triippztech.freshtrade.service.dto.item.ItemDetailDTO;
import com.triippztech.freshtrade.service.dto.item.ItemReservationDTO;
import com.triippztech.freshtrade.service.mail.BuyerMailService;
import com.triippztech.freshtrade.service.mail.SellerEmailService;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Item}.
 */
@Service
@Transactional
public class ItemService {

    public static class ItemServiceException extends RuntimeException {

        private String clientMessage;

        public ItemServiceException(String message) {
            super(message);
        }

        public ItemServiceException(String message, String clientMessage) {
            super(message);
            this.clientMessage = clientMessage;
        }

        public String getClientMessage() {
            return clientMessage;
        }
    }

    private final Logger log = LoggerFactory.getLogger(ItemService.class);

    private final ItemRepository itemRepository;

    private final ImageRepository imageRepository;

    private final ReservationRepository reservationRepository;

    private final ItemTokenRepository tokenRepository;

    private final BuyerMailService buyerMailService;

    private final SellerEmailService sellerMailService;

    public ItemService(
        ItemRepository itemRepository,
        ImageRepository imageRepository,
        ReservationRepository reservationRepository,
        ItemTokenRepository tokenRepository,
        BuyerMailService buyerMailService,
        SellerEmailService sellerMailService
    ) {
        this.itemRepository = itemRepository;
        this.imageRepository = imageRepository;
        this.reservationRepository = reservationRepository;
        this.tokenRepository = tokenRepository;
        this.buyerMailService = buyerMailService;
        this.sellerMailService = sellerMailService;
    }

    /**
     * Save a item.
     *
     * @param item the entity to save.
     * @return the persisted entity.
     */
    public Item save(Item item) {
        log.debug("Request to save Item : {}", item);
        item.setUpdatedDate(ZonedDateTime.now());
        return itemRepository.save(item);
    }

    public Item createItem(Item item, User user) {
        log.debug("Request to save Item : {} for Seller: {}", item, user);
        item.setCreatedDate(ZonedDateTime.now());
        item.isActive(true);
        item.setOwner(user);
        var createdItem = save(item);
        item
            .getImages()
            .forEach(
                image -> {
                    image.setItem(createdItem);
                    image.setCreatedDate(ZonedDateTime.now());
                    image.setIsVisible(true);
                    imageRepository.save(image);
                }
            );
        return createdItem;
    }

    public Item sellerUpdate(Item item, User user) {
        log.debug("Request to update Item : {} for Seller: {}", item, user);

        var foundItem = findOne(item.getId());
        if (foundItem.isEmpty()) throw new EntityNotFoundException("Item was not found");

        if (!foundItem.get().getOwner().getId().equals(user.getId())) throw new ItemServiceException("You are not the owner of this item.");

        item
            .getImages()
            .forEach(
                image -> {
                    if (
                        image.getId() == null ||
                        foundItem.get().getImages().stream().noneMatch(image1 -> image1.getImageUrl().equals(image.getImageUrl()))
                    ) {
                        image.setItem(foundItem.get());
                        image.setCreatedDate(ZonedDateTime.now());
                        image.setIsVisible(true);
                        imageRepository.save(image);
                    }
                }
            );
        return save(item);
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
     * Get all the items.
     *
     * @param user {@link User}
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Item> findAllByCurrentUser(User user, Pageable pageable) {
        log.debug("Request to get all Items for current User: {}", user);
        return itemRepository.findAllByOwner(user, pageable);
    }

    /**
     * Get all the items with eager load of many-to-many relationships.
     * @param pageable {@link Pageable} Page
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

    /**
     * Delete the item by id. Check if the SELLER deleting owns the item first
     *
     * @param id the id of the entity.
     * @param user {@link User}
     * @throws ItemServiceException unable to delete
     */
    public void delete(UUID id, User user) throws ItemServiceException {
        log.debug("Request by seller: {} to delete Item : {}", user, id);
        var foundItem = findOne(id);
        if (foundItem.isEmpty()) throw new ItemServiceException("Item does not exist");

        if (!foundItem.get().getOwner().getId().equals(user.getId())) throw new ItemServiceException(
            "Unable to delete. You are not the owner of this item."
        );
        delete(id);
    }

    /**
     * archive the item by id.
     *
     * @param id the id of the entity.
     * @throws ItemServiceException unable to delete
     */
    public void archiveItem(UUID id) throws ItemServiceException {
        log.debug("Request to delete Item : {}", id);
        var foundItem = findOne(id);
        if (foundItem.isEmpty()) throw new ItemServiceException("Item does not exist");

        foundItem.get().isActive(false);
        save(foundItem.get());
    }

    /**
     * archive the item by id.
     *
     * @param id the id of the entity.
     */
    public void archiveItem(UUID id, User user) throws ItemServiceException {
        log.debug("Request to delete Item : {}", id);
        var foundItem = findOne(id);
        if (foundItem.isEmpty()) throw new ItemServiceException("Item does not exist");

        if (!foundItem.get().getOwner().getId().equals(user.getId())) throw new ItemServiceException(
            "Unable to delete. You are not the owner of this item."
        );
        foundItem.get().isActive(false);
        save(foundItem.get());
    }

    @Transactional(readOnly = true)
    public Optional<ItemDetailDTO> findOneWithAvailableTokens(UUID id) {
        log.debug("Request to get Item : {} with available tokens", id);
        var found = itemRepository.findOneWithEagerRelationships(id);
        if (found.isEmpty()) return Optional.empty();

        var tokenCount = tokenRepository.countAllByItem(found.get());
        if (tokenCount == 0) return Optional.of(new ItemDetailDTO(found.get(), tokenCount));

        if (tokenCount.equals(found.get().getQuantity())) return Optional.of(new ItemDetailDTO(found.get(), 0));

        var availableTokens = found.get().getQuantity() - tokenCount;
        return Optional.of(new ItemDetailDTO(found.get(), availableTokens));
    }

    /**
     * Reserves an item. Creates a {@link Reservation} and issues {@link ItemToken} to the buyer for redemption
     *
     * @param id       {@link UUID} the ID of the {@link Item}
     * @param quantity {@link Integer} The quantity to reserve
     * @param buyer    {@link User} The currently logged in user (buyer)
     * @return {@link ItemDetailDTO} the updated {@link Item}
     * @throws ItemServiceException Thrown if item is not found or inaalid quantity
     */
    @Transactional(rollbackFor = Exception.class)
    public ItemReservationDTO reserveItem(UUID id, Integer quantity, User buyer) throws ItemServiceException {
        var foundItem = findOne(id);
        if (foundItem.isEmpty()) throw new ItemServiceException("Item with ID: " + id + " was not found", "Item was not found");

        // Check if any tokens are available
        if (foundItem.get().getTokens().size() == foundItem.get().getQuantity()) throw new ItemServiceException(
            "Item with ID: " + id + " is unavailable",
            "Item was not found"
        );

        // Check if there enough tokens to fulfill the request
        var availableTokens = foundItem.get().getQuantity() - foundItem.get().getTokens().size();
        if (quantity > availableTokens) throw new ItemServiceException(
            String.format("Unable to reserve %d %s(s), only %d available", quantity, foundItem.get().getName(), availableTokens),
            String.format("Unable to reserve %d %s(s), only %d available", quantity, foundItem.get().getName(), availableTokens)
        );

        // Create the reservation
        Reservation reservation = generatedNewReservation(
            foundItem.get(),
            quantity,
            buyer,
            foundItem.get().getOwner(),
            foundItem.get().getTradeEvent(),
            null
        );

        // Create x number of tokens and assign them to the user
        for (int i = 1; i <= quantity; i++) {
            reservation.addToken((generateToken(foundItem.get(), reservation, buyer)));
        }

        try {
            buyerMailService.sendBuyerConfirmReservationEmail(reservation, buyer, foundItem.get(), quantity);
            sellerMailService.sendSellerConfirmReservationEmail(reservation, buyer, foundItem.get(), quantity);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return new ItemReservationDTO()
            .wasReserved(true)
            .errorMessage(null)
            .reservationId(reservation.getId())
            .reservationNumber(reservation.getReservationNumber())
            .reservedAmount(quantity);
    }

    @Transactional
    public ItemToken generateToken(Item item, Reservation reservation, User owner) {
        log.debug("Request to generate new ItemToken");
        String tokenCode = item.getTokens().stream().findFirst().isPresent()
            ? item.getTokens().stream().findFirst().get().getTokenCode()
            : generateTokenCode(item);
        String tokenName = item.getTokens().stream().findFirst().isPresent()
            ? item.getTokens().stream().findFirst().get().getTokenName()
            : generateTokenName(item);

        ItemToken itemToken = new ItemToken()
            .item(item)
            .isActive(true)
            .tokenCode(tokenCode)
            .tokenName(tokenName)
            .createdDate(ZonedDateTime.now())
            .reservation(reservation)
            .owner(owner);

        return tokenRepository.save(itemToken);
    }

    /**
     * Generates a unique code for a {@link ItemToken} based on a random {@link UUID}
     * @param item {@link Item} The item to make a code for
     * @return {@link String} the generated Code
     */
    private String generateTokenCode(Item item) {
        return "FTT=" + UUID.randomUUID().toString();
    }

    /**
     * Generates a name for a {@link ItemToken} based on the {@link Item} name
     * @param item {@link Item} The item to generate a name from
     * @return {@link String} The name of the generated token
     */
    private String generateTokenName(Item item) {
        return "FT-" + item.getName().replace(" ", "_").toUpperCase();
    }

    @Transactional(readOnly = true)
    public Reservation generatedNewReservation(
        Item item,
        Integer totalItems,
        User buyer,
        User seller,
        TradeEvent event,
        ZonedDateTime pickupTime
    ) {
        log.debug("Request to generate new Reservation");
        Reservation reservation = new Reservation()
            .reservationNumber(generateReservationNumber())
            .buyer(buyer)
            .seller(seller)
            .event(event)
            .isActive(true)
            .createdDate(ZonedDateTime.now())
            .isCancelled(false)
            .item(item)
            .pricePer(item.getPrice())
            .totalPrice(totalItems * item.getPrice())
            .pickupTime(pickupTime);
        return reservationRepository.save(reservation);
    }

    private String generateReservationNumber() {
        return "FT-" + UUID.randomUUID().toString();
    }

    @Transactional(readOnly = true)
    public Page<Item> search(String query, Pageable page) {
        var found = itemRepository.search(query, page);
        return new PageImpl<>(
            found
                .getContent()
                .stream()
                .map(
                    key -> {
                        Hibernate.initialize(key.getCategories());
                        Hibernate.initialize(key.getImages());
                        Hibernate.initialize(key.getLocation());
                        Hibernate.initialize(key.getTradeEvent());
                        return key;
                    }
                )
                .collect(Collectors.toList()),
            page,
            found.getTotalElements()
        );
    }
}
