package com.triippztech.freshtrade.web.rest;

import static com.triippztech.freshtrade.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.triippztech.freshtrade.IntegrationTest;
import com.triippztech.freshtrade.domain.Item;
import com.triippztech.freshtrade.domain.ItemToken;
import com.triippztech.freshtrade.domain.User;
import com.triippztech.freshtrade.repository.ItemTokenRepository;
import com.triippztech.freshtrade.service.ItemTokenService;
import com.triippztech.freshtrade.service.criteria.ItemTokenCriteria;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
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

/**
 * Integration tests for the {@link ItemTokenResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ItemTokenResourceIT {

    private static final String DEFAULT_TOKEN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TOKEN_CODE = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN_CODE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/item-tokens";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ItemTokenRepository itemTokenRepository;

    @Mock
    private ItemTokenRepository itemTokenRepositoryMock;

    @Mock
    private ItemTokenService itemTokenServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemTokenMockMvc;

    private ItemToken itemToken;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemToken createEntity(EntityManager em) {
        ItemToken itemToken = new ItemToken()
            .tokenName(DEFAULT_TOKEN_NAME)
            .tokenCode(DEFAULT_TOKEN_CODE)
            .createdDate(DEFAULT_CREATED_DATE)
            .updatedDate(DEFAULT_UPDATED_DATE);
        return itemToken;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemToken createUpdatedEntity(EntityManager em) {
        ItemToken itemToken = new ItemToken()
            .tokenName(UPDATED_TOKEN_NAME)
            .tokenCode(UPDATED_TOKEN_CODE)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedDate(UPDATED_UPDATED_DATE);
        return itemToken;
    }

    @BeforeEach
    public void initTest() {
        itemToken = createEntity(em);
    }

    @Test
    @Transactional
    void createItemToken() throws Exception {
        int databaseSizeBeforeCreate = itemTokenRepository.findAll().size();
        // Create the ItemToken
        restItemTokenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemToken)))
            .andExpect(status().isCreated());

        // Validate the ItemToken in the database
        List<ItemToken> itemTokenList = itemTokenRepository.findAll();
        assertThat(itemTokenList).hasSize(databaseSizeBeforeCreate + 1);
        ItemToken testItemToken = itemTokenList.get(itemTokenList.size() - 1);
        assertThat(testItemToken.getTokenName()).isEqualTo(DEFAULT_TOKEN_NAME);
        assertThat(testItemToken.getTokenCode()).isEqualTo(DEFAULT_TOKEN_CODE);
        assertThat(testItemToken.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testItemToken.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
    }

    @Test
    @Transactional
    void createItemTokenWithExistingId() throws Exception {
        // Create the ItemToken with an existing ID
        itemToken.setId(1L);

        int databaseSizeBeforeCreate = itemTokenRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemTokenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemToken)))
            .andExpect(status().isBadRequest());

        // Validate the ItemToken in the database
        List<ItemToken> itemTokenList = itemTokenRepository.findAll();
        assertThat(itemTokenList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTokenNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemTokenRepository.findAll().size();
        // set the field null
        itemToken.setTokenName(null);

        // Create the ItemToken, which fails.

        restItemTokenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemToken)))
            .andExpect(status().isBadRequest());

        List<ItemToken> itemTokenList = itemTokenRepository.findAll();
        assertThat(itemTokenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTokenCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemTokenRepository.findAll().size();
        // set the field null
        itemToken.setTokenCode(null);

        // Create the ItemToken, which fails.

        restItemTokenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemToken)))
            .andExpect(status().isBadRequest());

        List<ItemToken> itemTokenList = itemTokenRepository.findAll();
        assertThat(itemTokenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllItemTokens() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);

        // Get all the itemTokenList
        restItemTokenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemToken.getId().intValue())))
            .andExpect(jsonPath("$.[*].tokenName").value(hasItem(DEFAULT_TOKEN_NAME)))
            .andExpect(jsonPath("$.[*].tokenCode").value(hasItem(DEFAULT_TOKEN_CODE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(sameInstant(DEFAULT_UPDATED_DATE))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllItemTokensWithEagerRelationshipsIsEnabled() throws Exception {
        when(itemTokenServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restItemTokenMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(itemTokenServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllItemTokensWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(itemTokenServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restItemTokenMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(itemTokenServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getItemToken() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);

        // Get the itemToken
        restItemTokenMockMvc
            .perform(get(ENTITY_API_URL_ID, itemToken.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(itemToken.getId().intValue()))
            .andExpect(jsonPath("$.tokenName").value(DEFAULT_TOKEN_NAME))
            .andExpect(jsonPath("$.tokenCode").value(DEFAULT_TOKEN_CODE))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.updatedDate").value(sameInstant(DEFAULT_UPDATED_DATE)));
    }

    @Test
    @Transactional
    void getItemTokensByIdFiltering() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);

        Long id = itemToken.getId();

        defaultItemTokenShouldBeFound("id.equals=" + id);
        defaultItemTokenShouldNotBeFound("id.notEquals=" + id);

        defaultItemTokenShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultItemTokenShouldNotBeFound("id.greaterThan=" + id);

        defaultItemTokenShouldBeFound("id.lessThanOrEqual=" + id);
        defaultItemTokenShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllItemTokensByTokenNameIsEqualToSomething() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);

        // Get all the itemTokenList where tokenName equals to DEFAULT_TOKEN_NAME
        defaultItemTokenShouldBeFound("tokenName.equals=" + DEFAULT_TOKEN_NAME);

        // Get all the itemTokenList where tokenName equals to UPDATED_TOKEN_NAME
        defaultItemTokenShouldNotBeFound("tokenName.equals=" + UPDATED_TOKEN_NAME);
    }

    @Test
    @Transactional
    void getAllItemTokensByTokenNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);

        // Get all the itemTokenList where tokenName not equals to DEFAULT_TOKEN_NAME
        defaultItemTokenShouldNotBeFound("tokenName.notEquals=" + DEFAULT_TOKEN_NAME);

        // Get all the itemTokenList where tokenName not equals to UPDATED_TOKEN_NAME
        defaultItemTokenShouldBeFound("tokenName.notEquals=" + UPDATED_TOKEN_NAME);
    }

    @Test
    @Transactional
    void getAllItemTokensByTokenNameIsInShouldWork() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);

        // Get all the itemTokenList where tokenName in DEFAULT_TOKEN_NAME or UPDATED_TOKEN_NAME
        defaultItemTokenShouldBeFound("tokenName.in=" + DEFAULT_TOKEN_NAME + "," + UPDATED_TOKEN_NAME);

        // Get all the itemTokenList where tokenName equals to UPDATED_TOKEN_NAME
        defaultItemTokenShouldNotBeFound("tokenName.in=" + UPDATED_TOKEN_NAME);
    }

    @Test
    @Transactional
    void getAllItemTokensByTokenNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);

        // Get all the itemTokenList where tokenName is not null
        defaultItemTokenShouldBeFound("tokenName.specified=true");

        // Get all the itemTokenList where tokenName is null
        defaultItemTokenShouldNotBeFound("tokenName.specified=false");
    }

    @Test
    @Transactional
    void getAllItemTokensByTokenNameContainsSomething() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);

        // Get all the itemTokenList where tokenName contains DEFAULT_TOKEN_NAME
        defaultItemTokenShouldBeFound("tokenName.contains=" + DEFAULT_TOKEN_NAME);

        // Get all the itemTokenList where tokenName contains UPDATED_TOKEN_NAME
        defaultItemTokenShouldNotBeFound("tokenName.contains=" + UPDATED_TOKEN_NAME);
    }

    @Test
    @Transactional
    void getAllItemTokensByTokenNameNotContainsSomething() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);

        // Get all the itemTokenList where tokenName does not contain DEFAULT_TOKEN_NAME
        defaultItemTokenShouldNotBeFound("tokenName.doesNotContain=" + DEFAULT_TOKEN_NAME);

        // Get all the itemTokenList where tokenName does not contain UPDATED_TOKEN_NAME
        defaultItemTokenShouldBeFound("tokenName.doesNotContain=" + UPDATED_TOKEN_NAME);
    }

    @Test
    @Transactional
    void getAllItemTokensByTokenCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);

        // Get all the itemTokenList where tokenCode equals to DEFAULT_TOKEN_CODE
        defaultItemTokenShouldBeFound("tokenCode.equals=" + DEFAULT_TOKEN_CODE);

        // Get all the itemTokenList where tokenCode equals to UPDATED_TOKEN_CODE
        defaultItemTokenShouldNotBeFound("tokenCode.equals=" + UPDATED_TOKEN_CODE);
    }

    @Test
    @Transactional
    void getAllItemTokensByTokenCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);

        // Get all the itemTokenList where tokenCode not equals to DEFAULT_TOKEN_CODE
        defaultItemTokenShouldNotBeFound("tokenCode.notEquals=" + DEFAULT_TOKEN_CODE);

        // Get all the itemTokenList where tokenCode not equals to UPDATED_TOKEN_CODE
        defaultItemTokenShouldBeFound("tokenCode.notEquals=" + UPDATED_TOKEN_CODE);
    }

    @Test
    @Transactional
    void getAllItemTokensByTokenCodeIsInShouldWork() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);

        // Get all the itemTokenList where tokenCode in DEFAULT_TOKEN_CODE or UPDATED_TOKEN_CODE
        defaultItemTokenShouldBeFound("tokenCode.in=" + DEFAULT_TOKEN_CODE + "," + UPDATED_TOKEN_CODE);

        // Get all the itemTokenList where tokenCode equals to UPDATED_TOKEN_CODE
        defaultItemTokenShouldNotBeFound("tokenCode.in=" + UPDATED_TOKEN_CODE);
    }

    @Test
    @Transactional
    void getAllItemTokensByTokenCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);

        // Get all the itemTokenList where tokenCode is not null
        defaultItemTokenShouldBeFound("tokenCode.specified=true");

        // Get all the itemTokenList where tokenCode is null
        defaultItemTokenShouldNotBeFound("tokenCode.specified=false");
    }

    @Test
    @Transactional
    void getAllItemTokensByTokenCodeContainsSomething() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);

        // Get all the itemTokenList where tokenCode contains DEFAULT_TOKEN_CODE
        defaultItemTokenShouldBeFound("tokenCode.contains=" + DEFAULT_TOKEN_CODE);

        // Get all the itemTokenList where tokenCode contains UPDATED_TOKEN_CODE
        defaultItemTokenShouldNotBeFound("tokenCode.contains=" + UPDATED_TOKEN_CODE);
    }

    @Test
    @Transactional
    void getAllItemTokensByTokenCodeNotContainsSomething() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);

        // Get all the itemTokenList where tokenCode does not contain DEFAULT_TOKEN_CODE
        defaultItemTokenShouldNotBeFound("tokenCode.doesNotContain=" + DEFAULT_TOKEN_CODE);

        // Get all the itemTokenList where tokenCode does not contain UPDATED_TOKEN_CODE
        defaultItemTokenShouldBeFound("tokenCode.doesNotContain=" + UPDATED_TOKEN_CODE);
    }

    @Test
    @Transactional
    void getAllItemTokensByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);

        // Get all the itemTokenList where createdDate equals to DEFAULT_CREATED_DATE
        defaultItemTokenShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the itemTokenList where createdDate equals to UPDATED_CREATED_DATE
        defaultItemTokenShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllItemTokensByCreatedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);

        // Get all the itemTokenList where createdDate not equals to DEFAULT_CREATED_DATE
        defaultItemTokenShouldNotBeFound("createdDate.notEquals=" + DEFAULT_CREATED_DATE);

        // Get all the itemTokenList where createdDate not equals to UPDATED_CREATED_DATE
        defaultItemTokenShouldBeFound("createdDate.notEquals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllItemTokensByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);

        // Get all the itemTokenList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultItemTokenShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the itemTokenList where createdDate equals to UPDATED_CREATED_DATE
        defaultItemTokenShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllItemTokensByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);

        // Get all the itemTokenList where createdDate is not null
        defaultItemTokenShouldBeFound("createdDate.specified=true");

        // Get all the itemTokenList where createdDate is null
        defaultItemTokenShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllItemTokensByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);

        // Get all the itemTokenList where createdDate is greater than or equal to DEFAULT_CREATED_DATE
        defaultItemTokenShouldBeFound("createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the itemTokenList where createdDate is greater than or equal to UPDATED_CREATED_DATE
        defaultItemTokenShouldNotBeFound("createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllItemTokensByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);

        // Get all the itemTokenList where createdDate is less than or equal to DEFAULT_CREATED_DATE
        defaultItemTokenShouldBeFound("createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the itemTokenList where createdDate is less than or equal to SMALLER_CREATED_DATE
        defaultItemTokenShouldNotBeFound("createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllItemTokensByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);

        // Get all the itemTokenList where createdDate is less than DEFAULT_CREATED_DATE
        defaultItemTokenShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the itemTokenList where createdDate is less than UPDATED_CREATED_DATE
        defaultItemTokenShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllItemTokensByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);

        // Get all the itemTokenList where createdDate is greater than DEFAULT_CREATED_DATE
        defaultItemTokenShouldNotBeFound("createdDate.greaterThan=" + DEFAULT_CREATED_DATE);

        // Get all the itemTokenList where createdDate is greater than SMALLER_CREATED_DATE
        defaultItemTokenShouldBeFound("createdDate.greaterThan=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllItemTokensByUpdatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);

        // Get all the itemTokenList where updatedDate equals to DEFAULT_UPDATED_DATE
        defaultItemTokenShouldBeFound("updatedDate.equals=" + DEFAULT_UPDATED_DATE);

        // Get all the itemTokenList where updatedDate equals to UPDATED_UPDATED_DATE
        defaultItemTokenShouldNotBeFound("updatedDate.equals=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllItemTokensByUpdatedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);

        // Get all the itemTokenList where updatedDate not equals to DEFAULT_UPDATED_DATE
        defaultItemTokenShouldNotBeFound("updatedDate.notEquals=" + DEFAULT_UPDATED_DATE);

        // Get all the itemTokenList where updatedDate not equals to UPDATED_UPDATED_DATE
        defaultItemTokenShouldBeFound("updatedDate.notEquals=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllItemTokensByUpdatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);

        // Get all the itemTokenList where updatedDate in DEFAULT_UPDATED_DATE or UPDATED_UPDATED_DATE
        defaultItemTokenShouldBeFound("updatedDate.in=" + DEFAULT_UPDATED_DATE + "," + UPDATED_UPDATED_DATE);

        // Get all the itemTokenList where updatedDate equals to UPDATED_UPDATED_DATE
        defaultItemTokenShouldNotBeFound("updatedDate.in=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllItemTokensByUpdatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);

        // Get all the itemTokenList where updatedDate is not null
        defaultItemTokenShouldBeFound("updatedDate.specified=true");

        // Get all the itemTokenList where updatedDate is null
        defaultItemTokenShouldNotBeFound("updatedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllItemTokensByUpdatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);

        // Get all the itemTokenList where updatedDate is greater than or equal to DEFAULT_UPDATED_DATE
        defaultItemTokenShouldBeFound("updatedDate.greaterThanOrEqual=" + DEFAULT_UPDATED_DATE);

        // Get all the itemTokenList where updatedDate is greater than or equal to UPDATED_UPDATED_DATE
        defaultItemTokenShouldNotBeFound("updatedDate.greaterThanOrEqual=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllItemTokensByUpdatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);

        // Get all the itemTokenList where updatedDate is less than or equal to DEFAULT_UPDATED_DATE
        defaultItemTokenShouldBeFound("updatedDate.lessThanOrEqual=" + DEFAULT_UPDATED_DATE);

        // Get all the itemTokenList where updatedDate is less than or equal to SMALLER_UPDATED_DATE
        defaultItemTokenShouldNotBeFound("updatedDate.lessThanOrEqual=" + SMALLER_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllItemTokensByUpdatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);

        // Get all the itemTokenList where updatedDate is less than DEFAULT_UPDATED_DATE
        defaultItemTokenShouldNotBeFound("updatedDate.lessThan=" + DEFAULT_UPDATED_DATE);

        // Get all the itemTokenList where updatedDate is less than UPDATED_UPDATED_DATE
        defaultItemTokenShouldBeFound("updatedDate.lessThan=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllItemTokensByUpdatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);

        // Get all the itemTokenList where updatedDate is greater than DEFAULT_UPDATED_DATE
        defaultItemTokenShouldNotBeFound("updatedDate.greaterThan=" + DEFAULT_UPDATED_DATE);

        // Get all the itemTokenList where updatedDate is greater than SMALLER_UPDATED_DATE
        defaultItemTokenShouldBeFound("updatedDate.greaterThan=" + SMALLER_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllItemTokensByOwnerIsEqualToSomething() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);
        User owner = UserResourceIT.createEntity(em);
        em.persist(owner);
        em.flush();
        itemToken.addOwner(owner);
        itemTokenRepository.saveAndFlush(itemToken);
        Long ownerId = owner.getId();

        // Get all the itemTokenList where owner equals to ownerId
        defaultItemTokenShouldBeFound("ownerId.equals=" + ownerId);

        // Get all the itemTokenList where owner equals to (ownerId + 1)
        defaultItemTokenShouldNotBeFound("ownerId.equals=" + (ownerId + 1));
    }

    @Test
    @Transactional
    void getAllItemTokensByItemIsEqualToSomething() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);
        Item item = ItemResourceIT.createEntity(em);
        em.persist(item);
        em.flush();
        itemToken.setItem(item);
        itemTokenRepository.saveAndFlush(itemToken);
        UUID itemId = item.getId();

        // Get all the itemTokenList where item equals to itemId
        defaultItemTokenShouldBeFound("itemId.equals=" + itemId);

        // Get all the itemTokenList where item equals to UUID.randomUUID()
        defaultItemTokenShouldNotBeFound("itemId.equals=" + UUID.randomUUID());
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultItemTokenShouldBeFound(String filter) throws Exception {
        restItemTokenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemToken.getId().intValue())))
            .andExpect(jsonPath("$.[*].tokenName").value(hasItem(DEFAULT_TOKEN_NAME)))
            .andExpect(jsonPath("$.[*].tokenCode").value(hasItem(DEFAULT_TOKEN_CODE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(sameInstant(DEFAULT_UPDATED_DATE))));

        // Check, that the count call also returns 1
        restItemTokenMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultItemTokenShouldNotBeFound(String filter) throws Exception {
        restItemTokenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restItemTokenMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingItemToken() throws Exception {
        // Get the itemToken
        restItemTokenMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewItemToken() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);

        int databaseSizeBeforeUpdate = itemTokenRepository.findAll().size();

        // Update the itemToken
        ItemToken updatedItemToken = itemTokenRepository.findById(itemToken.getId()).get();
        // Disconnect from session so that the updates on updatedItemToken are not directly saved in db
        em.detach(updatedItemToken);
        updatedItemToken
            .tokenName(UPDATED_TOKEN_NAME)
            .tokenCode(UPDATED_TOKEN_CODE)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedDate(UPDATED_UPDATED_DATE);

        restItemTokenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedItemToken.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedItemToken))
            )
            .andExpect(status().isOk());

        // Validate the ItemToken in the database
        List<ItemToken> itemTokenList = itemTokenRepository.findAll();
        assertThat(itemTokenList).hasSize(databaseSizeBeforeUpdate);
        ItemToken testItemToken = itemTokenList.get(itemTokenList.size() - 1);
        assertThat(testItemToken.getTokenName()).isEqualTo(UPDATED_TOKEN_NAME);
        assertThat(testItemToken.getTokenCode()).isEqualTo(UPDATED_TOKEN_CODE);
        assertThat(testItemToken.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testItemToken.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingItemToken() throws Exception {
        int databaseSizeBeforeUpdate = itemTokenRepository.findAll().size();
        itemToken.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemTokenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemToken.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemToken))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemToken in the database
        List<ItemToken> itemTokenList = itemTokenRepository.findAll();
        assertThat(itemTokenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchItemToken() throws Exception {
        int databaseSizeBeforeUpdate = itemTokenRepository.findAll().size();
        itemToken.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemTokenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemToken))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemToken in the database
        List<ItemToken> itemTokenList = itemTokenRepository.findAll();
        assertThat(itemTokenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamItemToken() throws Exception {
        int databaseSizeBeforeUpdate = itemTokenRepository.findAll().size();
        itemToken.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemTokenMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemToken)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemToken in the database
        List<ItemToken> itemTokenList = itemTokenRepository.findAll();
        assertThat(itemTokenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateItemTokenWithPatch() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);

        int databaseSizeBeforeUpdate = itemTokenRepository.findAll().size();

        // Update the itemToken using partial update
        ItemToken partialUpdatedItemToken = new ItemToken();
        partialUpdatedItemToken.setId(itemToken.getId());

        partialUpdatedItemToken.tokenCode(UPDATED_TOKEN_CODE);

        restItemTokenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemToken.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemToken))
            )
            .andExpect(status().isOk());

        // Validate the ItemToken in the database
        List<ItemToken> itemTokenList = itemTokenRepository.findAll();
        assertThat(itemTokenList).hasSize(databaseSizeBeforeUpdate);
        ItemToken testItemToken = itemTokenList.get(itemTokenList.size() - 1);
        assertThat(testItemToken.getTokenName()).isEqualTo(DEFAULT_TOKEN_NAME);
        assertThat(testItemToken.getTokenCode()).isEqualTo(UPDATED_TOKEN_CODE);
        assertThat(testItemToken.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testItemToken.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateItemTokenWithPatch() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);

        int databaseSizeBeforeUpdate = itemTokenRepository.findAll().size();

        // Update the itemToken using partial update
        ItemToken partialUpdatedItemToken = new ItemToken();
        partialUpdatedItemToken.setId(itemToken.getId());

        partialUpdatedItemToken
            .tokenName(UPDATED_TOKEN_NAME)
            .tokenCode(UPDATED_TOKEN_CODE)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedDate(UPDATED_UPDATED_DATE);

        restItemTokenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemToken.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemToken))
            )
            .andExpect(status().isOk());

        // Validate the ItemToken in the database
        List<ItemToken> itemTokenList = itemTokenRepository.findAll();
        assertThat(itemTokenList).hasSize(databaseSizeBeforeUpdate);
        ItemToken testItemToken = itemTokenList.get(itemTokenList.size() - 1);
        assertThat(testItemToken.getTokenName()).isEqualTo(UPDATED_TOKEN_NAME);
        assertThat(testItemToken.getTokenCode()).isEqualTo(UPDATED_TOKEN_CODE);
        assertThat(testItemToken.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testItemToken.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingItemToken() throws Exception {
        int databaseSizeBeforeUpdate = itemTokenRepository.findAll().size();
        itemToken.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemTokenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, itemToken.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemToken))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemToken in the database
        List<ItemToken> itemTokenList = itemTokenRepository.findAll();
        assertThat(itemTokenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchItemToken() throws Exception {
        int databaseSizeBeforeUpdate = itemTokenRepository.findAll().size();
        itemToken.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemTokenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemToken))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemToken in the database
        List<ItemToken> itemTokenList = itemTokenRepository.findAll();
        assertThat(itemTokenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamItemToken() throws Exception {
        int databaseSizeBeforeUpdate = itemTokenRepository.findAll().size();
        itemToken.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemTokenMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(itemToken))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemToken in the database
        List<ItemToken> itemTokenList = itemTokenRepository.findAll();
        assertThat(itemTokenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteItemToken() throws Exception {
        // Initialize the database
        itemTokenRepository.saveAndFlush(itemToken);

        int databaseSizeBeforeDelete = itemTokenRepository.findAll().size();

        // Delete the itemToken
        restItemTokenMockMvc
            .perform(delete(ENTITY_API_URL_ID, itemToken.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ItemToken> itemTokenList = itemTokenRepository.findAll();
        assertThat(itemTokenList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
