package com.triippztech.freshtrade.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Location.
 */
@Entity
@Table(name = "location")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "short_name", nullable = false)
    private String shortName;

    @Column(name = "address")
    private String address;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "lat_location")
    private Double latLocation;

    @Column(name = "long_location")
    private Double longLocation;

    @ManyToOne
    @JsonIgnoreProperties(value = { "locations" }, allowSetters = true)
    private Country country;

    @OneToMany(mappedBy = "location")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "images", "tokens", "owner", "location", "tradeEvent", "categories", "users" }, allowSetters = true)
    private Set<Item> items = new HashSet<>();

    @OneToMany(mappedBy = "location")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "location", "items", "reservations" }, allowSetters = true)
    private Set<TradeEvent> tradeEvents = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Location id(Long id) {
        this.id = id;
        return this;
    }

    public String getShortName() {
        return this.shortName;
    }

    public Location shortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getAddress() {
        return this.address;
    }

    public Location address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public Location postalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Double getLatLocation() {
        return this.latLocation;
    }

    public Location latLocation(Double latLocation) {
        this.latLocation = latLocation;
        return this;
    }

    public void setLatLocation(Double latLocation) {
        this.latLocation = latLocation;
    }

    public Double getLongLocation() {
        return this.longLocation;
    }

    public Location longLocation(Double longLocation) {
        this.longLocation = longLocation;
        return this;
    }

    public void setLongLocation(Double longLocation) {
        this.longLocation = longLocation;
    }

    public Country getCountry() {
        return this.country;
    }

    public Location country(Country country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Set<Item> getItems() {
        return this.items;
    }

    public Location items(Set<Item> items) {
        this.setItems(items);
        return this;
    }

    public Location addItems(Item item) {
        this.items.add(item);
        item.setLocation(this);
        return this;
    }

    public Location removeItems(Item item) {
        this.items.remove(item);
        item.setLocation(null);
        return this;
    }

    public void setItems(Set<Item> items) {
        if (this.items != null) {
            this.items.forEach(i -> i.setLocation(null));
        }
        if (items != null) {
            items.forEach(i -> i.setLocation(this));
        }
        this.items = items;
    }

    public Set<TradeEvent> getTradeEvents() {
        return this.tradeEvents;
    }

    public Location tradeEvents(Set<TradeEvent> tradeEvents) {
        this.setTradeEvents(tradeEvents);
        return this;
    }

    public Location addTradeEvents(TradeEvent tradeEvent) {
        this.tradeEvents.add(tradeEvent);
        tradeEvent.setLocation(this);
        return this;
    }

    public Location removeTradeEvents(TradeEvent tradeEvent) {
        this.tradeEvents.remove(tradeEvent);
        tradeEvent.setLocation(null);
        return this;
    }

    public void setTradeEvents(Set<TradeEvent> tradeEvents) {
        if (this.tradeEvents != null) {
            this.tradeEvents.forEach(i -> i.setLocation(null));
        }
        if (tradeEvents != null) {
            tradeEvents.forEach(i -> i.setLocation(this));
        }
        this.tradeEvents = tradeEvents;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Location)) {
            return false;
        }
        return id != null && id.equals(((Location) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Location{" +
            "id=" + getId() +
            ", shortName='" + getShortName() + "'" +
            ", address='" + getAddress() + "'" +
            ", postalCode='" + getPostalCode() + "'" +
            ", latLocation=" + getLatLocation() +
            ", longLocation=" + getLongLocation() +
            "}";
    }
}
