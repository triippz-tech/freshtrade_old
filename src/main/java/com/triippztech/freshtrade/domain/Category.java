package com.triippztech.freshtrade.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Category.
 */
@Entity
@Table(name = "category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    @Size(max = 50)
    @Column(name = "slug", length = 50, nullable = false)
    private String slug;

    @NotNull
    @Size(max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_featured")
    private Boolean isFeatured;

    @ManyToMany(mappedBy = "categories")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "images", "tokens", "owner", "location", "tradeEvent", "categories", "users" }, allowSetters = true)
    private Set<Item> items = new HashSet<>();

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "item", "category" }, allowSetters = true)
    private Set<Image> images = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Category id(UUID id) {
        this.id = id;
        return this;
    }

    public String getSlug() {
        return this.slug;
    }

    public Category slug(String slug) {
        this.slug = slug;
        return this;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return this.name;
    }

    public Category name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getCreatedDate() {
        return this.createdDate;
    }

    public Category createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Category isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<Item> getItems() {
        return this.items;
    }

    public Category items(Set<Item> items) {
        this.setItems(items);
        return this;
    }

    public Category addItems(Item item) {
        this.items.add(item);
        item.getCategories().add(this);
        return this;
    }

    public Category removeItems(Item item) {
        this.items.remove(item);
        item.getCategories().remove(this);
        return this;
    }

    public void setItems(Set<Item> items) {
        if (this.items != null) {
            this.items.forEach(i -> i.removeCategories(this));
        }
        if (items != null) {
            items.forEach(i -> i.addCategories(this));
        }
        this.items = items;
    }

    public Category images(Set<Image> images) {
        this.setImages(images);
        return this;
    }

    public Category addImage(Image image) {
        this.images.add(image);
        return this;
    }

    public Category removeImage(Image image) {
        this.images.remove(image);
        return this;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

    public Set<Image> getImages() {
        return images;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean featured) {
        isFeatured = featured;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Category)) {
            return false;
        }
        return id != null && id.equals(((Category) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Category{" +
            "id=" + getId() +
            ", slug='" + getSlug() + "'" +
            ", name='" + getName() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", isFeatured='" + getIsFeatured() + "'" +
            "}";
    }
}
