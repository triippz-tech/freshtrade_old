package com.triippztech.freshtrade.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.triippztech.freshtrade.domain.TradeEvent} entity. This class is used
 * in {@link com.triippztech.freshtrade.web.rest.TradeEventResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /trade-events?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TradeEventCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter eventName;

    private StringFilter eventDescription;

    private ZonedDateTimeFilter startDate;

    private ZonedDateTimeFilter endDate;

    private BooleanFilter isActive;

    private LongFilter locationId;

    private UUIDFilter itemsId;

    private UUIDFilter reservationsId;

    public TradeEventCriteria() {}

    public TradeEventCriteria(TradeEventCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.eventName = other.eventName == null ? null : other.eventName.copy();
        this.eventDescription = other.eventDescription == null ? null : other.eventDescription.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
        this.itemsId = other.itemsId == null ? null : other.itemsId.copy();
        this.reservationsId = other.reservationsId == null ? null : other.reservationsId.copy();
    }

    @Override
    public TradeEventCriteria copy() {
        return new TradeEventCriteria(this);
    }

    public UUIDFilter getId() {
        return id;
    }

    public UUIDFilter id() {
        if (id == null) {
            id = new UUIDFilter();
        }
        return id;
    }

    public void setId(UUIDFilter id) {
        this.id = id;
    }

    public StringFilter getEventName() {
        return eventName;
    }

    public StringFilter eventName() {
        if (eventName == null) {
            eventName = new StringFilter();
        }
        return eventName;
    }

    public void setEventName(StringFilter eventName) {
        this.eventName = eventName;
    }

    public StringFilter getEventDescription() {
        return eventDescription;
    }

    public StringFilter eventDescription() {
        if (eventDescription == null) {
            eventDescription = new StringFilter();
        }
        return eventDescription;
    }

    public void setEventDescription(StringFilter eventDescription) {
        this.eventDescription = eventDescription;
    }

    public ZonedDateTimeFilter getStartDate() {
        return startDate;
    }

    public ZonedDateTimeFilter startDate() {
        if (startDate == null) {
            startDate = new ZonedDateTimeFilter();
        }
        return startDate;
    }

    public void setStartDate(ZonedDateTimeFilter startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTimeFilter getEndDate() {
        return endDate;
    }

    public ZonedDateTimeFilter endDate() {
        if (endDate == null) {
            endDate = new ZonedDateTimeFilter();
        }
        return endDate;
    }

    public void setEndDate(ZonedDateTimeFilter endDate) {
        this.endDate = endDate;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            isActive = new BooleanFilter();
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public LongFilter getLocationId() {
        return locationId;
    }

    public LongFilter locationId() {
        if (locationId == null) {
            locationId = new LongFilter();
        }
        return locationId;
    }

    public void setLocationId(LongFilter locationId) {
        this.locationId = locationId;
    }

    public UUIDFilter getItemsId() {
        return itemsId;
    }

    public UUIDFilter itemsId() {
        if (itemsId == null) {
            itemsId = new UUIDFilter();
        }
        return itemsId;
    }

    public void setItemsId(UUIDFilter itemsId) {
        this.itemsId = itemsId;
    }

    public UUIDFilter getReservationsId() {
        return reservationsId;
    }

    public UUIDFilter reservationsId() {
        if (reservationsId == null) {
            reservationsId = new UUIDFilter();
        }
        return reservationsId;
    }

    public void setReservationsId(UUIDFilter reservationsId) {
        this.reservationsId = reservationsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TradeEventCriteria that = (TradeEventCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(eventName, that.eventName) &&
            Objects.equals(eventDescription, that.eventDescription) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(locationId, that.locationId) &&
            Objects.equals(itemsId, that.itemsId) &&
            Objects.equals(reservationsId, that.reservationsId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eventName, eventDescription, startDate, endDate, isActive, locationId, itemsId, reservationsId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TradeEventCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (eventName != null ? "eventName=" + eventName + ", " : "") +
            (eventDescription != null ? "eventDescription=" + eventDescription + ", " : "") +
            (startDate != null ? "startDate=" + startDate + ", " : "") +
            (endDate != null ? "endDate=" + endDate + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (locationId != null ? "locationId=" + locationId + ", " : "") +
            (itemsId != null ? "itemsId=" + itemsId + ", " : "") +
            (reservationsId != null ? "reservationsId=" + reservationsId + ", " : "") +
            "}";
    }
}
