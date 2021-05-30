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
 * Criteria class for the {@link com.triippztech.freshtrade.domain.Reservation} entity. This class is used
 * in {@link com.triippztech.freshtrade.web.rest.ReservationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /reservations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ReservationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter reservationNumber;

    private BooleanFilter isActive;

    private BooleanFilter isCancelled;

    private ZonedDateTimeFilter pickupTime;

    private ZonedDateTimeFilter createdDate;

    private ZonedDateTimeFilter updatedDate;

    private LongFilter sellerId;

    private LongFilter buyerId;

    private UUIDFilter eventId;

    public ReservationCriteria() {}

    public ReservationCriteria(ReservationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.reservationNumber = other.reservationNumber == null ? null : other.reservationNumber.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.isCancelled = other.isCancelled == null ? null : other.isCancelled.copy();
        this.pickupTime = other.pickupTime == null ? null : other.pickupTime.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.updatedDate = other.updatedDate == null ? null : other.updatedDate.copy();
        this.sellerId = other.sellerId == null ? null : other.sellerId.copy();
        this.buyerId = other.buyerId == null ? null : other.buyerId.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
    }

    @Override
    public ReservationCriteria copy() {
        return new ReservationCriteria(this);
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

    public StringFilter getReservationNumber() {
        return reservationNumber;
    }

    public StringFilter reservationNumber() {
        if (reservationNumber == null) {
            reservationNumber = new StringFilter();
        }
        return reservationNumber;
    }

    public void setReservationNumber(StringFilter reservationNumber) {
        this.reservationNumber = reservationNumber;
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

    public BooleanFilter getIsCancelled() {
        return isCancelled;
    }

    public BooleanFilter isCancelled() {
        if (isCancelled == null) {
            isCancelled = new BooleanFilter();
        }
        return isCancelled;
    }

    public void setIsCancelled(BooleanFilter isCancelled) {
        this.isCancelled = isCancelled;
    }

    public ZonedDateTimeFilter getPickupTime() {
        return pickupTime;
    }

    public ZonedDateTimeFilter pickupTime() {
        if (pickupTime == null) {
            pickupTime = new ZonedDateTimeFilter();
        }
        return pickupTime;
    }

    public void setPickupTime(ZonedDateTimeFilter pickupTime) {
        this.pickupTime = pickupTime;
    }

    public ZonedDateTimeFilter getCreatedDate() {
        return createdDate;
    }

    public ZonedDateTimeFilter createdDate() {
        if (createdDate == null) {
            createdDate = new ZonedDateTimeFilter();
        }
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTimeFilter createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTimeFilter getUpdatedDate() {
        return updatedDate;
    }

    public ZonedDateTimeFilter updatedDate() {
        if (updatedDate == null) {
            updatedDate = new ZonedDateTimeFilter();
        }
        return updatedDate;
    }

    public void setUpdatedDate(ZonedDateTimeFilter updatedDate) {
        this.updatedDate = updatedDate;
    }

    public LongFilter getSellerId() {
        return sellerId;
    }

    public LongFilter sellerId() {
        if (sellerId == null) {
            sellerId = new LongFilter();
        }
        return sellerId;
    }

    public void setSellerId(LongFilter sellerId) {
        this.sellerId = sellerId;
    }

    public LongFilter getBuyerId() {
        return buyerId;
    }

    public LongFilter buyerId() {
        if (buyerId == null) {
            buyerId = new LongFilter();
        }
        return buyerId;
    }

    public void setBuyerId(LongFilter buyerId) {
        this.buyerId = buyerId;
    }

    public UUIDFilter getEventId() {
        return eventId;
    }

    public UUIDFilter eventId() {
        if (eventId == null) {
            eventId = new UUIDFilter();
        }
        return eventId;
    }

    public void setEventId(UUIDFilter eventId) {
        this.eventId = eventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ReservationCriteria that = (ReservationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(reservationNumber, that.reservationNumber) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(isCancelled, that.isCancelled) &&
            Objects.equals(pickupTime, that.pickupTime) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(updatedDate, that.updatedDate) &&
            Objects.equals(sellerId, that.sellerId) &&
            Objects.equals(buyerId, that.buyerId) &&
            Objects.equals(eventId, that.eventId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reservationNumber, isActive, isCancelled, pickupTime, createdDate, updatedDate, sellerId, buyerId, eventId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReservationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (reservationNumber != null ? "reservationNumber=" + reservationNumber + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (isCancelled != null ? "isCancelled=" + isCancelled + ", " : "") +
            (pickupTime != null ? "pickupTime=" + pickupTime + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (updatedDate != null ? "updatedDate=" + updatedDate + ", " : "") +
            (sellerId != null ? "sellerId=" + sellerId + ", " : "") +
            (buyerId != null ? "buyerId=" + buyerId + ", " : "") +
            (eventId != null ? "eventId=" + eventId + ", " : "") +
            "}";
    }
}
