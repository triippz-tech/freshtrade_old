package com.triippztech.freshtrade.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.triippztech.freshtrade.domain.enumeration.Condition;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Item.
 */
@Entity
@Table(name = "item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    @NotNull
    @Min(value = 1)
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "details", nullable = false)
    private String details;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_condition")
    private Condition itemCondition;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "updated_date")
    private ZonedDateTime updatedDate;

    @OneToMany(mappedBy = "item", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "item" }, allowSetters = true)
    private Set<Image> images = new HashSet<>();

    @OneToMany(mappedBy = "item")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "owners", "item" }, allowSetters = true)
    private Set<ItemToken> tokens = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(
        value = {
            "langKey", "activated", "email", "password", "firstName", "lastName", "activationKey", "resetKey", "resetDate", "authorities",
        },
        allowSetters = true
    )
    private User owner;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "country", "items", "tradeEvents" }, allowSetters = true)
    private Location location;

    @ManyToOne
    @JsonIgnoreProperties(value = { "items", "reservations" }, allowSetters = true)
    private TradeEvent tradeEvent;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_item__categories",
        joinColumns = @JoinColumn(name = "item_id"),
        inverseJoinColumns = @JoinColumn(name = "categories_id")
    )
    @JsonIgnoreProperties(value = { "items" }, allowSetters = true)
    private Set<Category> categories = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "rel_item__user", joinColumns = @JoinColumn(name = "item_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Item id(UUID id) {
        this.id = id;
        return this;
    }

    public Double getPrice() {
        return this.price;
    }

    public Item price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public Item quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return this.name;
    }

    public Item name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return this.details;
    }

    public Item details(String details) {
        this.details = details;
        return this;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Condition getItemCondition() {
        return this.itemCondition;
    }

    public Item itemCondition(Condition itemCondition) {
        this.itemCondition = itemCondition;
        return this;
    }

    public void setItemCondition(Condition itemCondition) {
        this.itemCondition = itemCondition;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Item isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public ZonedDateTime getCreatedDate() {
        return this.createdDate;
    }

    public Item createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getUpdatedDate() {
        return this.updatedDate;
    }

    public Item updatedDate(ZonedDateTime updatedDate) {
        this.updatedDate = updatedDate;
        return this;
    }

    public void setUpdatedDate(ZonedDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Set<Image> getImages() {
        return this.images;
    }

    public Item images(Set<Image> images) {
        this.setImages(images);
        return this;
    }

    public Item addImages(Image image) {
        this.images.add(image);
        image.setItem(this);
        return this;
    }

    public Item removeImages(Image image) {
        this.images.remove(image);
        image.setItem(null);
        return this;
    }

    public void setImages(Set<Image> images) {
        if (this.images != null) {
            this.images.forEach(i -> i.setItem(null));
        }
        if (images != null) {
            images.forEach(i -> i.setItem(this));
        }
        this.images = images;
    }

    public Set<ItemToken> getTokens() {
        return this.tokens;
    }

    public Item tokens(Set<ItemToken> itemTokens) {
        this.setTokens(itemTokens);
        return this;
    }

    public Item addTokens(ItemToken itemToken) {
        this.tokens.add(itemToken);
        itemToken.setItem(this);
        return this;
    }

    public Item removeTokens(ItemToken itemToken) {
        this.tokens.remove(itemToken);
        itemToken.setItem(null);
        return this;
    }

    public void setTokens(Set<ItemToken> itemTokens) {
        if (this.tokens != null) {
            this.tokens.forEach(i -> i.setItem(null));
        }
        if (itemTokens != null) {
            itemTokens.forEach(i -> i.setItem(this));
        }
        this.tokens = itemTokens;
    }

    public User getOwner() {
        return this.owner;
    }

    public Item owner(User user) {
        this.setOwner(user);
        return this;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public Location getLocation() {
        return this.location;
    }

    public Item location(Location location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public TradeEvent getTradeEvent() {
        return this.tradeEvent;
    }

    public Item tradeEvent(TradeEvent tradeEvent) {
        this.setTradeEvent(tradeEvent);
        return this;
    }

    public void setTradeEvent(TradeEvent tradeEvent) {
        this.tradeEvent = tradeEvent;
    }

    public Set<Category> getCategories() {
        return this.categories;
    }

    public Item categories(Set<Category> categories) {
        this.setCategories(categories);
        return this;
    }

    public Item addCategories(Category category) {
        this.categories.add(category);
        category.getItems().add(this);
        return this;
    }

    public Item removeCategories(Category category) {
        this.categories.remove(category);
        category.getItems().remove(this);
        return this;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public Item users(Set<User> users) {
        this.setUsers(users);
        return this;
    }

    public Item addUser(User user) {
        this.users.add(user);
        return this;
    }

    public Item removeUser(User user) {
        this.users.remove(user);
        return this;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Item)) {
            return false;
        }
        return id != null && id.equals(((Item) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Item{" +
            "id=" + getId() +
            ", price=" + getPrice() +
            ", quantity=" + getQuantity() +
            ", name='" + getName() + "'" +
            ", details='" + getDetails() + "'" +
            ", itemCondition='" + getItemCondition() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            "}";
    }
}
