package com.triippztech.freshtrade.service.criteria;

import com.triippztech.freshtrade.domain.enumeration.Condition;
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
 * Criteria class for the {@link com.triippztech.freshtrade.domain.Item} entity. This class is used
 * in {@link com.triippztech.freshtrade.web.rest.ItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ItemCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Condition
     */
    public static class ConditionFilter extends Filter<Condition> {

        public ConditionFilter() {}

        public ConditionFilter(ConditionFilter filter) {
            super(filter);
        }

        @Override
        public ConditionFilter copy() {
            return new ConditionFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private DoubleFilter price;

    private IntegerFilter quantity;

    private StringFilter name;

    private ConditionFilter itemCondition;

    private BooleanFilter isActive;

    private ZonedDateTimeFilter createdDate;

    private ZonedDateTimeFilter updatedDate;

    private UUIDFilter imagesId;

    private LongFilter tokensId;

    private LongFilter ownerId;

    private LongFilter locationId;

    private UUIDFilter tradeEventId;

    private UUIDFilter categoriesId;

    private LongFilter userId;

    public ItemCriteria() {}

    public ItemCriteria(ItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.quantity = other.quantity == null ? null : other.quantity.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.itemCondition = other.itemCondition == null ? null : other.itemCondition.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.updatedDate = other.updatedDate == null ? null : other.updatedDate.copy();
        this.imagesId = other.imagesId == null ? null : other.imagesId.copy();
        this.tokensId = other.tokensId == null ? null : other.tokensId.copy();
        this.ownerId = other.ownerId == null ? null : other.ownerId.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
        this.tradeEventId = other.tradeEventId == null ? null : other.tradeEventId.copy();
        this.categoriesId = other.categoriesId == null ? null : other.categoriesId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public ItemCriteria copy() {
        return new ItemCriteria(this);
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

    public DoubleFilter getPrice() {
        return price;
    }

    public DoubleFilter price() {
        if (price == null) {
            price = new DoubleFilter();
        }
        return price;
    }

    public void setPrice(DoubleFilter price) {
        this.price = price;
    }

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public IntegerFilter quantity() {
        if (quantity == null) {
            quantity = new IntegerFilter();
        }
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public ConditionFilter getItemCondition() {
        return itemCondition;
    }

    public ConditionFilter itemCondition() {
        if (itemCondition == null) {
            itemCondition = new ConditionFilter();
        }
        return itemCondition;
    }

    public void setItemCondition(ConditionFilter itemCondition) {
        this.itemCondition = itemCondition;
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

    public UUIDFilter getImagesId() {
        return imagesId;
    }

    public UUIDFilter imagesId() {
        if (imagesId == null) {
            imagesId = new UUIDFilter();
        }
        return imagesId;
    }

    public void setImagesId(UUIDFilter imagesId) {
        this.imagesId = imagesId;
    }

    public LongFilter getTokensId() {
        return tokensId;
    }

    public LongFilter tokensId() {
        if (tokensId == null) {
            tokensId = new LongFilter();
        }
        return tokensId;
    }

    public void setTokensId(LongFilter tokensId) {
        this.tokensId = tokensId;
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

    public UUIDFilter getTradeEventId() {
        return tradeEventId;
    }

    public UUIDFilter tradeEventId() {
        if (tradeEventId == null) {
            tradeEventId = new UUIDFilter();
        }
        return tradeEventId;
    }

    public void setTradeEventId(UUIDFilter tradeEventId) {
        this.tradeEventId = tradeEventId;
    }

    public UUIDFilter getCategoriesId() {
        return categoriesId;
    }

    public UUIDFilter categoriesId() {
        if (categoriesId == null) {
            categoriesId = new UUIDFilter();
        }
        return categoriesId;
    }

    public void setCategoriesId(UUIDFilter categoriesId) {
        this.categoriesId = categoriesId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ItemCriteria that = (ItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(price, that.price) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(name, that.name) &&
            Objects.equals(itemCondition, that.itemCondition) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(updatedDate, that.updatedDate) &&
            Objects.equals(imagesId, that.imagesId) &&
            Objects.equals(tokensId, that.tokensId) &&
            Objects.equals(ownerId, that.ownerId) &&
            Objects.equals(locationId, that.locationId) &&
            Objects.equals(tradeEventId, that.tradeEventId) &&
            Objects.equals(categoriesId, that.categoriesId) &&
            Objects.equals(userId, that.userId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            price,
            quantity,
            name,
            itemCondition,
            isActive,
            createdDate,
            updatedDate,
            imagesId,
            tokensId,
            ownerId,
            locationId,
            tradeEventId,
            categoriesId,
            userId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (quantity != null ? "quantity=" + quantity + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (itemCondition != null ? "itemCondition=" + itemCondition + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (updatedDate != null ? "updatedDate=" + updatedDate + ", " : "") +
            (imagesId != null ? "imagesId=" + imagesId + ", " : "") +
            (tokensId != null ? "tokensId=" + tokensId + ", " : "") +
            (ownerId != null ? "ownerId=" + ownerId + ", " : "") +
            (locationId != null ? "locationId=" + locationId + ", " : "") +
            (tradeEventId != null ? "tradeEventId=" + tradeEventId + ", " : "") +
            (categoriesId != null ? "categoriesId=" + categoriesId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }
}
