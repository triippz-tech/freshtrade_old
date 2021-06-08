package com.triippztech.freshtrade.web.rest;

import com.triippztech.freshtrade.domain.Item;
import com.triippztech.freshtrade.repository.ItemRepository;
import com.triippztech.freshtrade.service.ItemQueryService;
import com.triippztech.freshtrade.service.ItemService;
import com.triippztech.freshtrade.service.UserService;
import com.triippztech.freshtrade.service.criteria.ItemCriteria;
import com.triippztech.freshtrade.service.dto.AdminUserDTO;
import com.triippztech.freshtrade.service.dto.item.ItemDetailDTO;
import com.triippztech.freshtrade.service.dto.item.ListItemDTO;
import com.triippztech.freshtrade.service.mapper.ItemMapper;
import com.triippztech.freshtrade.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.triippztech.freshtrade.domain.Item}.
 */
@RestController
@RequestMapping("/api")
public class ItemResource {

    private static class ItemResourceException extends RuntimeException {

        private ItemResourceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(ItemResource.class);

    private static final String ENTITY_NAME = "item";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemService itemService;

    private final ItemRepository itemRepository;

    private final ItemQueryService itemQueryService;

    private final ItemMapper itemMapper;

    private final UserService userService;

    public ItemResource(
        ItemService itemService,
        ItemRepository itemRepository,
        ItemQueryService itemQueryService,
        ItemMapper itemMapper,
        UserService userService
    ) {
        this.itemService = itemService;
        this.itemRepository = itemRepository;
        this.itemQueryService = itemQueryService;
        this.itemMapper = itemMapper;
        this.userService = userService;
    }

    /**
     * {@code POST  /items} : Create a new item.
     *
     * @param item the item to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new item, or with status {@code 400 (Bad Request)} if the item has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/items")
    public ResponseEntity<Item> createItem(@Valid @RequestBody Item item) throws URISyntaxException {
        log.debug("REST request to save Item : {}", item);
        if (item.getId() != null) {
            throw new BadRequestAlertException("A new item cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Item result = itemService.save(item);
        return ResponseEntity
            .created(new URI("/api/items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /items/:id} : Updates an existing item.
     *
     * @param id the id of the item to save.
     * @param item the item to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated item,
     * or with status {@code 400 (Bad Request)} if the item is not valid,
     * or with status {@code 500 (Internal Server Error)} if the item couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/items/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable(value = "id", required = false) final UUID id, @Valid @RequestBody Item item)
        throws URISyntaxException {
        log.debug("REST request to update Item : {}, {}", id, item);
        if (item.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, item.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Item result = itemService.save(item);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, item.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /items/:id/reserve/:quantity} : Reserves an item.
     *
     * @param principal {@link Principal}
     * @param id the id of the item to save.
     * @param quantity the item quantity to reserver.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated item,
     * or with status {@code 400 (Bad Request)} if the item is not valid,
     * or with status {@code 500 (Internal Server Error)} if the item couldn't be updated.
     */
    @PutMapping("/items/{id}/reserve/{quantity}")
    public ResponseEntity<ItemDetailDTO> reserveItem(
        Principal principal,
        @PathVariable(value = "id") final UUID id,
        @PathVariable(value = "quantity") @Min(1) final Integer quantity
    ) {
        log.debug("Request to reserve {} Item(s): {} by User: {}", quantity, id, principal);

        var user = userService.getUserWithAuthorities();
        if (user.isEmpty()) throw new ItemResourceException("Current User could not be found");

        var item = itemService.reserveItem(id, quantity, user.get());
        return ResponseEntity.ok().body(item);
    }

    /**
     * {@code PATCH  /items/:id} : Partial updates given fields of an existing item, field will ignore if it is null
     *
     * @param id the id of the item to save.
     * @param item the item to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated item,
     * or with status {@code 400 (Bad Request)} if the item is not valid,
     * or with status {@code 404 (Not Found)} if the item is not found,
     * or with status {@code 500 (Internal Server Error)} if the item couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/items/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Item> partialUpdateItem(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody Item item
    ) throws URISyntaxException {
        log.debug("REST request to partial update Item partially : {}, {}", id, item);
        if (item.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, item.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Item> result = itemService.partialUpdate(item);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, item.getId().toString())
        );
    }

    /**
     * {@code GET  /items} : get all the items.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of items in body.
     */
    @GetMapping("/items")
    public ResponseEntity<List<Item>> getAllItems(ItemCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Items by criteria: {}", criteria);
        Page<Item> page = itemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /items} : get all the items for list page.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of items in body.
     */
    @GetMapping("/items/list-page")
    public ResponseEntity<List<ListItemDTO>> getListPageItems(ItemCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Items by criteria: {}", criteria);
        Page<Item> page = itemQueryService.findByCriteriaEagerLoad(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(itemMapper.itemsToItemDTOs(page.getContent()));
    }

    /**
     * {@code GET  /items/count} : count all the items.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/items/count")
    public ResponseEntity<Long> countItems(ItemCriteria criteria) {
        log.debug("REST request to count Items by criteria: {}", criteria);
        return ResponseEntity.ok().body(itemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /items/:id} : get the "id" item.
     *
     * @param id the id of the item to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the item, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/items/{id}")
    public ResponseEntity<Item> getItem(@PathVariable UUID id) {
        log.debug("REST request to get Item : {}", id);
        Optional<Item> item = itemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(item);
    }

    /**
     * {@code GET  /items/:id/detail} : get the "id" item.
     *
     * @param id the id of the item to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the item, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/items/{id}/detail")
    public ResponseEntity<ItemDetailDTO> getItemDetails(@PathVariable UUID id) {
        log.debug("REST request to get Item : {}", id);
        Optional<ItemDetailDTO> item = itemService.findOneWithAvailableTokens(id);
        return ResponseUtil.wrapOrNotFound(item);
    }

    /**
     * {@code DELETE  /items/:id} : delete the "id" item.
     *
     * @param id the id of the item to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable UUID id) {
        log.debug("REST request to delete Item : {}", id);
        itemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
