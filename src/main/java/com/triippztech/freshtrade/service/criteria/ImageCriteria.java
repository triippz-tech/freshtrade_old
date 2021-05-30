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
 * Criteria class for the {@link com.triippztech.freshtrade.domain.Image} entity. This class is used
 * in {@link com.triippztech.freshtrade.web.rest.ImageResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /images?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ImageCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter imageUrl;

    private ZonedDateTimeFilter createdDate;

    private BooleanFilter isVisible;

    private UUIDFilter itemId;

    public ImageCriteria() {}

    public ImageCriteria(ImageCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.imageUrl = other.imageUrl == null ? null : other.imageUrl.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.isVisible = other.isVisible == null ? null : other.isVisible.copy();
        this.itemId = other.itemId == null ? null : other.itemId.copy();
    }

    @Override
    public ImageCriteria copy() {
        return new ImageCriteria(this);
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

    public StringFilter getImageUrl() {
        return imageUrl;
    }

    public StringFilter imageUrl() {
        if (imageUrl == null) {
            imageUrl = new StringFilter();
        }
        return imageUrl;
    }

    public void setImageUrl(StringFilter imageUrl) {
        this.imageUrl = imageUrl;
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

    public BooleanFilter getIsVisible() {
        return isVisible;
    }

    public BooleanFilter isVisible() {
        if (isVisible == null) {
            isVisible = new BooleanFilter();
        }
        return isVisible;
    }

    public void setIsVisible(BooleanFilter isVisible) {
        this.isVisible = isVisible;
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
        final ImageCriteria that = (ImageCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(imageUrl, that.imageUrl) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(isVisible, that.isVisible) &&
            Objects.equals(itemId, that.itemId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, imageUrl, createdDate, isVisible, itemId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImageCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (imageUrl != null ? "imageUrl=" + imageUrl + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (isVisible != null ? "isVisible=" + isVisible + ", " : "") +
            (itemId != null ? "itemId=" + itemId + ", " : "") +
            "}";
    }
}
