package com.triippztech.freshtrade.web.rest;

import static com.triippztech.freshtrade.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.triippztech.freshtrade.IntegrationTest;
import com.triippztech.freshtrade.domain.Item;
import com.triippztech.freshtrade.domain.Location;
import com.triippztech.freshtrade.domain.Reservation;
import com.triippztech.freshtrade.domain.TradeEvent;
import com.triippztech.freshtrade.repository.TradeEventRepository;
import com.triippztech.freshtrade.service.criteria.TradeEventCriteria;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TradeEventResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TradeEventResourceIT {

    private static final String DEFAULT_EVENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EVENT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/trade-events";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private TradeEventRepository tradeEventRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTradeEventMockMvc;

    private TradeEvent tradeEvent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TradeEvent createEntity(EntityManager em) {
        TradeEvent tradeEvent = new TradeEvent()
            .eventName(DEFAULT_EVENT_NAME)
            .eventDescription(DEFAULT_EVENT_DESCRIPTION)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .isActive(DEFAULT_IS_ACTIVE);
        return tradeEvent;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TradeEvent createUpdatedEntity(EntityManager em) {
        TradeEvent tradeEvent = new TradeEvent()
            .eventName(UPDATED_EVENT_NAME)
            .eventDescription(UPDATED_EVENT_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isActive(UPDATED_IS_ACTIVE);
        return tradeEvent;
    }

    @BeforeEach
    public void initTest() {
        tradeEvent = createEntity(em);
    }

    @Test
    @Transactional
    void createTradeEvent() throws Exception {
        int databaseSizeBeforeCreate = tradeEventRepository.findAll().size();
        // Create the TradeEvent
        restTradeEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tradeEvent)))
            .andExpect(status().isCreated());

        // Validate the TradeEvent in the database
        List<TradeEvent> tradeEventList = tradeEventRepository.findAll();
        assertThat(tradeEventList).hasSize(databaseSizeBeforeCreate + 1);
        TradeEvent testTradeEvent = tradeEventList.get(tradeEventList.size() - 1);
        assertThat(testTradeEvent.getEventName()).isEqualTo(DEFAULT_EVENT_NAME);
        assertThat(testTradeEvent.getEventDescription()).isEqualTo(DEFAULT_EVENT_DESCRIPTION);
        assertThat(testTradeEvent.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testTradeEvent.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testTradeEvent.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createTradeEventWithExistingId() throws Exception {
        // Create the TradeEvent with an existing ID
        tradeEventRepository.saveAndFlush(tradeEvent);

        int databaseSizeBeforeCreate = tradeEventRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTradeEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tradeEvent)))
            .andExpect(status().isBadRequest());

        // Validate the TradeEvent in the database
        List<TradeEvent> tradeEventList = tradeEventRepository.findAll();
        assertThat(tradeEventList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEventNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = tradeEventRepository.findAll().size();
        // set the field null
        tradeEvent.setEventName(null);

        // Create the TradeEvent, which fails.

        restTradeEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tradeEvent)))
            .andExpect(status().isBadRequest());

        List<TradeEvent> tradeEventList = tradeEventRepository.findAll();
        assertThat(tradeEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEventDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = tradeEventRepository.findAll().size();
        // set the field null
        tradeEvent.setEventDescription(null);

        // Create the TradeEvent, which fails.

        restTradeEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tradeEvent)))
            .andExpect(status().isBadRequest());

        List<TradeEvent> tradeEventList = tradeEventRepository.findAll();
        assertThat(tradeEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = tradeEventRepository.findAll().size();
        // set the field null
        tradeEvent.setStartDate(null);

        // Create the TradeEvent, which fails.

        restTradeEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tradeEvent)))
            .andExpect(status().isBadRequest());

        List<TradeEvent> tradeEventList = tradeEventRepository.findAll();
        assertThat(tradeEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = tradeEventRepository.findAll().size();
        // set the field null
        tradeEvent.setEndDate(null);

        // Create the TradeEvent, which fails.

        restTradeEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tradeEvent)))
            .andExpect(status().isBadRequest());

        List<TradeEvent> tradeEventList = tradeEventRepository.findAll();
        assertThat(tradeEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTradeEvents() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        // Get all the tradeEventList
        restTradeEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tradeEvent.getId().toString())))
            .andExpect(jsonPath("$.[*].eventName").value(hasItem(DEFAULT_EVENT_NAME)))
            .andExpect(jsonPath("$.[*].eventDescription").value(hasItem(DEFAULT_EVENT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getTradeEvent() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        // Get the tradeEvent
        restTradeEventMockMvc
            .perform(get(ENTITY_API_URL_ID, tradeEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tradeEvent.getId().toString()))
            .andExpect(jsonPath("$.eventName").value(DEFAULT_EVENT_NAME))
            .andExpect(jsonPath("$.eventDescription").value(DEFAULT_EVENT_DESCRIPTION))
            .andExpect(jsonPath("$.startDate").value(sameInstant(DEFAULT_START_DATE)))
            .andExpect(jsonPath("$.endDate").value(sameInstant(DEFAULT_END_DATE)))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getTradeEventsByIdFiltering() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        UUID id = tradeEvent.getId();

        defaultTradeEventShouldBeFound("id.equals=" + id);
        defaultTradeEventShouldNotBeFound("id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllTradeEventsByEventNameIsEqualToSomething() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        // Get all the tradeEventList where eventName equals to DEFAULT_EVENT_NAME
        defaultTradeEventShouldBeFound("eventName.equals=" + DEFAULT_EVENT_NAME);

        // Get all the tradeEventList where eventName equals to UPDATED_EVENT_NAME
        defaultTradeEventShouldNotBeFound("eventName.equals=" + UPDATED_EVENT_NAME);
    }

    @Test
    @Transactional
    void getAllTradeEventsByEventNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        // Get all the tradeEventList where eventName not equals to DEFAULT_EVENT_NAME
        defaultTradeEventShouldNotBeFound("eventName.notEquals=" + DEFAULT_EVENT_NAME);

        // Get all the tradeEventList where eventName not equals to UPDATED_EVENT_NAME
        defaultTradeEventShouldBeFound("eventName.notEquals=" + UPDATED_EVENT_NAME);
    }

    @Test
    @Transactional
    void getAllTradeEventsByEventNameIsInShouldWork() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        // Get all the tradeEventList where eventName in DEFAULT_EVENT_NAME or UPDATED_EVENT_NAME
        defaultTradeEventShouldBeFound("eventName.in=" + DEFAULT_EVENT_NAME + "," + UPDATED_EVENT_NAME);

        // Get all the tradeEventList where eventName equals to UPDATED_EVENT_NAME
        defaultTradeEventShouldNotBeFound("eventName.in=" + UPDATED_EVENT_NAME);
    }

    @Test
    @Transactional
    void getAllTradeEventsByEventNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        // Get all the tradeEventList where eventName is not null
        defaultTradeEventShouldBeFound("eventName.specified=true");

        // Get all the tradeEventList where eventName is null
        defaultTradeEventShouldNotBeFound("eventName.specified=false");
    }

    @Test
    @Transactional
    void getAllTradeEventsByEventNameContainsSomething() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        // Get all the tradeEventList where eventName contains DEFAULT_EVENT_NAME
        defaultTradeEventShouldBeFound("eventName.contains=" + DEFAULT_EVENT_NAME);

        // Get all the tradeEventList where eventName contains UPDATED_EVENT_NAME
        defaultTradeEventShouldNotBeFound("eventName.contains=" + UPDATED_EVENT_NAME);
    }

    @Test
    @Transactional
    void getAllTradeEventsByEventNameNotContainsSomething() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        // Get all the tradeEventList where eventName does not contain DEFAULT_EVENT_NAME
        defaultTradeEventShouldNotBeFound("eventName.doesNotContain=" + DEFAULT_EVENT_NAME);

        // Get all the tradeEventList where eventName does not contain UPDATED_EVENT_NAME
        defaultTradeEventShouldBeFound("eventName.doesNotContain=" + UPDATED_EVENT_NAME);
    }

    @Test
    @Transactional
    void getAllTradeEventsByEventDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        // Get all the tradeEventList where eventDescription equals to DEFAULT_EVENT_DESCRIPTION
        defaultTradeEventShouldBeFound("eventDescription.equals=" + DEFAULT_EVENT_DESCRIPTION);

        // Get all the tradeEventList where eventDescription equals to UPDATED_EVENT_DESCRIPTION
        defaultTradeEventShouldNotBeFound("eventDescription.equals=" + UPDATED_EVENT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTradeEventsByEventDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        // Get all the tradeEventList where eventDescription not equals to DEFAULT_EVENT_DESCRIPTION
        defaultTradeEventShouldNotBeFound("eventDescription.notEquals=" + DEFAULT_EVENT_DESCRIPTION);

        // Get all the tradeEventList where eventDescription not equals to UPDATED_EVENT_DESCRIPTION
        defaultTradeEventShouldBeFound("eventDescription.notEquals=" + UPDATED_EVENT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTradeEventsByEventDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        // Get all the tradeEventList where eventDescription in DEFAULT_EVENT_DESCRIPTION or UPDATED_EVENT_DESCRIPTION
        defaultTradeEventShouldBeFound("eventDescription.in=" + DEFAULT_EVENT_DESCRIPTION + "," + UPDATED_EVENT_DESCRIPTION);

        // Get all the tradeEventList where eventDescription equals to UPDATED_EVENT_DESCRIPTION
        defaultTradeEventShouldNotBeFound("eventDescription.in=" + UPDATED_EVENT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTradeEventsByEventDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        // Get all the tradeEventList where eventDescription is not null
        defaultTradeEventShouldBeFound("eventDescription.specified=true");

        // Get all the tradeEventList where eventDescription is null
        defaultTradeEventShouldNotBeFound("eventDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllTradeEventsByEventDescriptionContainsSomething() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        // Get all the tradeEventList where eventDescription contains DEFAULT_EVENT_DESCRIPTION
        defaultTradeEventShouldBeFound("eventDescription.contains=" + DEFAULT_EVENT_DESCRIPTION);

        // Get all the tradeEventList where eventDescription contains UPDATED_EVENT_DESCRIPTION
        defaultTradeEventShouldNotBeFound("eventDescription.contains=" + UPDATED_EVENT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTradeEventsByEventDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        // Get all the tradeEventList where eventDescription does not contain DEFAULT_EVENT_DESCRIPTION
        defaultTradeEventShouldNotBeFound("eventDescription.doesNotContain=" + DEFAULT_EVENT_DESCRIPTION);

        // Get all the tradeEventList where eventDescription does not contain UPDATED_EVENT_DESCRIPTION
        defaultTradeEventShouldBeFound("eventDescription.doesNotContain=" + UPDATED_EVENT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTradeEventsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        // Get all the tradeEventList where startDate equals to DEFAULT_START_DATE
        defaultTradeEventShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the tradeEventList where startDate equals to UPDATED_START_DATE
        defaultTradeEventShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllTradeEventsByStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        // Get all the tradeEventList where startDate not equals to DEFAULT_START_DATE
        defaultTradeEventShouldNotBeFound("startDate.notEquals=" + DEFAULT_START_DATE);

        // Get all the tradeEventList where startDate not equals to UPDATED_START_DATE
        defaultTradeEventShouldBeFound("startDate.notEquals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllTradeEventsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        // Get all the tradeEventList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultTradeEventShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the tradeEventList where startDate equals to UPDATED_START_DATE
        defaultTradeEventShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllTradeEventsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        // Get all the tradeEventList where startDate is not null
        defaultTradeEventShouldBeFound("startDate.specified=true");

        // Get all the tradeEventList where startDate is null
        defaultTradeEventShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTradeEventsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        // Get all the tradeEventList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultTradeEventShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the tradeEventList where startDate is greater than or equal to UPDATED_START_DATE
        defaultTradeEventShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllTradeEventsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        // Get all the tradeEventList where startDate is less than or equal to DEFAULT_START_DATE
        defaultTradeEventShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the tradeEventList where startDate is less than or equal to SMALLER_START_DATE
        defaultTradeEventShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllTradeEventsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        // Get all the tradeEventList where startDate is less than DEFAULT_START_DATE
        defaultTradeEventShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the tradeEventList where startDate is less than UPDATED_START_DATE
        defaultTradeEventShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllTradeEventsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        // Get all the tradeEventList where startDate is greater than DEFAULT_START_DATE
        defaultTradeEventShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the tradeEventList where startDate is greater than SMALLER_START_DATE
        defaultTradeEventShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllTradeEventsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        // Get all the tradeEventList where endDate equals to DEFAULT_END_DATE
        defaultTradeEventShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the tradeEventList where endDate equals to UPDATED_END_DATE
        defaultTradeEventShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllTradeEventsByEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        // Get all the tradeEventList where endDate not equals to DEFAULT_END_DATE
        defaultTradeEventShouldNotBeFound("endDate.notEquals=" + DEFAULT_END_DATE);

        // Get all the tradeEventList where endDate not equals to UPDATED_END_DATE
        defaultTradeEventShouldBeFound("endDate.notEquals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllTradeEventsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        // Get all the tradeEventList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultTradeEventShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the tradeEventList where endDate equals to UPDATED_END_DATE
        defaultTradeEventShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllTradeEventsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        // Get all the tradeEventList where endDate is not null
        defaultTradeEventShouldBeFound("endDate.specified=true");

        // Get all the tradeEventList where endDate is null
        defaultTradeEventShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTradeEventsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        // Get all the tradeEventList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultTradeEventShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the tradeEventList where endDate is greater than or equal to UPDATED_END_DATE
        defaultTradeEventShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllTradeEventsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        // Get all the tradeEventList where endDate is less than or equal to DEFAULT_END_DATE
        defaultTradeEventShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the tradeEventList where endDate is less than or equal to SMALLER_END_DATE
        defaultTradeEventShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllTradeEventsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        // Get all the tradeEventList where endDate is less than DEFAULT_END_DATE
        defaultTradeEventShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the tradeEventList where endDate is less than UPDATED_END_DATE
        defaultTradeEventShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllTradeEventsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        // Get all the tradeEventList where endDate is greater than DEFAULT_END_DATE
        defaultTradeEventShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the tradeEventList where endDate is greater than SMALLER_END_DATE
        defaultTradeEventShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllTradeEventsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        // Get all the tradeEventList where isActive equals to DEFAULT_IS_ACTIVE
        defaultTradeEventShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the tradeEventList where isActive equals to UPDATED_IS_ACTIVE
        defaultTradeEventShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllTradeEventsByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        // Get all the tradeEventList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultTradeEventShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the tradeEventList where isActive not equals to UPDATED_IS_ACTIVE
        defaultTradeEventShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllTradeEventsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        // Get all the tradeEventList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultTradeEventShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the tradeEventList where isActive equals to UPDATED_IS_ACTIVE
        defaultTradeEventShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllTradeEventsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        // Get all the tradeEventList where isActive is not null
        defaultTradeEventShouldBeFound("isActive.specified=true");

        // Get all the tradeEventList where isActive is null
        defaultTradeEventShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllTradeEventsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);
        Location location = LocationResourceIT.createEntity(em);
        em.persist(location);
        em.flush();
        tradeEvent.setLocation(location);
        tradeEventRepository.saveAndFlush(tradeEvent);
        Long locationId = location.getId();

        // Get all the tradeEventList where location equals to locationId
        defaultTradeEventShouldBeFound("locationId.equals=" + locationId);

        // Get all the tradeEventList where location equals to (locationId + 1)
        defaultTradeEventShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }

    @Test
    @Transactional
    void getAllTradeEventsByItemsIsEqualToSomething() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);
        Item items = ItemResourceIT.createEntity(em);
        em.persist(items);
        em.flush();
        tradeEvent.addItems(items);
        tradeEventRepository.saveAndFlush(tradeEvent);
        UUID itemsId = items.getId();

        // Get all the tradeEventList where items equals to itemsId
        defaultTradeEventShouldBeFound("itemsId.equals=" + itemsId);

        // Get all the tradeEventList where items equals to UUID.randomUUID()
        defaultTradeEventShouldNotBeFound("itemsId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllTradeEventsByReservationsIsEqualToSomething() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);
        Reservation reservations = ReservationResourceIT.createEntity(em);
        em.persist(reservations);
        em.flush();
        tradeEvent.addReservations(reservations);
        tradeEventRepository.saveAndFlush(tradeEvent);
        UUID reservationsId = reservations.getId();

        // Get all the tradeEventList where reservations equals to reservationsId
        defaultTradeEventShouldBeFound("reservationsId.equals=" + reservationsId);

        // Get all the tradeEventList where reservations equals to UUID.randomUUID()
        defaultTradeEventShouldNotBeFound("reservationsId.equals=" + UUID.randomUUID());
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTradeEventShouldBeFound(String filter) throws Exception {
        restTradeEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tradeEvent.getId().toString())))
            .andExpect(jsonPath("$.[*].eventName").value(hasItem(DEFAULT_EVENT_NAME)))
            .andExpect(jsonPath("$.[*].eventDescription").value(hasItem(DEFAULT_EVENT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restTradeEventMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTradeEventShouldNotBeFound(String filter) throws Exception {
        restTradeEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTradeEventMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTradeEvent() throws Exception {
        // Get the tradeEvent
        restTradeEventMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTradeEvent() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        int databaseSizeBeforeUpdate = tradeEventRepository.findAll().size();

        // Update the tradeEvent
        TradeEvent updatedTradeEvent = tradeEventRepository.findById(tradeEvent.getId()).get();
        // Disconnect from session so that the updates on updatedTradeEvent are not directly saved in db
        em.detach(updatedTradeEvent);
        updatedTradeEvent
            .eventName(UPDATED_EVENT_NAME)
            .eventDescription(UPDATED_EVENT_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isActive(UPDATED_IS_ACTIVE);

        restTradeEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTradeEvent.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTradeEvent))
            )
            .andExpect(status().isOk());

        // Validate the TradeEvent in the database
        List<TradeEvent> tradeEventList = tradeEventRepository.findAll();
        assertThat(tradeEventList).hasSize(databaseSizeBeforeUpdate);
        TradeEvent testTradeEvent = tradeEventList.get(tradeEventList.size() - 1);
        assertThat(testTradeEvent.getEventName()).isEqualTo(UPDATED_EVENT_NAME);
        assertThat(testTradeEvent.getEventDescription()).isEqualTo(UPDATED_EVENT_DESCRIPTION);
        assertThat(testTradeEvent.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testTradeEvent.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testTradeEvent.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingTradeEvent() throws Exception {
        int databaseSizeBeforeUpdate = tradeEventRepository.findAll().size();
        tradeEvent.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTradeEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tradeEvent.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tradeEvent))
            )
            .andExpect(status().isBadRequest());

        // Validate the TradeEvent in the database
        List<TradeEvent> tradeEventList = tradeEventRepository.findAll();
        assertThat(tradeEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTradeEvent() throws Exception {
        int databaseSizeBeforeUpdate = tradeEventRepository.findAll().size();
        tradeEvent.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTradeEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tradeEvent))
            )
            .andExpect(status().isBadRequest());

        // Validate the TradeEvent in the database
        List<TradeEvent> tradeEventList = tradeEventRepository.findAll();
        assertThat(tradeEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTradeEvent() throws Exception {
        int databaseSizeBeforeUpdate = tradeEventRepository.findAll().size();
        tradeEvent.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTradeEventMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tradeEvent)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TradeEvent in the database
        List<TradeEvent> tradeEventList = tradeEventRepository.findAll();
        assertThat(tradeEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTradeEventWithPatch() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        int databaseSizeBeforeUpdate = tradeEventRepository.findAll().size();

        // Update the tradeEvent using partial update
        TradeEvent partialUpdatedTradeEvent = new TradeEvent();
        partialUpdatedTradeEvent.setId(tradeEvent.getId());

        partialUpdatedTradeEvent.eventName(UPDATED_EVENT_NAME).endDate(UPDATED_END_DATE);

        restTradeEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTradeEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTradeEvent))
            )
            .andExpect(status().isOk());

        // Validate the TradeEvent in the database
        List<TradeEvent> tradeEventList = tradeEventRepository.findAll();
        assertThat(tradeEventList).hasSize(databaseSizeBeforeUpdate);
        TradeEvent testTradeEvent = tradeEventList.get(tradeEventList.size() - 1);
        assertThat(testTradeEvent.getEventName()).isEqualTo(UPDATED_EVENT_NAME);
        assertThat(testTradeEvent.getEventDescription()).isEqualTo(DEFAULT_EVENT_DESCRIPTION);
        assertThat(testTradeEvent.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testTradeEvent.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testTradeEvent.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateTradeEventWithPatch() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        int databaseSizeBeforeUpdate = tradeEventRepository.findAll().size();

        // Update the tradeEvent using partial update
        TradeEvent partialUpdatedTradeEvent = new TradeEvent();
        partialUpdatedTradeEvent.setId(tradeEvent.getId());

        partialUpdatedTradeEvent
            .eventName(UPDATED_EVENT_NAME)
            .eventDescription(UPDATED_EVENT_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isActive(UPDATED_IS_ACTIVE);

        restTradeEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTradeEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTradeEvent))
            )
            .andExpect(status().isOk());

        // Validate the TradeEvent in the database
        List<TradeEvent> tradeEventList = tradeEventRepository.findAll();
        assertThat(tradeEventList).hasSize(databaseSizeBeforeUpdate);
        TradeEvent testTradeEvent = tradeEventList.get(tradeEventList.size() - 1);
        assertThat(testTradeEvent.getEventName()).isEqualTo(UPDATED_EVENT_NAME);
        assertThat(testTradeEvent.getEventDescription()).isEqualTo(UPDATED_EVENT_DESCRIPTION);
        assertThat(testTradeEvent.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testTradeEvent.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testTradeEvent.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingTradeEvent() throws Exception {
        int databaseSizeBeforeUpdate = tradeEventRepository.findAll().size();
        tradeEvent.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTradeEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tradeEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tradeEvent))
            )
            .andExpect(status().isBadRequest());

        // Validate the TradeEvent in the database
        List<TradeEvent> tradeEventList = tradeEventRepository.findAll();
        assertThat(tradeEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTradeEvent() throws Exception {
        int databaseSizeBeforeUpdate = tradeEventRepository.findAll().size();
        tradeEvent.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTradeEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tradeEvent))
            )
            .andExpect(status().isBadRequest());

        // Validate the TradeEvent in the database
        List<TradeEvent> tradeEventList = tradeEventRepository.findAll();
        assertThat(tradeEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTradeEvent() throws Exception {
        int databaseSizeBeforeUpdate = tradeEventRepository.findAll().size();
        tradeEvent.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTradeEventMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tradeEvent))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TradeEvent in the database
        List<TradeEvent> tradeEventList = tradeEventRepository.findAll();
        assertThat(tradeEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTradeEvent() throws Exception {
        // Initialize the database
        tradeEventRepository.saveAndFlush(tradeEvent);

        int databaseSizeBeforeDelete = tradeEventRepository.findAll().size();

        // Delete the tradeEvent
        restTradeEventMockMvc
            .perform(delete(ENTITY_API_URL_ID, tradeEvent.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TradeEvent> tradeEventList = tradeEventRepository.findAll();
        assertThat(tradeEventList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
