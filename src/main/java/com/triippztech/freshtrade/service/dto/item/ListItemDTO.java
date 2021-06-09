package com.triippztech.freshtrade.service.dto.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.triippztech.freshtrade.domain.*;
import com.triippztech.freshtrade.domain.enumeration.Condition;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ListItemDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private Double price;

    private Integer quantity;

    private String name;

    private Condition itemCondition;

    private Set<Image> images = new HashSet<>();

    private Boolean isActive;

    private User owner;

    @JsonIgnoreProperties(value = { "country", "items", "tradeEvents" }, allowSetters = true)
    private Location location;

    @JsonIgnoreProperties(value = { "location", "items", "reservations" }, allowSetters = true)
    private TradeEvent tradeEvent;

    public ListItemDTO() {}

    public ListItemDTO(
        UUID id,
        Double price,
        Integer quantity,
        String name,
        Condition itemCondition,
        Set<Image> images,
        User owner,
        Location location,
        TradeEvent tradeEvent
    ) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.name = name;
        this.itemCondition = itemCondition;
        this.images = images;
        this.owner = owner;
        this.location = location;
        this.tradeEvent = tradeEvent;
    }

    public ListItemDTO(Item item) {
        this.id = item.getId();
        this.price = item.getPrice();
        this.quantity = item.getQuantity();
        this.name = item.getName();
        this.itemCondition = item.getItemCondition();
        this.images = item.getImages();
        this.owner = item.getOwner();
        this.location = item.getLocation();
        this.tradeEvent = item.getTradeEvent();
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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return (
            "ListItemDTO{" +
            "id=" +
            id +
            ", price=" +
            price +
            ", quantity=" +
            quantity +
            ", name='" +
            name +
            '\'' +
            ", itemCondition=" +
            itemCondition +
            ", images=" +
            images +
            ", owner=" +
            owner +
            ", location=" +
            location +
            ", tradeEvent=" +
            tradeEvent +
            '}'
        );
    }
}
