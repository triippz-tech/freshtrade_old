package com.triippztech.freshtrade.service.dto.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.triippztech.freshtrade.domain.*;
import com.triippztech.freshtrade.domain.enumeration.Condition;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ItemDetailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private Double price;

    private Integer quantity;

    private String name;

    private String details;

    private Condition itemCondition;

    private Integer availableTokens;

    @JsonIgnoreProperties(value = { "item" }, allowSetters = true)
    private Set<Image> images = new HashSet<>();

    @JsonIgnoreProperties(
        value = {
            "langKey", "activated", "email", "password", "firstName", "lastName", "activationKey", "resetKey", "resetDate", "authorities",
        },
        allowSetters = true
    )
    private User owner;

    @JsonIgnoreProperties(value = { "country", "items", "tradeEvents" }, allowSetters = true)
    private Location location;

    @JsonIgnoreProperties(value = { "items", "reservations" }, allowSetters = true)
    private TradeEvent tradeEvent;

    @JsonIgnoreProperties(value = { "owners", "item" }, allowSetters = true)
    private Set<ItemToken> tokens = new HashSet<>();

    @JsonIgnoreProperties(value = { "items" }, allowSetters = true)
    private Set<Category> categories = new HashSet<>();

    public ItemDetailDTO() {}

    public ItemDetailDTO(Item item) {
        this.id = item.getId();
        this.price = item.getPrice();
        this.quantity = item.getQuantity();
        this.name = item.getName();
        this.itemCondition = item.getItemCondition();
        this.images = item.getImages();
        this.owner = item.getOwner();
        this.location = item.getLocation();
        this.tradeEvent = item.getTradeEvent();
        this.tokens = item.getTokens();
        this.categories = item.getCategories();
        this.details = item.getDetails();
    }

    public ItemDetailDTO(Item item, Integer availableTokens) {
        this.id = item.getId();
        this.price = item.getPrice();
        this.quantity = item.getQuantity();
        this.name = item.getName();
        this.itemCondition = item.getItemCondition();
        this.images = item.getImages();
        this.owner = item.getOwner();
        this.location = item.getLocation();
        this.tradeEvent = item.getTradeEvent();
        this.tokens = item.getTokens();
        this.categories = item.getCategories();
        this.availableTokens = availableTokens;
        this.details = item.getDetails();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Condition getItemCondition() {
        return itemCondition;
    }

    public void setItemCondition(Condition itemCondition) {
        this.itemCondition = itemCondition;
    }

    public Set<Image> getImages() {
        return images;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public TradeEvent getTradeEvent() {
        return tradeEvent;
    }

    public void setTradeEvent(TradeEvent tradeEvent) {
        this.tradeEvent = tradeEvent;
    }

    public Set<ItemToken> getTokens() {
        return tokens;
    }

    public void setTokens(Set<ItemToken> tokens) {
        this.tokens = tokens;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Integer getAvailableTokens() {
        return availableTokens;
    }

    public void setAvailableTokens(Integer availableTokens) {
        this.availableTokens = availableTokens;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
