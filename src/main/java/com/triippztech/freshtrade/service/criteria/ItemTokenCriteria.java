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
 * Criteria class for the {@link com.triippztech.freshtrade.domain.ItemToken} entity. This class is used
 * in {@link com.triippztech.freshtrade.web.rest.ItemTokenResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /item-tokens?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ItemTokenCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tokenName;

    private StringFilter tokenCode;

    private ZonedDateTimeFilter createdDate;

    private ZonedDateTimeFilter updatedDate;

    private LongFilter ownerId;

    private UUIDFilter itemId;

    public ItemTokenCriteria() {}

    public ItemTokenCriteria(ItemTokenCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tokenName = other.tokenName == null ? null : other.tokenName.copy();
        this.tokenCode = other.tokenCode == null ? null : other.tokenCode.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.updatedDate = other.updatedDate == null ? null : other.updatedDate.copy();
        this.ownerId = other.ownerId == null ? null : other.ownerId.copy();
        this.itemId = other.itemId == null ? null : other.itemId.copy();
    }

    @Override
    public ItemTokenCriteria copy() {
        return new ItemTokenCriteria(this);
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

    public StringFilter getTokenName() {
        return tokenName;
    }

    public StringFilter tokenName() {
        if (tokenName == null) {
            tokenName = new StringFilter();
        }
        return tokenName;
    }

    public void setTokenName(StringFilter tokenName) {
        this.tokenName = tokenName;
    }

    public StringFilter getTokenCode() {
        return tokenCode;
    }

    public StringFilter tokenCode() {
        if (tokenCode == null) {
            tokenCode = new StringFilter();
        }
        return tokenCode;
    }

    public void setTokenCode(StringFilter tokenCode) {
        this.tokenCode = tokenCode;
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

    public LongFilter getOwnerId() {
        return ownerId;
    }

    public LongFilter ownerId() {
        if (ownerId == null) {
            ownerId = new LongFilter();
        }
        return ownerId;
    }

    public void setOwnerId(LongFilter ownerId) {
        this.ownerId = ownerId;
    }

    public UUIDFilter getItemId() {
        return itemId;
    }

    public UUIDFilter itemId() {
        if (itemId == null) {
            itemId = new UUIDFilter();
        }
        return itemId;
    }

    public void setItemId(UUIDFilter itemId) {
        this.itemId = itemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ItemTokenCriteria that = (ItemTokenCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tokenName, that.tokenName) &&
            Objects.equals(tokenCode, that.tokenCode) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(updatedDate, that.updatedDate) &&
            Objects.equals(ownerId, that.ownerId) &&
            Objects.equals(itemId, that.itemId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tokenName, tokenCode, createdDate, updatedDate, ownerId, itemId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemTokenCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tokenName != null ? "tokenName=" + tokenName + ", " : "") +
            (tokenCode != null ? "tokenCode=" + tokenCode + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (updatedDate != null ? "updatedDate=" + updatedDate + ", " : "") +
            (ownerId != null ? "ownerId=" + ownerId + ", " : "") +
            (itemId != null ? "itemId=" + itemId + ", " : "") +
            "}";
    }
}
