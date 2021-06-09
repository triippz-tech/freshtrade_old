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
 * A TradeEvent.
 */
@Entity
@Table(name = "trade_event")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TradeEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    @Column(name = "event_name", nullable = false)
    private String eventName;

    @NotNull
    @Column(name = "event_description", nullable = false)
    private String eventDescription;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private ZonedDateTime startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private ZonedDateTime endDate;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "country", "items", "tradeEvents" }, allowSetters = true)
    private Location location;

    @OneToMany(mappedBy = "tradeEvent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "images", "tokens", "owner", "location", "tradeEvent", "categories", "users" }, allowSetters = true)
    private Set<Item> items = new HashSet<>();

    @OneToMany(mappedBy = "event")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "seller", "buyer", "event" }, allowSetters = true)
    private Set<Reservation> reservations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TradeEvent id(UUID id) {
        this.id = id;
        return this;
    }

    public String getEventName() {
        return this.eventName;
    }

    public TradeEvent eventName(String eventName) {
        this.eventName = eventName;
        return this;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return this.eventDescription;
    }

    public TradeEvent eventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
        return this;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public ZonedDateTime getStartDate() {
        return this.startDate;
    }

    public TradeEvent startDate(ZonedDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return this.endDate;
    }

    public TradeEvent endDate(ZonedDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public TradeEvent isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Location getLocation() {
        return this.location;
    }

    public TradeEvent location(Location location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Set<Item> getItems() {
        return this.items;
    }

    public TradeEvent items(Set<Item> items) {
        this.setItems(items);
        return this;
    }

    public TradeEvent addItems(Item item) {
        this.items.add(item);
        item.setTradeEvent(this);
        return this;
    }

    public TradeEvent removeItems(Item item) {
        this.items.remove(item);
        item.setTradeEvent(null);
        return this;
    }

    public void setItems(Set<Item> items) {
        if (this.items != null) {
            this.items.forEach(i -> i.setTradeEvent(null));
        }
        if (items != null) {
            items.forEach(i -> i.setTradeEvent(this));
        }
        this.items = items;
    }

    public Set<Reservation> getReservations() {
        return this.reservations;
    }

    public TradeEvent reservations(Set<Reservation> reservations) {
        this.setReservations(reservations);
        return this;
    }

    public TradeEvent addReservations(Reservation reservation) {
        this.reservations.add(reservation);
        reservation.setEvent(this);
        return this;
    }

    public TradeEvent removeReservations(Reservation reservation) {
        this.reservations.remove(reservation);
        reservation.setEvent(null);
        return this;
    }

    public void setReservations(Set<Reservation> reservations) {
        if (this.reservations != null) {
            this.reservations.forEach(i -> i.setEvent(null));
        }
        if (reservations != null) {
            reservations.forEach(i -> i.setEvent(this));
        }
        this.reservations = reservations;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TradeEvent)) {
            return false;
        }
        return id != null && id.equals(((TradeEvent) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TradeEvent{" +
            "id=" + getId() +
            ", eventName='" + getEventName() + "'" +
            ", eventDescription='" + getEventDescription() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
