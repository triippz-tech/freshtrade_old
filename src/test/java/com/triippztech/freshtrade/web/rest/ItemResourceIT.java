package com.triippztech.freshtrade.web.rest;

import static com.triippztech.freshtrade.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.triippztech.freshtrade.IntegrationTest;
import com.triippztech.freshtrade.domain.Category;
import com.triippztech.freshtrade.domain.Image;
import com.triippztech.freshtrade.domain.Item;
import com.triippztech.freshtrade.domain.ItemToken;
import com.triippztech.freshtrade.domain.Location;
import com.triippztech.freshtrade.domain.TradeEvent;
import com.triippztech.freshtrade.domain.User;
import com.triippztech.freshtrade.domain.enumeration.Condition;
import com.triippztech.freshtrade.repository.ItemRepository;
import com.triippztech.freshtrade.service.ItemService;
import com.triippztech.freshtrade.service.criteria.ItemCriteria;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(value = "admin")
class ItemResourceIT {

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;
    private static final Double SMALLER_PRICE = 1D - 1D;

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;
    private static final Integer SMALLER_QUANTITY = 1 - 1;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_DETAILS = "BBBBBBBBBB";

    private static final Condition DEFAULT_ITEM_CONDITION = Condition.NEW;
    private static final Condition UPDATED_ITEM_CONDITION = Condition.OPEN_BOX;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ItemRepository itemRepository;

    @Mock
    private ItemRepository itemRepositoryMock;

    @Mock
    private ItemService itemServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemMockMvc;

