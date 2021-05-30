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

/**
 * Criteria class for the {@link com.triippztech.freshtrade.domain.Location} entity. This class is used
 * in {@link com.triippztech.freshtrade.web.rest.LocationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /locations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LocationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter shortName;

    private StringFilter address;

    private StringFilter postalCode;

    private DoubleFilter latLocation;

    private DoubleFilter longLocation;

    private LongFilter countryId;

    private UUIDFilter itemsId;

    private UUIDFilter tradeEventsId;

    public LocationCriteria() {}

    public LocationCriteria(LocationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.shortName = other.shortName == null ? null : other.shortName.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.postalCode = other.postalCode == null ? null : other.postalCode.copy();
        this.latLocation = other.latLocation == null ? null : other.latLocation.copy();
        this.longLocation = other.longLocation == null ? null : other.longLocation.copy();
        this.countryId = other.countryId == null ? null : other.countryId.copy();
        this.itemsId = other.itemsId == null ? null : other.itemsId.copy();
        this.tradeEventsId = other.tradeEventsId == null ? null : other.tradeEventsId.copy();
    }

    @Override
    public LocationCriteria copy() {
        return new LocationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getShortName() {
        return shortName;
    }

    public StringFilter shortName() {
        if (shortName == null) {
            shortName = new StringFilter();
        }
        return shortName;
    }

    public void setShortName(StringFilter shortName) {
        this.shortName = shortName;
    }

    public StringFilter getAddress() {
        return address;
    }

    public StringFilter address() {
        if (address == null) {
            address = new StringFilter();
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getPostalCode() {
        return postalCode;
    }

    public StringFilter postalCode() {
        if (postalCode == null) {
            postalCode = new StringFilter();
        }
        return postalCode;
    }

    public void setPostalCode(StringFilter postalCode) {
        this.postalCode = postalCode;
    }

    public DoubleFilter getLatLocation() {
        return latLocation;
    }

    public DoubleFilter latLocation() {
        if (latLocation == null) {
            latLocation = new DoubleFilter();
        }
        return latLocation;
    }

    public void setLatLocation(DoubleFilter latLocation) {
        this.latLocation = latLocation;
    }

    public DoubleFilter getLongLocation() {
        return longLocation;
    }

    public DoubleFilter longLocation() {
        if (longLocation == null) {
            longLocation = new DoubleFilter();
        }
        return longLocation;
    }

    public void setLongLocation(DoubleFilter longLocation) {
        this.longLocation = longLocation;
    }

    public LongFilter getCountryId() {
        return countryId;
    }

    public LongFilter countryId() {
        if (countryId == null) {
            countryId = new LongFilter();
        }
        return countryId;
    }

    public void setCountryId(LongFilter countryId) {
        this.countryId = countryId;
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

    public UUIDFilter getTradeEventsId() {
        return tradeEventsId;
    }

    public UUIDFilter tradeEventsId() {
        if (tradeEventsId == null) {
            tradeEventsId = new UUIDFilter();
        }
        return tradeEventsId;
    }

    public void setTradeEventsId(UUIDFilter tradeEventsId) {
        this.tradeEventsId = tradeEventsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LocationCriteria that = (LocationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(shortName, that.shortName) &&
            Objects.equals(address, that.address) &&
            Objects.equals(postalCode, that.postalCode) &&
            Objects.equals(latLocation, that.latLocation) &&
            Objects.equals(longLocation, that.longLocation) &&
            Objects.equals(countryId, that.countryId) &&
            Objects.equals(itemsId, that.itemsId) &&
            Objects.equals(tradeEventsId, that.tradeEventsId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shortName, address, postalCode, latLocation, longLocation, countryId, itemsId, tradeEventsId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (shortName != null ? "shortName=" + shortName + ", " : "") +
            (address != null ? "address=" + address + ", " : "") +
            (postalCode != null ? "postalCode=" + postalCode + ", " : "") +
            (latLocation != null ? "latLocation=" + latLocation + ", " : "") +
            (longLocation != null ? "longLocation=" + longLocation + ", " : "") +
            (countryId != null ? "countryId=" + countryId + ", " : "") +
            (itemsId != null ? "itemsId=" + itemsId + ", " : "") +
            (tradeEventsId != null ? "tradeEventsId=" + tradeEventsId + ", " : "") +
            "}";
    }
}
