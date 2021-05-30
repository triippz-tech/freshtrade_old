package com.triippztech.freshtrade.web.rest;

import static com.triippztech.freshtrade.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.triippztech.freshtrade.IntegrationTest;
import com.triippztech.freshtrade.domain.Reservation;
import com.triippztech.freshtrade.domain.TradeEvent;
import com.triippztech.freshtrade.domain.User;
import com.triippztech.freshtrade.repository.ReservationRepository;
import com.triippztech.freshtrade.service.criteria.ReservationCriteria;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ReservationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReservationResourceIT {

    private static final String DEFAULT_RESERVATION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_RESERVATION_NUMBER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Boolean DEFAULT_IS_CANCELLED = false;
    private static final Boolean UPDATED_IS_CANCELLED = true;

    private static final String DEFAULT_CANCELLATION_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_CANCELLATION_NOTE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_PICKUP_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PICKUP_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_PICKUP_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/reservations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReservationMockMvc;

    private Reservation reservation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reservation createEntity(EntityManager em) {
        Reservation reservation = new Reservation()
            .reservationNumber(DEFAULT_RESERVATION_NUMBER)
            .isActive(DEFAULT_IS_ACTIVE)
            .isCancelled(DEFAULT_IS_CANCELLED)
            .cancellationNote(DEFAULT_CANCELLATION_NOTE)
            .pickupTime(DEFAULT_PICKUP_TIME)
            .createdDate(DEFAULT_CREATED_DATE)
            .updatedDate(DEFAULT_UPDATED_DATE);
        return reservation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reservation createUpdatedEntity(EntityManager em) {
        Reservation reservation = new Reservation()
            .reservationNumber(UPDATED_RESERVATION_NUMBER)
            .isActive(UPDATED_IS_ACTIVE)
            .isCancelled(UPDATED_IS_CANCELLED)
            .cancellationNote(UPDATED_CANCELLATION_NOTE)
            .pickupTime(UPDATED_PICKUP_TIME)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedDate(UPDATED_UPDATED_DATE);
        return reservation;
    }

    @BeforeEach
    public void initTest() {
        reservation = createEntity(em);
    }

    @Test
    @Transactional
    void createReservation() throws Exception {
        int databaseSizeBeforeCreate = reservationRepository.findAll().size();
        // Create the Reservation
        restReservationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reservation)))
            .andExpect(status().isCreated());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeCreate + 1);
        Reservation testReservation = reservationList.get(reservationList.size() - 1);
        assertThat(testReservation.getReservationNumber()).isEqualTo(DEFAULT_RESERVATION_NUMBER);
        assertThat(testReservation.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testReservation.getIsCancelled()).isEqualTo(DEFAULT_IS_CANCELLED);
        assertThat(testReservation.getCancellationNote()).isEqualTo(DEFAULT_CANCELLATION_NOTE);
        assertThat(testReservation.getPickupTime()).isEqualTo(DEFAULT_PICKUP_TIME);
        assertThat(testReservation.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testReservation.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
    }

    @Test
    @Transactional
    void createReservationWithExistingId() throws Exception {
        // Create the Reservation with an existing ID
        reservationRepository.saveAndFlush(reservation);

        int databaseSizeBeforeCreate = reservationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReservationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reservation)))
            .andExpect(status().isBadRequest());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkReservationNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = reservationRepository.findAll().size();
        // set the field null
        reservation.setReservationNumber(null);

        // Create the Reservation, which fails.

        restReservationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reservation)))
            .andExpect(status().isBadRequest());

        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = reservationRepository.findAll().size();
        // set the field null
        reservation.setIsActive(null);

        // Create the Reservation, which fails.

        restReservationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reservation)))
            .andExpect(status().isBadRequest());

        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReservations() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList
        restReservationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reservation.getId().toString())))
            .andExpect(jsonPath("$.[*].reservationNumber").value(hasItem(DEFAULT_RESERVATION_NUMBER)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isCancelled").value(hasItem(DEFAULT_IS_CANCELLED.booleanValue())))
            .andExpect(jsonPath("$.[*].cancellationNote").value(hasItem(DEFAULT_CANCELLATION_NOTE.toString())))
            .andExpect(jsonPath("$.[*].pickupTime").value(hasItem(sameInstant(DEFAULT_PICKUP_TIME))))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(sameInstant(DEFAULT_UPDATED_DATE))));
    }

    @Test
    @Transactional
    void getReservation() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get the reservation
        restReservationMockMvc
            .perform(get(ENTITY_API_URL_ID, reservation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reservation.getId().toString()))
            .andExpect(jsonPath("$.reservationNumber").value(DEFAULT_RESERVATION_NUMBER))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.isCancelled").value(DEFAULT_IS_CANCELLED.booleanValue()))
            .andExpect(jsonPath("$.cancellationNote").value(DEFAULT_CANCELLATION_NOTE.toString()))
            .andExpect(jsonPath("$.pickupTime").value(sameInstant(DEFAULT_PICKUP_TIME)))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.updatedDate").value(sameInstant(DEFAULT_UPDATED_DATE)));
    }

    @Test
    @Transactional
    void getReservationsByIdFiltering() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        UUID id = reservation.getId();

        defaultReservationShouldBeFound("id.equals=" + id);
        defaultReservationShouldNotBeFound("id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllReservationsByReservationNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where reservationNumber equals to DEFAULT_RESERVATION_NUMBER
        defaultReservationShouldBeFound("reservationNumber.equals=" + DEFAULT_RESERVATION_NUMBER);

        // Get all the reservationList where reservationNumber equals to UPDATED_RESERVATION_NUMBER
        defaultReservationShouldNotBeFound("reservationNumber.equals=" + UPDATED_RESERVATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllReservationsByReservationNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where reservationNumber not equals to DEFAULT_RESERVATION_NUMBER
        defaultReservationShouldNotBeFound("reservationNumber.notEquals=" + DEFAULT_RESERVATION_NUMBER);

        // Get all the reservationList where reservationNumber not equals to UPDATED_RESERVATION_NUMBER
        defaultReservationShouldBeFound("reservationNumber.notEquals=" + UPDATED_RESERVATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllReservationsByReservationNumberIsInShouldWork() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where reservationNumber in DEFAULT_RESERVATION_NUMBER or UPDATED_RESERVATION_NUMBER
        defaultReservationShouldBeFound("reservationNumber.in=" + DEFAULT_RESERVATION_NUMBER + "," + UPDATED_RESERVATION_NUMBER);

        // Get all the reservationList where reservationNumber equals to UPDATED_RESERVATION_NUMBER
        defaultReservationShouldNotBeFound("reservationNumber.in=" + UPDATED_RESERVATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllReservationsByReservationNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where reservationNumber is not null
        defaultReservationShouldBeFound("reservationNumber.specified=true");

        // Get all the reservationList where reservationNumber is null
        defaultReservationShouldNotBeFound("reservationNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllReservationsByReservationNumberContainsSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where reservationNumber contains DEFAULT_RESERVATION_NUMBER
        defaultReservationShouldBeFound("reservationNumber.contains=" + DEFAULT_RESERVATION_NUMBER);

        // Get all the reservationList where reservationNumber contains UPDATED_RESERVATION_NUMBER
        defaultReservationShouldNotBeFound("reservationNumber.contains=" + UPDATED_RESERVATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllReservationsByReservationNumberNotContainsSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where reservationNumber does not contain DEFAULT_RESERVATION_NUMBER
        defaultReservationShouldNotBeFound("reservationNumber.doesNotContain=" + DEFAULT_RESERVATION_NUMBER);

        // Get all the reservationList where reservationNumber does not contain UPDATED_RESERVATION_NUMBER
        defaultReservationShouldBeFound("reservationNumber.doesNotContain=" + UPDATED_RESERVATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllReservationsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where isActive equals to DEFAULT_IS_ACTIVE
        defaultReservationShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the reservationList where isActive equals to UPDATED_IS_ACTIVE
        defaultReservationShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllReservationsByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultReservationShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the reservationList where isActive not equals to UPDATED_IS_ACTIVE
        defaultReservationShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllReservationsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultReservationShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the reservationList where isActive equals to UPDATED_IS_ACTIVE
        defaultReservationShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllReservationsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where isActive is not null
        defaultReservationShouldBeFound("isActive.specified=true");

        // Get all the reservationList where isActive is null
        defaultReservationShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllReservationsByIsCancelledIsEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where isCancelled equals to DEFAULT_IS_CANCELLED
        defaultReservationShouldBeFound("isCancelled.equals=" + DEFAULT_IS_CANCELLED);

        // Get all the reservationList where isCancelled equals to UPDATED_IS_CANCELLED
        defaultReservationShouldNotBeFound("isCancelled.equals=" + UPDATED_IS_CANCELLED);
    }

    @Test
    @Transactional
    void getAllReservationsByIsCancelledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where isCancelled not equals to DEFAULT_IS_CANCELLED
        defaultReservationShouldNotBeFound("isCancelled.notEquals=" + DEFAULT_IS_CANCELLED);

        // Get all the reservationList where isCancelled not equals to UPDATED_IS_CANCELLED
        defaultReservationShouldBeFound("isCancelled.notEquals=" + UPDATED_IS_CANCELLED);
    }

    @Test
    @Transactional
    void getAllReservationsByIsCancelledIsInShouldWork() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where isCancelled in DEFAULT_IS_CANCELLED or UPDATED_IS_CANCELLED
        defaultReservationShouldBeFound("isCancelled.in=" + DEFAULT_IS_CANCELLED + "," + UPDATED_IS_CANCELLED);

        // Get all the reservationList where isCancelled equals to UPDATED_IS_CANCELLED
        defaultReservationShouldNotBeFound("isCancelled.in=" + UPDATED_IS_CANCELLED);
    }

    @Test
    @Transactional
    void getAllReservationsByIsCancelledIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where isCancelled is not null
        defaultReservationShouldBeFound("isCancelled.specified=true");

        // Get all the reservationList where isCancelled is null
        defaultReservationShouldNotBeFound("isCancelled.specified=false");
    }

    @Test
    @Transactional
    void getAllReservationsByPickupTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where pickupTime equals to DEFAULT_PICKUP_TIME
        defaultReservationShouldBeFound("pickupTime.equals=" + DEFAULT_PICKUP_TIME);

        // Get all the reservationList where pickupTime equals to UPDATED_PICKUP_TIME
        defaultReservationShouldNotBeFound("pickupTime.equals=" + UPDATED_PICKUP_TIME);
    }

    @Test
    @Transactional
    void getAllReservationsByPickupTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where pickupTime not equals to DEFAULT_PICKUP_TIME
        defaultReservationShouldNotBeFound("pickupTime.notEquals=" + DEFAULT_PICKUP_TIME);

        // Get all the reservationList where pickupTime not equals to UPDATED_PICKUP_TIME
        defaultReservationShouldBeFound("pickupTime.notEquals=" + UPDATED_PICKUP_TIME);
    }

    @Test
    @Transactional
    void getAllReservationsByPickupTimeIsInShouldWork() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where pickupTime in DEFAULT_PICKUP_TIME or UPDATED_PICKUP_TIME
        defaultReservationShouldBeFound("pickupTime.in=" + DEFAULT_PICKUP_TIME + "," + UPDATED_PICKUP_TIME);

        // Get all the reservationList where pickupTime equals to UPDATED_PICKUP_TIME
        defaultReservationShouldNotBeFound("pickupTime.in=" + UPDATED_PICKUP_TIME);
    }

    @Test
    @Transactional
    void getAllReservationsByPickupTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where pickupTime is not null
        defaultReservationShouldBeFound("pickupTime.specified=true");

        // Get all the reservationList where pickupTime is null
        defaultReservationShouldNotBeFound("pickupTime.specified=false");
    }

    @Test
    @Transactional
    void getAllReservationsByPickupTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where pickupTime is greater than or equal to DEFAULT_PICKUP_TIME
        defaultReservationShouldBeFound("pickupTime.greaterThanOrEqual=" + DEFAULT_PICKUP_TIME);

        // Get all the reservationList where pickupTime is greater than or equal to UPDATED_PICKUP_TIME
        defaultReservationShouldNotBeFound("pickupTime.greaterThanOrEqual=" + UPDATED_PICKUP_TIME);
    }

    @Test
    @Transactional
    void getAllReservationsByPickupTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where pickupTime is less than or equal to DEFAULT_PICKUP_TIME
        defaultReservationShouldBeFound("pickupTime.lessThanOrEqual=" + DEFAULT_PICKUP_TIME);

        // Get all the reservationList where pickupTime is less than or equal to SMALLER_PICKUP_TIME
        defaultReservationShouldNotBeFound("pickupTime.lessThanOrEqual=" + SMALLER_PICKUP_TIME);
    }

    @Test
    @Transactional
    void getAllReservationsByPickupTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where pickupTime is less than DEFAULT_PICKUP_TIME
        defaultReservationShouldNotBeFound("pickupTime.lessThan=" + DEFAULT_PICKUP_TIME);

        // Get all the reservationList where pickupTime is less than UPDATED_PICKUP_TIME
        defaultReservationShouldBeFound("pickupTime.lessThan=" + UPDATED_PICKUP_TIME);
    }

    @Test
    @Transactional
    void getAllReservationsByPickupTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where pickupTime is greater than DEFAULT_PICKUP_TIME
        defaultReservationShouldNotBeFound("pickupTime.greaterThan=" + DEFAULT_PICKUP_TIME);

        // Get all the reservationList where pickupTime is greater than SMALLER_PICKUP_TIME
        defaultReservationShouldBeFound("pickupTime.greaterThan=" + SMALLER_PICKUP_TIME);
    }

    @Test
    @Transactional
    void getAllReservationsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where createdDate equals to DEFAULT_CREATED_DATE
        defaultReservationShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the reservationList where createdDate equals to UPDATED_CREATED_DATE
        defaultReservationShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllReservationsByCreatedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where createdDate not equals to DEFAULT_CREATED_DATE
        defaultReservationShouldNotBeFound("createdDate.notEquals=" + DEFAULT_CREATED_DATE);

        // Get all the reservationList where createdDate not equals to UPDATED_CREATED_DATE
        defaultReservationShouldBeFound("createdDate.notEquals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllReservationsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultReservationShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the reservationList where createdDate equals to UPDATED_CREATED_DATE
        defaultReservationShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllReservationsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where createdDate is not null
        defaultReservationShouldBeFound("createdDate.specified=true");

        // Get all the reservationList where createdDate is null
        defaultReservationShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllReservationsByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where createdDate is greater than or equal to DEFAULT_CREATED_DATE
        defaultReservationShouldBeFound("createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the reservationList where createdDate is greater than or equal to UPDATED_CREATED_DATE
        defaultReservationShouldNotBeFound("createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllReservationsByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where createdDate is less than or equal to DEFAULT_CREATED_DATE
        defaultReservationShouldBeFound("createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the reservationList where createdDate is less than or equal to SMALLER_CREATED_DATE
        defaultReservationShouldNotBeFound("createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllReservationsByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where createdDate is less than DEFAULT_CREATED_DATE
        defaultReservationShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the reservationList where createdDate is less than UPDATED_CREATED_DATE
        defaultReservationShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllReservationsByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where createdDate is greater than DEFAULT_CREATED_DATE
        defaultReservationShouldNotBeFound("createdDate.greaterThan=" + DEFAULT_CREATED_DATE);

        // Get all the reservationList where createdDate is greater than SMALLER_CREATED_DATE
        defaultReservationShouldBeFound("createdDate.greaterThan=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllReservationsByUpdatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where updatedDate equals to DEFAULT_UPDATED_DATE
        defaultReservationShouldBeFound("updatedDate.equals=" + DEFAULT_UPDATED_DATE);

        // Get all the reservationList where updatedDate equals to UPDATED_UPDATED_DATE
        defaultReservationShouldNotBeFound("updatedDate.equals=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllReservationsByUpdatedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where updatedDate not equals to DEFAULT_UPDATED_DATE
        defaultReservationShouldNotBeFound("updatedDate.notEquals=" + DEFAULT_UPDATED_DATE);

        // Get all the reservationList where updatedDate not equals to UPDATED_UPDATED_DATE
        defaultReservationShouldBeFound("updatedDate.notEquals=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllReservationsByUpdatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where updatedDate in DEFAULT_UPDATED_DATE or UPDATED_UPDATED_DATE
        defaultReservationShouldBeFound("updatedDate.in=" + DEFAULT_UPDATED_DATE + "," + UPDATED_UPDATED_DATE);

        // Get all the reservationList where updatedDate equals to UPDATED_UPDATED_DATE
        defaultReservationShouldNotBeFound("updatedDate.in=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllReservationsByUpdatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where updatedDate is not null
        defaultReservationShouldBeFound("updatedDate.specified=true");

        // Get all the reservationList where updatedDate is null
        defaultReservationShouldNotBeFound("updatedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllReservationsByUpdatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where updatedDate is greater than or equal to DEFAULT_UPDATED_DATE
        defaultReservationShouldBeFound("updatedDate.greaterThanOrEqual=" + DEFAULT_UPDATED_DATE);

        // Get all the reservationList where updatedDate is greater than or equal to UPDATED_UPDATED_DATE
        defaultReservationShouldNotBeFound("updatedDate.greaterThanOrEqual=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllReservationsByUpdatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where updatedDate is less than or equal to DEFAULT_UPDATED_DATE
        defaultReservationShouldBeFound("updatedDate.lessThanOrEqual=" + DEFAULT_UPDATED_DATE);

        // Get all the reservationList where updatedDate is less than or equal to SMALLER_UPDATED_DATE
        defaultReservationShouldNotBeFound("updatedDate.lessThanOrEqual=" + SMALLER_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllReservationsByUpdatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where updatedDate is less than DEFAULT_UPDATED_DATE
        defaultReservationShouldNotBeFound("updatedDate.lessThan=" + DEFAULT_UPDATED_DATE);

        // Get all the reservationList where updatedDate is less than UPDATED_UPDATED_DATE
        defaultReservationShouldBeFound("updatedDate.lessThan=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllReservationsByUpdatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where updatedDate is greater than DEFAULT_UPDATED_DATE
        defaultReservationShouldNotBeFound("updatedDate.greaterThan=" + DEFAULT_UPDATED_DATE);

        // Get all the reservationList where updatedDate is greater than SMALLER_UPDATED_DATE
        defaultReservationShouldBeFound("updatedDate.greaterThan=" + SMALLER_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllReservationsBySellerIsEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);
        User seller = UserResourceIT.createEntity(em);
        em.persist(seller);
        em.flush();
        reservation.setSeller(seller);
        reservationRepository.saveAndFlush(reservation);
        Long sellerId = seller.getId();

        // Get all the reservationList where seller equals to sellerId
        defaultReservationShouldBeFound("sellerId.equals=" + sellerId);

        // Get all the reservationList where seller equals to (sellerId + 1)
        defaultReservationShouldNotBeFound("sellerId.equals=" + (sellerId + 1));
    }

    @Test
    @Transactional
    void getAllReservationsByBuyerIsEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);
        User buyer = UserResourceIT.createEntity(em);
        em.persist(buyer);
        em.flush();
        reservation.setBuyer(buyer);
        reservationRepository.saveAndFlush(reservation);
        Long buyerId = buyer.getId();

        // Get all the reservationList where buyer equals to buyerId
        defaultReservationShouldBeFound("buyerId.equals=" + buyerId);

        // Get all the reservationList where buyer equals to (buyerId + 1)
        defaultReservationShouldNotBeFound("buyerId.equals=" + (buyerId + 1));
    }

    @Test
    @Transactional
    void getAllReservationsByEventIsEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);
        TradeEvent event = TradeEventResourceIT.createEntity(em);
        em.persist(event);
        em.flush();
        reservation.setEvent(event);
        reservationRepository.saveAndFlush(reservation);
        UUID eventId = event.getId();

        // Get all the reservationList where event equals to eventId
        defaultReservationShouldBeFound("eventId.equals=" + eventId);

        // Get all the reservationList where event equals to UUID.randomUUID()
        defaultReservationShouldNotBeFound("eventId.equals=" + UUID.randomUUID());
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReservationShouldBeFound(String filter) throws Exception {
        restReservationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reservation.getId().toString())))
            .andExpect(jsonPath("$.[*].reservationNumber").value(hasItem(DEFAULT_RESERVATION_NUMBER)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isCancelled").value(hasItem(DEFAULT_IS_CANCELLED.booleanValue())))
            .andExpect(jsonPath("$.[*].cancellationNote").value(hasItem(DEFAULT_CANCELLATION_NOTE.toString())))
            .andExpect(jsonPath("$.[*].pickupTime").value(hasItem(sameInstant(DEFAULT_PICKUP_TIME))))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(sameInstant(DEFAULT_UPDATED_DATE))));

        // Check, that the count call also returns 1
        restReservationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReservationShouldNotBeFound(String filter) throws Exception {
        restReservationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReservationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReservation() throws Exception {
        // Get the reservation
        restReservationMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReservation() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();

        // Update the reservation
        Reservation updatedReservation = reservationRepository.findById(reservation.getId()).get();
        // Disconnect from session so that the updates on updatedReservation are not directly saved in db
        em.detach(updatedReservation);
        updatedReservation
            .reservationNumber(UPDATED_RESERVATION_NUMBER)
            .isActive(UPDATED_IS_ACTIVE)
            .isCancelled(UPDATED_IS_CANCELLED)
            .cancellationNote(UPDATED_CANCELLATION_NOTE)
            .pickupTime(UPDATED_PICKUP_TIME)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedDate(UPDATED_UPDATED_DATE);

        restReservationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReservation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedReservation))
            )
            .andExpect(status().isOk());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
        Reservation testReservation = reservationList.get(reservationList.size() - 1);
        assertThat(testReservation.getReservationNumber()).isEqualTo(UPDATED_RESERVATION_NUMBER);
        assertThat(testReservation.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testReservation.getIsCancelled()).isEqualTo(UPDATED_IS_CANCELLED);
        assertThat(testReservation.getCancellationNote()).isEqualTo(UPDATED_CANCELLATION_NOTE);
        assertThat(testReservation.getPickupTime()).isEqualTo(UPDATED_PICKUP_TIME);
        assertThat(testReservation.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testReservation.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingReservation() throws Exception {
        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();
        reservation.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReservationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reservation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reservation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReservation() throws Exception {
        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();
        reservation.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reservation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReservation() throws Exception {
        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();
        reservation.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reservation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReservationWithPatch() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();

        // Update the reservation using partial update
        Reservation partialUpdatedReservation = new Reservation();
        partialUpdatedReservation.setId(reservation.getId());

        partialUpdatedReservation
            .reservationNumber(UPDATED_RESERVATION_NUMBER)
            .isCancelled(UPDATED_IS_CANCELLED)
            .pickupTime(UPDATED_PICKUP_TIME)
            .updatedDate(UPDATED_UPDATED_DATE);

        restReservationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReservation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReservation))
            )
            .andExpect(status().isOk());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
        Reservation testReservation = reservationList.get(reservationList.size() - 1);
        assertThat(testReservation.getReservationNumber()).isEqualTo(UPDATED_RESERVATION_NUMBER);
        assertThat(testReservation.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testReservation.getIsCancelled()).isEqualTo(UPDATED_IS_CANCELLED);
        assertThat(testReservation.getCancellationNote()).isEqualTo(DEFAULT_CANCELLATION_NOTE);
        assertThat(testReservation.getPickupTime()).isEqualTo(UPDATED_PICKUP_TIME);
        assertThat(testReservation.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testReservation.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateReservationWithPatch() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();

        // Update the reservation using partial update
        Reservation partialUpdatedReservation = new Reservation();
        partialUpdatedReservation.setId(reservation.getId());

        partialUpdatedReservation
            .reservationNumber(UPDATED_RESERVATION_NUMBER)
            .isActive(UPDATED_IS_ACTIVE)
            .isCancelled(UPDATED_IS_CANCELLED)
            .cancellationNote(UPDATED_CANCELLATION_NOTE)
            .pickupTime(UPDATED_PICKUP_TIME)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedDate(UPDATED_UPDATED_DATE);

        restReservationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReservation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReservation))
            )
            .andExpect(status().isOk());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
        Reservation testReservation = reservationList.get(reservationList.size() - 1);
        assertThat(testReservation.getReservationNumber()).isEqualTo(UPDATED_RESERVATION_NUMBER);
        assertThat(testReservation.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testReservation.getIsCancelled()).isEqualTo(UPDATED_IS_CANCELLED);
        assertThat(testReservation.getCancellationNote()).isEqualTo(UPDATED_CANCELLATION_NOTE);
        assertThat(testReservation.getPickupTime()).isEqualTo(UPDATED_PICKUP_TIME);
        assertThat(testReservation.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testReservation.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingReservation() throws Exception {
        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();
        reservation.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReservationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reservation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reservation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReservation() throws Exception {
        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();
        reservation.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reservation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReservation() throws Exception {
        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();
        reservation.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(reservation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReservation() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        int databaseSizeBeforeDelete = reservationRepository.findAll().size();

        // Delete the reservation
        restReservationMockMvc
            .perform(delete(ENTITY_API_URL_ID, reservation.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