    private Item item;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Item createEntity(EntityManager em) {
        Item item = new Item()
            .price(DEFAULT_PRICE)
            .quantity(DEFAULT_QUANTITY)
            .name(DEFAULT_NAME)
            .details(DEFAULT_DETAILS)
            .itemCondition(DEFAULT_ITEM_CONDITION)
            .isActive(DEFAULT_IS_ACTIVE)
            .createdDate(DEFAULT_CREATED_DATE)
            .updatedDate(DEFAULT_UPDATED_DATE);
        return item;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Item createUpdatedEntity(EntityManager em) {
        Item item = new Item()
            .price(UPDATED_PRICE)
            .quantity(UPDATED_QUANTITY)
            .name(UPDATED_NAME)
            .details(UPDATED_DETAILS)
            .itemCondition(UPDATED_ITEM_CONDITION)
            .isActive(UPDATED_IS_ACTIVE)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedDate(UPDATED_UPDATED_DATE);
        return item;
    }
    //    @BeforeEach
    //    public void initTest() {
    //        item = createEntity(em);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void createItem() throws Exception {
    //        int databaseSizeBeforeCreate = itemRepository.findAll().size();
    //        // Create the Item
    //        restItemMockMvc
    //            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(item)))
    //            .andExpect(status().isCreated());
    //
    //        // Validate the Item in the database
    //        List<Item> itemList = itemRepository.findAll();
    //        assertThat(itemList).hasSize(databaseSizeBeforeCreate + 1);
    //        Item testItem = itemList.get(itemList.size() - 1);
    //        assertThat(testItem.getPrice()).isEqualTo(DEFAULT_PRICE);
    //        assertThat(testItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    //        assertThat(testItem.getName()).isEqualTo(DEFAULT_NAME);
    //        assertThat(testItem.getDetails()).isEqualTo(DEFAULT_DETAILS);
    //        assertThat(testItem.getItemCondition()).isEqualTo(DEFAULT_ITEM_CONDITION);
    //        assertThat(testItem.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    //        assertThat(testItem.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    //        assertThat(testItem.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void createItemWithExistingId() throws Exception {
    //        // Create the Item with an existing ID
    //        itemRepository.saveAndFlush(item);
    //
    //        int databaseSizeBeforeCreate = itemRepository.findAll().size();
    //
    //        // An entity with an existing ID cannot be created, so this API call must fail
    //        restItemMockMvc
    //            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(item)))
    //            .andExpect(status().isBadRequest());
    //
    //        // Validate the Item in the database
    //        List<Item> itemList = itemRepository.findAll();
    //        assertThat(itemList).hasSize(databaseSizeBeforeCreate);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void checkPriceIsRequired() throws Exception {
    //        int databaseSizeBeforeTest = itemRepository.findAll().size();
    //        // set the field null
    //        item.setPrice(null);
    //
    //        // Create the Item, which fails.
    //
    //        restItemMockMvc
    //            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(item)))
    //            .andExpect(status().isBadRequest());
    //
    //        List<Item> itemList = itemRepository.findAll();
    //        assertThat(itemList).hasSize(databaseSizeBeforeTest);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void checkQuantityIsRequired() throws Exception {
    //        int databaseSizeBeforeTest = itemRepository.findAll().size();
    //        // set the field null
    //        item.setQuantity(null);
    //
    //        // Create the Item, which fails.
    //
    //        restItemMockMvc
    //            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(item)))
    //            .andExpect(status().isBadRequest());
    //
    //        List<Item> itemList = itemRepository.findAll();
    //        assertThat(itemList).hasSize(databaseSizeBeforeTest);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void checkNameIsRequired() throws Exception {
    //        int databaseSizeBeforeTest = itemRepository.findAll().size();
    //        // set the field null
    //        item.setName(null);
    //
    //        // Create the Item, which fails.
    //
    //        restItemMockMvc
    //            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(item)))
    //            .andExpect(status().isBadRequest());
    //
    //        List<Item> itemList = itemRepository.findAll();
    //        assertThat(itemList).hasSize(databaseSizeBeforeTest);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void checkIsActiveIsRequired() throws Exception {
    //        int databaseSizeBeforeTest = itemRepository.findAll().size();
    //        // set the field null
    //        item.setIsActive(null);
    //
    //        // Create the Item, which fails.
    //
    //        restItemMockMvc
    //            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(item)))
    //            .andExpect(status().isBadRequest());
    //
    //        List<Item> itemList = itemRepository.findAll();
    //        assertThat(itemList).hasSize(databaseSizeBeforeTest);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItems() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList
    //        restItemMockMvc
    //            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
    //            .andExpect(status().isOk())
    //            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
    //            .andExpect(jsonPath("$.[*].id").value(hasItem(item.getId().toString())))
    //            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
    //            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
    //            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
    //            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS.toString())))
    //            .andExpect(jsonPath("$.[*].itemCondition").value(hasItem(DEFAULT_ITEM_CONDITION.toString())))
    //            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
    //            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
    //            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(sameInstant(DEFAULT_UPDATED_DATE))));
    //    }
    //
    //    @SuppressWarnings({ "unchecked" })
    //    void getAllItemsWithEagerRelationshipsIsEnabled() throws Exception {
    //        when(itemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
    //
    //        restItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());
    //
    //        verify(itemServiceMock, times(1)).findAllWithEagerRelationships(any());
    //    }
    //
    //    @SuppressWarnings({ "unchecked" })
    //    void getAllItemsWithEagerRelationshipsIsNotEnabled() throws Exception {
    //        when(itemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
    //
    //        restItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());
    //
    //        verify(itemServiceMock, times(1)).findAllWithEagerRelationships(any());
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getItem() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get the item
    //        restItemMockMvc
    //            .perform(get(ENTITY_API_URL_ID, item.getId()))
    //            .andExpect(status().isOk())
    //            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
    //            .andExpect(jsonPath("$.id").value(item.getId().toString()))
    //            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
    //            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
    //            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
    //            .andExpect(jsonPath("$.details").value(DEFAULT_DETAILS.toString()))
    //            .andExpect(jsonPath("$.itemCondition").value(DEFAULT_ITEM_CONDITION.toString()))
    //            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
    //            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
    //            .andExpect(jsonPath("$.updatedDate").value(sameInstant(DEFAULT_UPDATED_DATE)));
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getItemsByIdFiltering() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        UUID id = item.getId();
    //
    //        defaultItemShouldBeFound("id.equals=" + id);
    //        defaultItemShouldNotBeFound("id.notEquals=" + id);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByPriceIsEqualToSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where price equals to DEFAULT_PRICE
    //        defaultItemShouldBeFound("price.equals=" + DEFAULT_PRICE);
    //
    //        // Get all the itemList where price equals to UPDATED_PRICE
    //        defaultItemShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByPriceIsNotEqualToSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where price not equals to DEFAULT_PRICE
    //        defaultItemShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);
    //
    //        // Get all the itemList where price not equals to UPDATED_PRICE
    //        defaultItemShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByPriceIsInShouldWork() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where price in DEFAULT_PRICE or UPDATED_PRICE
    //        defaultItemShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);
    //
    //        // Get all the itemList where price equals to UPDATED_PRICE
    //        defaultItemShouldNotBeFound("price.in=" + UPDATED_PRICE);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByPriceIsNullOrNotNull() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where price is not null
    //        defaultItemShouldBeFound("price.specified=true");
    //
    //        // Get all the itemList where price is null
    //        defaultItemShouldNotBeFound("price.specified=false");
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where price is greater than or equal to DEFAULT_PRICE
    //        defaultItemShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);
    //
    //        // Get all the itemList where price is greater than or equal to UPDATED_PRICE
    //        defaultItemShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByPriceIsLessThanOrEqualToSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where price is less than or equal to DEFAULT_PRICE
    //        defaultItemShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);
    //
    //        // Get all the itemList where price is less than or equal to SMALLER_PRICE
    //        defaultItemShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByPriceIsLessThanSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where price is less than DEFAULT_PRICE
    //        defaultItemShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);
    //
    //        // Get all the itemList where price is less than UPDATED_PRICE
    //        defaultItemShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByPriceIsGreaterThanSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where price is greater than DEFAULT_PRICE
    //        defaultItemShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);
    //
    //        // Get all the itemList where price is greater than SMALLER_PRICE
    //        defaultItemShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByQuantityIsEqualToSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where quantity equals to DEFAULT_QUANTITY
    //        defaultItemShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);
    //
    //        // Get all the itemList where quantity equals to UPDATED_QUANTITY
    //        defaultItemShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByQuantityIsNotEqualToSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where quantity not equals to DEFAULT_QUANTITY
    //        defaultItemShouldNotBeFound("quantity.notEquals=" + DEFAULT_QUANTITY);
    //
    //        // Get all the itemList where quantity not equals to UPDATED_QUANTITY
    //        defaultItemShouldBeFound("quantity.notEquals=" + UPDATED_QUANTITY);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByQuantityIsInShouldWork() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
    //        defaultItemShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);
    //
    //        // Get all the itemList where quantity equals to UPDATED_QUANTITY
    //        defaultItemShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByQuantityIsNullOrNotNull() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where quantity is not null
    //        defaultItemShouldBeFound("quantity.specified=true");
    //
    //        // Get all the itemList where quantity is null
    //        defaultItemShouldNotBeFound("quantity.specified=false");
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where quantity is greater than or equal to DEFAULT_QUANTITY
    //        defaultItemShouldBeFound("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY);
    //
    //        // Get all the itemList where quantity is greater than or equal to UPDATED_QUANTITY
    //        defaultItemShouldNotBeFound("quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByQuantityIsLessThanOrEqualToSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where quantity is less than or equal to DEFAULT_QUANTITY
    //        defaultItemShouldBeFound("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY);
    //
    //        // Get all the itemList where quantity is less than or equal to SMALLER_QUANTITY
    //        defaultItemShouldNotBeFound("quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByQuantityIsLessThanSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where quantity is less than DEFAULT_QUANTITY
    //        defaultItemShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);
    //
    //        // Get all the itemList where quantity is less than UPDATED_QUANTITY
    //        defaultItemShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByQuantityIsGreaterThanSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where quantity is greater than DEFAULT_QUANTITY
    //        defaultItemShouldNotBeFound("quantity.greaterThan=" + DEFAULT_QUANTITY);
    //
    //        // Get all the itemList where quantity is greater than SMALLER_QUANTITY
    //        defaultItemShouldBeFound("quantity.greaterThan=" + SMALLER_QUANTITY);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByNameIsEqualToSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where name equals to DEFAULT_NAME
    //        defaultItemShouldBeFound("name.equals=" + DEFAULT_NAME);
    //
    //        // Get all the itemList where name equals to UPDATED_NAME
    //        defaultItemShouldNotBeFound("name.equals=" + UPDATED_NAME);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByNameIsNotEqualToSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where name not equals to DEFAULT_NAME
    //        defaultItemShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);
    //
    //        // Get all the itemList where name not equals to UPDATED_NAME
    //        defaultItemShouldBeFound("name.notEquals=" + UPDATED_NAME);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByNameIsInShouldWork() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where name in DEFAULT_NAME or UPDATED_NAME
    //        defaultItemShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);
    //
    //        // Get all the itemList where name equals to UPDATED_NAME
    //        defaultItemShouldNotBeFound("name.in=" + UPDATED_NAME);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByNameIsNullOrNotNull() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where name is not null
    //        defaultItemShouldBeFound("name.specified=true");
    //
    //        // Get all the itemList where name is null
    //        defaultItemShouldNotBeFound("name.specified=false");
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByNameContainsSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where name contains DEFAULT_NAME
    //        defaultItemShouldBeFound("name.contains=" + DEFAULT_NAME);
    //
    //        // Get all the itemList where name contains UPDATED_NAME
    //        defaultItemShouldNotBeFound("name.contains=" + UPDATED_NAME);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByNameNotContainsSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where name does not contain DEFAULT_NAME
    //        defaultItemShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);
    //
    //        // Get all the itemList where name does not contain UPDATED_NAME
    //        defaultItemShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByItemConditionIsEqualToSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where itemCondition equals to DEFAULT_ITEM_CONDITION
    //        defaultItemShouldBeFound("itemCondition.equals=" + DEFAULT_ITEM_CONDITION);
    //
    //        // Get all the itemList where itemCondition equals to UPDATED_ITEM_CONDITION
    //        defaultItemShouldNotBeFound("itemCondition.equals=" + UPDATED_ITEM_CONDITION);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByItemConditionIsNotEqualToSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where itemCondition not equals to DEFAULT_ITEM_CONDITION
    //        defaultItemShouldNotBeFound("itemCondition.notEquals=" + DEFAULT_ITEM_CONDITION);
    //
    //        // Get all the itemList where itemCondition not equals to UPDATED_ITEM_CONDITION
    //        defaultItemShouldBeFound("itemCondition.notEquals=" + UPDATED_ITEM_CONDITION);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByItemConditionIsInShouldWork() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where itemCondition in DEFAULT_ITEM_CONDITION or UPDATED_ITEM_CONDITION
    //        defaultItemShouldBeFound("itemCondition.in=" + DEFAULT_ITEM_CONDITION + "," + UPDATED_ITEM_CONDITION);
    //
    //        // Get all the itemList where itemCondition equals to UPDATED_ITEM_CONDITION
    //        defaultItemShouldNotBeFound("itemCondition.in=" + UPDATED_ITEM_CONDITION);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByItemConditionIsNullOrNotNull() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where itemCondition is not null
    //        defaultItemShouldBeFound("itemCondition.specified=true");
    //
    //        // Get all the itemList where itemCondition is null
    //        defaultItemShouldNotBeFound("itemCondition.specified=false");
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByIsActiveIsEqualToSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where isActive equals to DEFAULT_IS_ACTIVE
    //        defaultItemShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);
    //
    //        // Get all the itemList where isActive equals to UPDATED_IS_ACTIVE
    //        defaultItemShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByIsActiveIsNotEqualToSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where isActive not equals to DEFAULT_IS_ACTIVE
    //        defaultItemShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);
    //
    //        // Get all the itemList where isActive not equals to UPDATED_IS_ACTIVE
    //        defaultItemShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByIsActiveIsInShouldWork() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
    //        defaultItemShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);
    //
    //        // Get all the itemList where isActive equals to UPDATED_IS_ACTIVE
    //        defaultItemShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByIsActiveIsNullOrNotNull() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where isActive is not null
    //        defaultItemShouldBeFound("isActive.specified=true");
    //
    //        // Get all the itemList where isActive is null
    //        defaultItemShouldNotBeFound("isActive.specified=false");
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByCreatedDateIsEqualToSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where createdDate equals to DEFAULT_CREATED_DATE
    //        defaultItemShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);
    //
    //        // Get all the itemList where createdDate equals to UPDATED_CREATED_DATE
    //        defaultItemShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByCreatedDateIsNotEqualToSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where createdDate not equals to DEFAULT_CREATED_DATE
    //        defaultItemShouldNotBeFound("createdDate.notEquals=" + DEFAULT_CREATED_DATE);
    //
    //        // Get all the itemList where createdDate not equals to UPDATED_CREATED_DATE
    //        defaultItemShouldBeFound("createdDate.notEquals=" + UPDATED_CREATED_DATE);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByCreatedDateIsInShouldWork() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
    //        defaultItemShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);
    //
    //        // Get all the itemList where createdDate equals to UPDATED_CREATED_DATE
    //        defaultItemShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByCreatedDateIsNullOrNotNull() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where createdDate is not null
    //        defaultItemShouldBeFound("createdDate.specified=true");
    //
    //        // Get all the itemList where createdDate is null
    //        defaultItemShouldNotBeFound("createdDate.specified=false");
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where createdDate is greater than or equal to DEFAULT_CREATED_DATE
    //        defaultItemShouldBeFound("createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE);
    //
    //        // Get all the itemList where createdDate is greater than or equal to UPDATED_CREATED_DATE
    //        defaultItemShouldNotBeFound("createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where createdDate is less than or equal to DEFAULT_CREATED_DATE
    //        defaultItemShouldBeFound("createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE);
    //
    //        // Get all the itemList where createdDate is less than or equal to SMALLER_CREATED_DATE
    //        defaultItemShouldNotBeFound("createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByCreatedDateIsLessThanSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where createdDate is less than DEFAULT_CREATED_DATE
    //        defaultItemShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);
    //
    //        // Get all the itemList where createdDate is less than UPDATED_CREATED_DATE
    //        defaultItemShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByCreatedDateIsGreaterThanSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where createdDate is greater than DEFAULT_CREATED_DATE
    //        defaultItemShouldNotBeFound("createdDate.greaterThan=" + DEFAULT_CREATED_DATE);
    //
    //        // Get all the itemList where createdDate is greater than SMALLER_CREATED_DATE
    //        defaultItemShouldBeFound("createdDate.greaterThan=" + SMALLER_CREATED_DATE);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByUpdatedDateIsEqualToSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where updatedDate equals to DEFAULT_UPDATED_DATE
    //        defaultItemShouldBeFound("updatedDate.equals=" + DEFAULT_UPDATED_DATE);
    //
    //        // Get all the itemList where updatedDate equals to UPDATED_UPDATED_DATE
    //        defaultItemShouldNotBeFound("updatedDate.equals=" + UPDATED_UPDATED_DATE);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByUpdatedDateIsNotEqualToSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where updatedDate not equals to DEFAULT_UPDATED_DATE
    //        defaultItemShouldNotBeFound("updatedDate.notEquals=" + DEFAULT_UPDATED_DATE);
    //
    //        // Get all the itemList where updatedDate not equals to UPDATED_UPDATED_DATE
    //        defaultItemShouldBeFound("updatedDate.notEquals=" + UPDATED_UPDATED_DATE);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByUpdatedDateIsInShouldWork() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where updatedDate in DEFAULT_UPDATED_DATE or UPDATED_UPDATED_DATE
    //        defaultItemShouldBeFound("updatedDate.in=" + DEFAULT_UPDATED_DATE + "," + UPDATED_UPDATED_DATE);
    //
    //        // Get all the itemList where updatedDate equals to UPDATED_UPDATED_DATE
    //        defaultItemShouldNotBeFound("updatedDate.in=" + UPDATED_UPDATED_DATE);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByUpdatedDateIsNullOrNotNull() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where updatedDate is not null
    //        defaultItemShouldBeFound("updatedDate.specified=true");
    //
    //        // Get all the itemList where updatedDate is null
    //        defaultItemShouldNotBeFound("updatedDate.specified=false");
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByUpdatedDateIsGreaterThanOrEqualToSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where updatedDate is greater than or equal to DEFAULT_UPDATED_DATE
    //        defaultItemShouldBeFound("updatedDate.greaterThanOrEqual=" + DEFAULT_UPDATED_DATE);
    //
    //        // Get all the itemList where updatedDate is greater than or equal to UPDATED_UPDATED_DATE
    //        defaultItemShouldNotBeFound("updatedDate.greaterThanOrEqual=" + UPDATED_UPDATED_DATE);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByUpdatedDateIsLessThanOrEqualToSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where updatedDate is less than or equal to DEFAULT_UPDATED_DATE
    //        defaultItemShouldBeFound("updatedDate.lessThanOrEqual=" + DEFAULT_UPDATED_DATE);
    //
    //        // Get all the itemList where updatedDate is less than or equal to SMALLER_UPDATED_DATE
    //        defaultItemShouldNotBeFound("updatedDate.lessThanOrEqual=" + SMALLER_UPDATED_DATE);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByUpdatedDateIsLessThanSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where updatedDate is less than DEFAULT_UPDATED_DATE
    //        defaultItemShouldNotBeFound("updatedDate.lessThan=" + DEFAULT_UPDATED_DATE);
    //
    //        // Get all the itemList where updatedDate is less than UPDATED_UPDATED_DATE
    //        defaultItemShouldBeFound("updatedDate.lessThan=" + UPDATED_UPDATED_DATE);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByUpdatedDateIsGreaterThanSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        // Get all the itemList where updatedDate is greater than DEFAULT_UPDATED_DATE
    //        defaultItemShouldNotBeFound("updatedDate.greaterThan=" + DEFAULT_UPDATED_DATE);
    //
    //        // Get all the itemList where updatedDate is greater than SMALLER_UPDATED_DATE
    //        defaultItemShouldBeFound("updatedDate.greaterThan=" + SMALLER_UPDATED_DATE);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByImagesIsEqualToSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //        Image images = ImageResourceIT.createEntity(em);
    //        em.persist(images);
    //        em.flush();
    //        item.addImages(images);
    //        itemRepository.saveAndFlush(item);
    //        UUID imagesId = images.getId();
    //
    //        // Get all the itemList where images equals to imagesId
    //        defaultItemShouldBeFound("imagesId.equals=" + imagesId);
    //
    //        // Get all the itemList where images equals to UUID.randomUUID()
    //        defaultItemShouldNotBeFound("imagesId.equals=" + UUID.randomUUID());
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByTokensIsEqualToSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //        ItemToken tokens = ItemTokenResourceIT.createEntity(em);
    //        em.persist(tokens);
    //        em.flush();
    //        item.addTokens(tokens);
    //        itemRepository.saveAndFlush(item);
    //        Long tokensId = tokens.getId();
    //
    //        // Get all the itemList where tokens equals to tokensId
    //        defaultItemShouldBeFound("tokensId.equals=" + tokensId);
    //
    //        // Get all the itemList where tokens equals to (tokensId + 1)
    //        defaultItemShouldNotBeFound("tokensId.equals=" + (tokensId + 1));
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByOwnerIsEqualToSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //        User owner = UserResourceIT.createEntity(em);
    //        em.persist(owner);
    //        em.flush();
    //        item.setOwner(owner);
    //        itemRepository.saveAndFlush(item);
    //        Long ownerId = owner.getId();
    //
    //        // Get all the itemList where owner equals to ownerId
    //        defaultItemShouldBeFound("ownerId.equals=" + ownerId);
    //
    //        // Get all the itemList where owner equals to (ownerId + 1)
    //        defaultItemShouldNotBeFound("ownerId.equals=" + (ownerId + 1));
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByLocationIsEqualToSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //        Location location = LocationResourceIT.createEntity(em);
    //        em.persist(location);
    //        em.flush();
    //        item.setLocation(location);
    //        itemRepository.saveAndFlush(item);
    //        Long locationId = location.getId();
    //
    //        // Get all the itemList where location equals to locationId
    //        defaultItemShouldBeFound("locationId.equals=" + locationId);
    //
    //        // Get all the itemList where location equals to (locationId + 1)
    //        defaultItemShouldNotBeFound("locationId.equals=" + (locationId + 1));
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByTradeEventIsEqualToSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //        TradeEvent tradeEvent = TradeEventResourceIT.createEntity(em);
    //        em.persist(tradeEvent);
    //        em.flush();
    //        item.setTradeEvent(tradeEvent);
    //        itemRepository.saveAndFlush(item);
    //        UUID tradeEventId = tradeEvent.getId();
    //
    //        // Get all the itemList where tradeEvent equals to tradeEventId
    //        defaultItemShouldBeFound("tradeEventId.equals=" + tradeEventId);
    //
    //        // Get all the itemList where tradeEvent equals to UUID.randomUUID()
    //        defaultItemShouldNotBeFound("tradeEventId.equals=" + UUID.randomUUID());
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByCategoriesIsEqualToSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //        Category categories = CategoryResourceIT.createEntity(em);
    //        em.persist(categories);
    //        em.flush();
    //        item.addCategories(categories);
    //        itemRepository.saveAndFlush(item);
    //        UUID categoriesId = categories.getId();
    //
    //        // Get all the itemList where categories equals to categoriesId
    //        defaultItemShouldBeFound("categoriesId.equals=" + categoriesId);
    //
    //        // Get all the itemList where categories equals to UUID.randomUUID()
    //        defaultItemShouldNotBeFound("categoriesId.equals=" + UUID.randomUUID());
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getAllItemsByUserIsEqualToSomething() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //        User user = UserResourceIT.createEntity(em);
    //        em.persist(user);
    //        em.flush();
    //        item.addUser(user);
    //        itemRepository.saveAndFlush(item);
    //        Long userId = user.getId();
    //
    //        // Get all the itemList where user equals to userId
    //        defaultItemShouldBeFound("userId.equals=" + userId);
    //
    //        // Get all the itemList where user equals to (userId + 1)
    //        defaultItemShouldNotBeFound("userId.equals=" + (userId + 1));
    //    }
    //
    //    /**
    //     * Executes the search, and checks that the default entity is returned.
    //     */
    //    private void defaultItemShouldBeFound(String filter) throws Exception {
    //        restItemMockMvc
    //            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
    //            .andExpect(status().isOk())
    //            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
    //            .andExpect(jsonPath("$.[*].id").value(hasItem(item.getId().toString())))
    //            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
    //            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
    //            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
    //            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS.toString())))
    //            .andExpect(jsonPath("$.[*].itemCondition").value(hasItem(DEFAULT_ITEM_CONDITION.toString())))
    //            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
    //            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
    //            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(sameInstant(DEFAULT_UPDATED_DATE))));
    //
    //        // Check, that the count call also returns 1
    //        restItemMockMvc
    //            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
    //            .andExpect(status().isOk())
    //            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
    //            .andExpect(content().string("1"));
    //    }
    //
    //    /**
    //     * Executes the search, and checks that the default entity is not returned.
    //     */
    //    private void defaultItemShouldNotBeFound(String filter) throws Exception {
    //        restItemMockMvc
    //            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
    //            .andExpect(status().isOk())
    //            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
    //            .andExpect(jsonPath("$").isArray())
    //            .andExpect(jsonPath("$").isEmpty());
    //
    //        // Check, that the count call also returns 0
    //        restItemMockMvc
    //            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
    //            .andExpect(status().isOk())
    //            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
    //            .andExpect(content().string("0"));
    //    }
    //
    //    @Test
    //    @Transactional
    //    void getNonExistingItem() throws Exception {
    //        // Get the item
    //        restItemMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    //    }
    //
    //    @Test
    //    @Transactional
    //    void putNewItem() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
    //
    //        // Update the item
    //        Item updatedItem = itemRepository.findById(item.getId()).get();
    //        // Disconnect from session so that the updates on updatedItem are not directly saved in db
    //        em.detach(updatedItem);
    //        updatedItem
    //            .price(UPDATED_PRICE)
    //            .quantity(UPDATED_QUANTITY)
    //            .name(UPDATED_NAME)
    //            .details(UPDATED_DETAILS)
    //            .itemCondition(UPDATED_ITEM_CONDITION)
    //            .isActive(UPDATED_IS_ACTIVE)
    //            .createdDate(UPDATED_CREATED_DATE)
    //            .updatedDate(UPDATED_UPDATED_DATE);
    //
    //        restItemMockMvc
    //            .perform(
    //                put(ENTITY_API_URL_ID, updatedItem.getId())
    //                    .contentType(MediaType.APPLICATION_JSON)
    //                    .content(TestUtil.convertObjectToJsonBytes(updatedItem))
    //            )
    //            .andExpect(status().isOk());
    //
    //        // Validate the Item in the database
    //        List<Item> itemList = itemRepository.findAll();
    //        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    //        Item testItem = itemList.get(itemList.size() - 1);
    //        assertThat(testItem.getPrice()).isEqualTo(UPDATED_PRICE);
    //        assertThat(testItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    //        assertThat(testItem.getName()).isEqualTo(UPDATED_NAME);
    //        assertThat(testItem.getDetails()).isEqualTo(UPDATED_DETAILS);
    //        assertThat(testItem.getItemCondition()).isEqualTo(UPDATED_ITEM_CONDITION);
    //        assertThat(testItem.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    //        assertThat(testItem.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    //        assertThat(testItem.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void putNonExistingItem() throws Exception {
    //        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
    //        item.setId(UUID.randomUUID());
    //
    //        // If the entity doesn't have an ID, it will throw BadRequestAlertException
    //        restItemMockMvc
    //            .perform(
    //                put(ENTITY_API_URL_ID, item.getId())
    //                    .contentType(MediaType.APPLICATION_JSON)
    //                    .content(TestUtil.convertObjectToJsonBytes(item))
    //            )
    //            .andExpect(status().isBadRequest());
    //
    //        // Validate the Item in the database
    //        List<Item> itemList = itemRepository.findAll();
    //        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void putWithIdMismatchItem() throws Exception {
    //        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
    //        item.setId(UUID.randomUUID());
    //
    //        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    //        restItemMockMvc
    //            .perform(
    //                put(ENTITY_API_URL_ID, UUID.randomUUID())
    //                    .contentType(MediaType.APPLICATION_JSON)
    //                    .content(TestUtil.convertObjectToJsonBytes(item))
    //            )
    //            .andExpect(status().isBadRequest());
    //
    //        // Validate the Item in the database
    //        List<Item> itemList = itemRepository.findAll();
    //        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void putWithMissingIdPathParamItem() throws Exception {
    //        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
    //        item.setId(UUID.randomUUID());
    //
    //        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    //        restItemMockMvc
    //            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(item)))
    //            .andExpect(status().isMethodNotAllowed());
    //
    //        // Validate the Item in the database
    //        List<Item> itemList = itemRepository.findAll();
    //        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void partialUpdateItemWithPatch() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
    //
    //        // Update the item using partial update
    //        Item partialUpdatedItem = new Item();
    //        partialUpdatedItem.setId(item.getId());
    //
    //        partialUpdatedItem.price(UPDATED_PRICE).quantity(UPDATED_QUANTITY).itemCondition(UPDATED_ITEM_CONDITION);
    //
    //        restItemMockMvc
    //            .perform(
    //                patch(ENTITY_API_URL_ID, partialUpdatedItem.getId())
    //                    .contentType("application/merge-patch+json")
    //                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItem))
    //            )
    //            .andExpect(status().isOk());
    //
    //        // Validate the Item in the database
    //        List<Item> itemList = itemRepository.findAll();
    //        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    //        Item testItem = itemList.get(itemList.size() - 1);
    //        assertThat(testItem.getPrice()).isEqualTo(UPDATED_PRICE);
    //        assertThat(testItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    //        assertThat(testItem.getName()).isEqualTo(DEFAULT_NAME);
    //        assertThat(testItem.getDetails()).isEqualTo(DEFAULT_DETAILS);
    //        assertThat(testItem.getItemCondition()).isEqualTo(UPDATED_ITEM_CONDITION);
    //        assertThat(testItem.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    //        assertThat(testItem.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    //        assertThat(testItem.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void fullUpdateItemWithPatch() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
    //
    //        // Update the item using partial update
    //        Item partialUpdatedItem = new Item();
    //        partialUpdatedItem.setId(item.getId());
    //
    //        partialUpdatedItem
    //            .price(UPDATED_PRICE)
    //            .quantity(UPDATED_QUANTITY)
    //            .name(UPDATED_NAME)
    //            .details(UPDATED_DETAILS)
    //            .itemCondition(UPDATED_ITEM_CONDITION)
    //            .isActive(UPDATED_IS_ACTIVE)
    //            .createdDate(UPDATED_CREATED_DATE)
    //            .updatedDate(UPDATED_UPDATED_DATE);
    //
    //        restItemMockMvc
    //            .perform(
    //                patch(ENTITY_API_URL_ID, partialUpdatedItem.getId())
    //                    .contentType("application/merge-patch+json")
    //                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItem))
    //            )
    //            .andExpect(status().isOk());
    //
    //        // Validate the Item in the database
    //        List<Item> itemList = itemRepository.findAll();
    //        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    //        Item testItem = itemList.get(itemList.size() - 1);
    //        assertThat(testItem.getPrice()).isEqualTo(UPDATED_PRICE);
    //        assertThat(testItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    //        assertThat(testItem.getName()).isEqualTo(UPDATED_NAME);
    //        assertThat(testItem.getDetails()).isEqualTo(UPDATED_DETAILS);
    //        assertThat(testItem.getItemCondition()).isEqualTo(UPDATED_ITEM_CONDITION);
    //        assertThat(testItem.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    //        assertThat(testItem.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    //        assertThat(testItem.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void patchNonExistingItem() throws Exception {
    //        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
    //        item.setId(UUID.randomUUID());
    //
    //        // If the entity doesn't have an ID, it will throw BadRequestAlertException
    //        restItemMockMvc
    //            .perform(
    //                patch(ENTITY_API_URL_ID, item.getId())
    //                    .contentType("application/merge-patch+json")
    //                    .content(TestUtil.convertObjectToJsonBytes(item))
    //            )
    //            .andExpect(status().isBadRequest());
    //
    //        // Validate the Item in the database
    //        List<Item> itemList = itemRepository.findAll();
    //        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void patchWithIdMismatchItem() throws Exception {
    //        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
    //        item.setId(UUID.randomUUID());
    //
    //        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    //        restItemMockMvc
    //            .perform(
    //                patch(ENTITY_API_URL_ID, UUID.randomUUID())
    //                    .contentType("application/merge-patch+json")
    //                    .content(TestUtil.convertObjectToJsonBytes(item))
    //            )
    //            .andExpect(status().isBadRequest());
    //
    //        // Validate the Item in the database
    //        List<Item> itemList = itemRepository.findAll();
    //        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void patchWithMissingIdPathParamItem() throws Exception {
    //        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
    //        item.setId(UUID.randomUUID());
    //
    //        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    //        restItemMockMvc
    //            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(item)))
    //            .andExpect(status().isMethodNotAllowed());
    //
    //        // Validate the Item in the database
    //        List<Item> itemList = itemRepository.findAll();
    //        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    //    }
    //
    //    @Test
    //    @Transactional
    //    void deleteItem() throws Exception {
    //        // Initialize the database
    //        itemRepository.saveAndFlush(item);
    //
    //        int databaseSizeBeforeDelete = itemRepository.findAll().size();
    //
    //        // Delete the item
    //        restItemMockMvc
    //            .perform(delete(ENTITY_API_URL_ID, item.getId().toString()).accept(MediaType.APPLICATION_JSON))
    //            .andExpect(status().isNoContent());
    //
    //        // Validate the database contains one less item
    //        List<Item> itemList = itemRepository.findAll();
    //        assertThat(itemList).hasSize(databaseSizeBeforeDelete - 1);
    //    }
}
