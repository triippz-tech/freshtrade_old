package com.triippztech.freshtrade.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.triippztech.freshtrade.IntegrationTest;
import com.triippztech.freshtrade.domain.Country;
import com.triippztech.freshtrade.domain.Item;
import com.triippztech.freshtrade.domain.Location;
import com.triippztech.freshtrade.domain.TradeEvent;
import com.triippztech.freshtrade.repository.LocationRepository;
import com.triippztech.freshtrade.service.criteria.LocationCriteria;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
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
 * Integration tests for the {@link LocationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LocationResourceIT {

    private static final String DEFAULT_SHORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    private static final Double DEFAULT_LAT_LOCATION = 1D;
    private static final Double UPDATED_LAT_LOCATION = 2D;
    private static final Double SMALLER_LAT_LOCATION = 1D - 1D;

    private static final Double DEFAULT_LONG_LOCATION = 1D;
    private static final Double UPDATED_LONG_LOCATION = 2D;
    private static final Double SMALLER_LONG_LOCATION = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/locations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocationMockMvc;

    private Location location;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Location createEntity(EntityManager em) {
        Location location = new Location()
            .shortName(DEFAULT_SHORT_NAME)
            .address(DEFAULT_ADDRESS)
            .postalCode(DEFAULT_POSTAL_CODE)
            .latLocation(DEFAULT_LAT_LOCATION)
            .longLocation(DEFAULT_LONG_LOCATION);
        return location;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Location createUpdatedEntity(EntityManager em) {
        Location location = new Location()
            .shortName(UPDATED_SHORT_NAME)
            .address(UPDATED_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .latLocation(UPDATED_LAT_LOCATION)
            .longLocation(UPDATED_LONG_LOCATION);
        return location;
    }

    @BeforeEach
    public void initTest() {
        location = createEntity(em);
    }

    @Test
    @Transactional
    void createLocation() throws Exception {
        int databaseSizeBeforeCreate = locationRepository.findAll().size();
        // Create the Location
        restLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(location)))
            .andExpect(status().isCreated());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeCreate + 1);
        Location testLocation = locationList.get(locationList.size() - 1);
        assertThat(testLocation.getShortName()).isEqualTo(DEFAULT_SHORT_NAME);
        assertThat(testLocation.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testLocation.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testLocation.getLatLocation()).isEqualTo(DEFAULT_LAT_LOCATION);
        assertThat(testLocation.getLongLocation()).isEqualTo(DEFAULT_LONG_LOCATION);
    }

    @Test
    @Transactional
    void createLocationWithExistingId() throws Exception {
        // Create the Location with an existing ID
        location.setId(1L);

        int databaseSizeBeforeCreate = locationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(location)))
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkShortNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationRepository.findAll().size();
        // set the field null
        location.setShortName(null);

        // Create the Location, which fails.

        restLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(location)))
            .andExpect(status().isBadRequest());

        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLocations() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList
        restLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(location.getId().intValue())))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].latLocation").value(hasItem(DEFAULT_LAT_LOCATION.doubleValue())))
            .andExpect(jsonPath("$.[*].longLocation").value(hasItem(DEFAULT_LONG_LOCATION.doubleValue())));
    }

    @Test
    @Transactional
    void getLocation() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get the location
        restLocationMockMvc
            .perform(get(ENTITY_API_URL_ID, location.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(location.getId().intValue()))
            .andExpect(jsonPath("$.shortName").value(DEFAULT_SHORT_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE))
            .andExpect(jsonPath("$.latLocation").value(DEFAULT_LAT_LOCATION.doubleValue()))
            .andExpect(jsonPath("$.longLocation").value(DEFAULT_LONG_LOCATION.doubleValue()));
    }

    @Test
    @Transactional
    void getLocationsByIdFiltering() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        Long id = location.getId();

        defaultLocationShouldBeFound("id.equals=" + id);
        defaultLocationShouldNotBeFound("id.notEquals=" + id);

        defaultLocationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLocationShouldNotBeFound("id.greaterThan=" + id);

        defaultLocationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLocationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLocationsByShortNameIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where shortName equals to DEFAULT_SHORT_NAME
        defaultLocationShouldBeFound("shortName.equals=" + DEFAULT_SHORT_NAME);

        // Get all the locationList where shortName equals to UPDATED_SHORT_NAME
        defaultLocationShouldNotBeFound("shortName.equals=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllLocationsByShortNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where shortName not equals to DEFAULT_SHORT_NAME
        defaultLocationShouldNotBeFound("shortName.notEquals=" + DEFAULT_SHORT_NAME);

        // Get all the locationList where shortName not equals to UPDATED_SHORT_NAME
        defaultLocationShouldBeFound("shortName.notEquals=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllLocationsByShortNameIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where shortName in DEFAULT_SHORT_NAME or UPDATED_SHORT_NAME
        defaultLocationShouldBeFound("shortName.in=" + DEFAULT_SHORT_NAME + "," + UPDATED_SHORT_NAME);

        // Get all the locationList where shortName equals to UPDATED_SHORT_NAME
        defaultLocationShouldNotBeFound("shortName.in=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllLocationsByShortNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where shortName is not null
        defaultLocationShouldBeFound("shortName.specified=true");

        // Get all the locationList where shortName is null
        defaultLocationShouldNotBeFound("shortName.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByShortNameContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where shortName contains DEFAULT_SHORT_NAME
        defaultLocationShouldBeFound("shortName.contains=" + DEFAULT_SHORT_NAME);

        // Get all the locationList where shortName contains UPDATED_SHORT_NAME
        defaultLocationShouldNotBeFound("shortName.contains=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllLocationsByShortNameNotContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where shortName does not contain DEFAULT_SHORT_NAME
        defaultLocationShouldNotBeFound("shortName.doesNotContain=" + DEFAULT_SHORT_NAME);

        // Get all the locationList where shortName does not contain UPDATED_SHORT_NAME
        defaultLocationShouldBeFound("shortName.doesNotContain=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllLocationsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where address equals to DEFAULT_ADDRESS
        defaultLocationShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the locationList where address equals to UPDATED_ADDRESS
        defaultLocationShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllLocationsByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where address not equals to DEFAULT_ADDRESS
        defaultLocationShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the locationList where address not equals to UPDATED_ADDRESS
        defaultLocationShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllLocationsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultLocationShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the locationList where address equals to UPDATED_ADDRESS
        defaultLocationShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllLocationsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where address is not null
        defaultLocationShouldBeFound("address.specified=true");

        // Get all the locationList where address is null
        defaultLocationShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByAddressContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where address contains DEFAULT_ADDRESS
        defaultLocationShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the locationList where address contains UPDATED_ADDRESS
        defaultLocationShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllLocationsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where address does not contain DEFAULT_ADDRESS
        defaultLocationShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the locationList where address does not contain UPDATED_ADDRESS
        defaultLocationShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllLocationsByPostalCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where postalCode equals to DEFAULT_POSTAL_CODE
        defaultLocationShouldBeFound("postalCode.equals=" + DEFAULT_POSTAL_CODE);

        // Get all the locationList where postalCode equals to UPDATED_POSTAL_CODE
        defaultLocationShouldNotBeFound("postalCode.equals=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllLocationsByPostalCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where postalCode not equals to DEFAULT_POSTAL_CODE
        defaultLocationShouldNotBeFound("postalCode.notEquals=" + DEFAULT_POSTAL_CODE);

        // Get all the locationList where postalCode not equals to UPDATED_POSTAL_CODE
        defaultLocationShouldBeFound("postalCode.notEquals=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllLocationsByPostalCodeIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where postalCode in DEFAULT_POSTAL_CODE or UPDATED_POSTAL_CODE
        defaultLocationShouldBeFound("postalCode.in=" + DEFAULT_POSTAL_CODE + "," + UPDATED_POSTAL_CODE);

        // Get all the locationList where postalCode equals to UPDATED_POSTAL_CODE
        defaultLocationShouldNotBeFound("postalCode.in=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllLocationsByPostalCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where postalCode is not null
        defaultLocationShouldBeFound("postalCode.specified=true");

        // Get all the locationList where postalCode is null
        defaultLocationShouldNotBeFound("postalCode.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByPostalCodeContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where postalCode contains DEFAULT_POSTAL_CODE
        defaultLocationShouldBeFound("postalCode.contains=" + DEFAULT_POSTAL_CODE);

        // Get all the locationList where postalCode contains UPDATED_POSTAL_CODE
        defaultLocationShouldNotBeFound("postalCode.contains=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllLocationsByPostalCodeNotContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where postalCode does not contain DEFAULT_POSTAL_CODE
        defaultLocationShouldNotBeFound("postalCode.doesNotContain=" + DEFAULT_POSTAL_CODE);

        // Get all the locationList where postalCode does not contain UPDATED_POSTAL_CODE
        defaultLocationShouldBeFound("postalCode.doesNotContain=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllLocationsByLatLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where latLocation equals to DEFAULT_LAT_LOCATION
        defaultLocationShouldBeFound("latLocation.equals=" + DEFAULT_LAT_LOCATION);

        // Get all the locationList where latLocation equals to UPDATED_LAT_LOCATION
        defaultLocationShouldNotBeFound("latLocation.equals=" + UPDATED_LAT_LOCATION);
    }

    @Test
    @Transactional
    void getAllLocationsByLatLocationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where latLocation not equals to DEFAULT_LAT_LOCATION
        defaultLocationShouldNotBeFound("latLocation.notEquals=" + DEFAULT_LAT_LOCATION);

        // Get all the locationList where latLocation not equals to UPDATED_LAT_LOCATION
        defaultLocationShouldBeFound("latLocation.notEquals=" + UPDATED_LAT_LOCATION);
    }

    @Test
    @Transactional
    void getAllLocationsByLatLocationIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where latLocation in DEFAULT_LAT_LOCATION or UPDATED_LAT_LOCATION
        defaultLocationShouldBeFound("latLocation.in=" + DEFAULT_LAT_LOCATION + "," + UPDATED_LAT_LOCATION);

        // Get all the locationList where latLocation equals to UPDATED_LAT_LOCATION
        defaultLocationShouldNotBeFound("latLocation.in=" + UPDATED_LAT_LOCATION);
    }

    @Test
    @Transactional
    void getAllLocationsByLatLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where latLocation is not null
        defaultLocationShouldBeFound("latLocation.specified=true");

        // Get all the locationList where latLocation is null
        defaultLocationShouldNotBeFound("latLocation.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByLatLocationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where latLocation is greater than or equal to DEFAULT_LAT_LOCATION
        defaultLocationShouldBeFound("latLocation.greaterThanOrEqual=" + DEFAULT_LAT_LOCATION);

        // Get all the locationList where latLocation is greater than or equal to UPDATED_LAT_LOCATION
        defaultLocationShouldNotBeFound("latLocation.greaterThanOrEqual=" + UPDATED_LAT_LOCATION);
    }

    @Test
    @Transactional
    void getAllLocationsByLatLocationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where latLocation is less than or equal to DEFAULT_LAT_LOCATION
        defaultLocationShouldBeFound("latLocation.lessThanOrEqual=" + DEFAULT_LAT_LOCATION);

        // Get all the locationList where latLocation is less than or equal to SMALLER_LAT_LOCATION
        defaultLocationShouldNotBeFound("latLocation.lessThanOrEqual=" + SMALLER_LAT_LOCATION);
    }

    @Test
    @Transactional
    void getAllLocationsByLatLocationIsLessThanSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where latLocation is less than DEFAULT_LAT_LOCATION
        defaultLocationShouldNotBeFound("latLocation.lessThan=" + DEFAULT_LAT_LOCATION);

        // Get all the locationList where latLocation is less than UPDATED_LAT_LOCATION
        defaultLocationShouldBeFound("latLocation.lessThan=" + UPDATED_LAT_LOCATION);
    }

    @Test
    @Transactional
    void getAllLocationsByLatLocationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where latLocation is greater than DEFAULT_LAT_LOCATION
        defaultLocationShouldNotBeFound("latLocation.greaterThan=" + DEFAULT_LAT_LOCATION);

        // Get all the locationList where latLocation is greater than SMALLER_LAT_LOCATION
        defaultLocationShouldBeFound("latLocation.greaterThan=" + SMALLER_LAT_LOCATION);
    }

    @Test
    @Transactional
    void getAllLocationsByLongLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where longLocation equals to DEFAULT_LONG_LOCATION
        defaultLocationShouldBeFound("longLocation.equals=" + DEFAULT_LONG_LOCATION);

        // Get all the locationList where longLocation equals to UPDATED_LONG_LOCATION
        defaultLocationShouldNotBeFound("longLocation.equals=" + UPDATED_LONG_LOCATION);
    }

    @Test
    @Transactional
    void getAllLocationsByLongLocationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where longLocation not equals to DEFAULT_LONG_LOCATION
        defaultLocationShouldNotBeFound("longLocation.notEquals=" + DEFAULT_LONG_LOCATION);

        // Get all the locationList where longLocation not equals to UPDATED_LONG_LOCATION
        defaultLocationShouldBeFound("longLocation.notEquals=" + UPDATED_LONG_LOCATION);
    }

    @Test
    @Transactional
    void getAllLocationsByLongLocationIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where longLocation in DEFAULT_LONG_LOCATION or UPDATED_LONG_LOCATION
        defaultLocationShouldBeFound("longLocation.in=" + DEFAULT_LONG_LOCATION + "," + UPDATED_LONG_LOCATION);

        // Get all the locationList where longLocation equals to UPDATED_LONG_LOCATION
        defaultLocationShouldNotBeFound("longLocation.in=" + UPDATED_LONG_LOCATION);
    }

    @Test
    @Transactional
    void getAllLocationsByLongLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where longLocation is not null
        defaultLocationShouldBeFound("longLocation.specified=true");

        // Get all the locationList where longLocation is null
        defaultLocationShouldNotBeFound("longLocation.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByLongLocationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where longLocation is greater than or equal to DEFAULT_LONG_LOCATION
        defaultLocationShouldBeFound("longLocation.greaterThanOrEqual=" + DEFAULT_LONG_LOCATION);

        // Get all the locationList where longLocation is greater than or equal to UPDATED_LONG_LOCATION
        defaultLocationShouldNotBeFound("longLocation.greaterThanOrEqual=" + UPDATED_LONG_LOCATION);
    }

    @Test
    @Transactional
    void getAllLocationsByLongLocationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where longLocation is less than or equal to DEFAULT_LONG_LOCATION
        defaultLocationShouldBeFound("longLocation.lessThanOrEqual=" + DEFAULT_LONG_LOCATION);

        // Get all the locationList where longLocation is less than or equal to SMALLER_LONG_LOCATION
        defaultLocationShouldNotBeFound("longLocation.lessThanOrEqual=" + SMALLER_LONG_LOCATION);
    }

    @Test
    @Transactional
    void getAllLocationsByLongLocationIsLessThanSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where longLocation is less than DEFAULT_LONG_LOCATION
        defaultLocationShouldNotBeFound("longLocation.lessThan=" + DEFAULT_LONG_LOCATION);

        // Get all the locationList where longLocation is less than UPDATED_LONG_LOCATION
        defaultLocationShouldBeFound("longLocation.lessThan=" + UPDATED_LONG_LOCATION);
    }

    @Test
    @Transactional
    void getAllLocationsByLongLocationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where longLocation is greater than DEFAULT_LONG_LOCATION
        defaultLocationShouldNotBeFound("longLocation.greaterThan=" + DEFAULT_LONG_LOCATION);

        // Get all the locationList where longLocation is greater than SMALLER_LONG_LOCATION
        defaultLocationShouldBeFound("longLocation.greaterThan=" + SMALLER_LONG_LOCATION);
    }

    @Test
    @Transactional
    void getAllLocationsByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);
        Country country = CountryResourceIT.createEntity(em);
        em.persist(country);
        em.flush();
        location.setCountry(country);
        locationRepository.saveAndFlush(location);
        Long countryId = country.getId();

        // Get all the locationList where country equals to countryId
        defaultLocationShouldBeFound("countryId.equals=" + countryId);

        // Get all the locationList where country equals to (countryId + 1)
        defaultLocationShouldNotBeFound("countryId.equals=" + (countryId + 1));
    }

    @Test
    @Transactional
    void getAllLocationsByItemsIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);
        Item items = ItemResourceIT.createEntity(em);
        em.persist(items);
        em.flush();
        location.addItems(items);
        locationRepository.saveAndFlush(location);
        UUID itemsId = items.getId();

        // Get all the locationList where items equals to itemsId
        defaultLocationShouldBeFound("itemsId.equals=" + itemsId);

        // Get all the locationList where items equals to UUID.randomUUID()
        defaultLocationShouldNotBeFound("itemsId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllLocationsByTradeEventsIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);
        TradeEvent tradeEvents = TradeEventResourceIT.createEntity(em);
        em.persist(tradeEvents);
        em.flush();
        location.addTradeEvents(tradeEvents);
        locationRepository.saveAndFlush(location);
        UUID tradeEventsId = tradeEvents.getId();

        // Get all the locationList where tradeEvents equals to tradeEventsId
        defaultLocationShouldBeFound("tradeEventsId.equals=" + tradeEventsId);

        // Get all the locationList where tradeEvents equals to UUID.randomUUID()
        defaultLocationShouldNotBeFound("tradeEventsId.equals=" + UUID.randomUUID());
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLocationShouldBeFound(String filter) throws Exception {
        restLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(location.getId().intValue())))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].latLocation").value(hasItem(DEFAULT_LAT_LOCATION.doubleValue())))
            .andExpect(jsonPath("$.[*].longLocation").value(hasItem(DEFAULT_LONG_LOCATION.doubleValue())));

        // Check, that the count call also returns 1
        restLocationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLocationShouldNotBeFound(String filter) throws Exception {
        restLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLocationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLocation() throws Exception {
        // Get the location
        restLocationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLocation() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        int databaseSizeBeforeUpdate = locationRepository.findAll().size();

        // Update the location
        Location updatedLocation = locationRepository.findById(location.getId()).get();
        // Disconnect from session so that the updates on updatedLocation are not directly saved in db
        em.detach(updatedLocation);
        updatedLocation
            .shortName(UPDATED_SHORT_NAME)
            .address(UPDATED_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .latLocation(UPDATED_LAT_LOCATION)
            .longLocation(UPDATED_LONG_LOCATION);

        restLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLocation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLocation))
            )
            .andExpect(status().isOk());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
        Location testLocation = locationList.get(locationList.size() - 1);
        assertThat(testLocation.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testLocation.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testLocation.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testLocation.getLatLocation()).isEqualTo(UPDATED_LAT_LOCATION);
        assertThat(testLocation.getLongLocation()).isEqualTo(UPDATED_LONG_LOCATION);
    }

    @Test
    @Transactional
    void putNonExistingLocation() throws Exception {
        int databaseSizeBeforeUpdate = locationRepository.findAll().size();
        location.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, location.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(location))
            )
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLocation() throws Exception {
        int databaseSizeBeforeUpdate = locationRepository.findAll().size();
        location.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(location))
            )
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLocation() throws Exception {
        int databaseSizeBeforeUpdate = locationRepository.findAll().size();
        location.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(location)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLocationWithPatch() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        int databaseSizeBeforeUpdate = locationRepository.findAll().size();

        // Update the location using partial update
        Location partialUpdatedLocation = new Location();
        partialUpdatedLocation.setId(location.getId());

        partialUpdatedLocation.shortName(UPDATED_SHORT_NAME).postalCode(UPDATED_POSTAL_CODE).longLocation(UPDATED_LONG_LOCATION);

        restLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocation))
            )
            .andExpect(status().isOk());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
        Location testLocation = locationList.get(locationList.size() - 1);
        assertThat(testLocation.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testLocation.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testLocation.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testLocation.getLatLocation()).isEqualTo(DEFAULT_LAT_LOCATION);
        assertThat(testLocation.getLongLocation()).isEqualTo(UPDATED_LONG_LOCATION);
    }

    @Test
    @Transactional
    void fullUpdateLocationWithPatch() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        int databaseSizeBeforeUpdate = locationRepository.findAll().size();

        // Update the location using partial update
        Location partialUpdatedLocation = new Location();
        partialUpdatedLocation.setId(location.getId());

        partialUpdatedLocation
            .shortName(UPDATED_SHORT_NAME)
            .address(UPDATED_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .latLocation(UPDATED_LAT_LOCATION)
            .longLocation(UPDATED_LONG_LOCATION);

        restLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocation))
            )
            .andExpect(status().isOk());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
        Location testLocation = locationList.get(locationList.size() - 1);
        assertThat(testLocation.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testLocation.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testLocation.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testLocation.getLatLocation()).isEqualTo(UPDATED_LAT_LOCATION);
        assertThat(testLocation.getLongLocation()).isEqualTo(UPDATED_LONG_LOCATION);
    }

    @Test
    @Transactional
    void patchNonExistingLocation() throws Exception {
        int databaseSizeBeforeUpdate = locationRepository.findAll().size();
        location.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, location.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(location))
            )
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLocation() throws Exception {
        int databaseSizeBeforeUpdate = locationRepository.findAll().size();
        location.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(location))
            )
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLocation() throws Exception {
        int databaseSizeBeforeUpdate = locationRepository.findAll().size();
        location.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(location)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLocation() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        int databaseSizeBeforeDelete = locationRepository.findAll().size();

        // Delete the location
        restLocationMockMvc
            .perform(delete(ENTITY_API_URL_ID, location.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
