package com.triippztech.freshtrade.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Reservation.
 */
@Entity
@Table(name = "reservation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    @Size(max = 100)
    @Column(name = "reservation_number", length = 100, nullable = false, unique = true)
    private String reservationNumber;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "is_cancelled")
    private Boolean isCancelled;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "cancellation_note")
    private String cancellationNote;

    @Column(name = "pickup_time")
    private ZonedDateTime pickupTime;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "updated_date")
    private ZonedDateTime updatedDate;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "price_per")
    private Double pricePer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "tokens", "owner", "location", "tradeEvent", "categories", "users" }, allowSetters = true)
    private Item item;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "langKey", "activated", "email", "password", "firstName", "lastName", "activationKey", "resetKey", "resetDate", "authorities",
        },
        allowSetters = true
    )
    private User seller;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(
        value = {
            "langKey", "activated", "email", "password", "firstName", "lastName", "activationKey", "resetKey", "resetDate", "authorities",
        },
        allowSetters = true
    )
    private User buyer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "location", "items", "reservations" }, allowSetters = true)
    private TradeEvent event;

    @OneToMany(mappedBy = "reservation")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "reservation" }, allowSetters = true)
    private Set<ItemToken> tokens = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Reservation id(UUID id) {
        this.id = id;
        return this;
    }

    public String getReservationNumber() {
        return this.reservationNumber;
    }

    public Reservation reservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
        return this;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Reservation isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsCancelled() {
        return this.isCancelled;
    }

    public Reservation isCancelled(Boolean isCancelled) {
        this.isCancelled = isCancelled;
        return this;
    }

    public void setIsCancelled(Boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public String getCancellationNote() {
        return this.cancellationNote;
    }

    public Reservation cancellationNote(String cancellationNote) {
        this.cancellationNote = cancellationNote;
        return this;
    }

    public void setCancellationNote(String cancellationNote) {
        this.cancellationNote = cancellationNote;
    }

    public ZonedDateTime getPickupTime() {
        return this.pickupTime;
    }

    public Reservation pickupTime(ZonedDateTime pickupTime) {
        this.pickupTime = pickupTime;
        return this;
    }

    public void setPickupTime(ZonedDateTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public ZonedDateTime getCreatedDate() {
        return this.createdDate;
    }

    public Reservation createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getUpdatedDate() {
        return this.updatedDate;
    }

    public Reservation updatedDate(ZonedDateTime updatedDate) {
        this.updatedDate = updatedDate;
        return this;
    }

    public void setUpdatedDate(ZonedDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public User getSeller() {
        return this.seller;
    }

    public Reservation seller(User user) {
        this.setSeller(user);
        return this;
    }

    public void setSeller(User user) {
        this.seller = user;
    }

    public User getBuyer() {
        return this.buyer;
    }

    public Reservation buyer(User user) {
        this.setBuyer(user);
        return this;
    }

    public void setBuyer(User user) {
        this.buyer = user;
    }

    public TradeEvent getEvent() {
        return this.event;
    }

    public Reservation event(TradeEvent tradeEvent) {
        this.setEvent(tradeEvent);
        return this;
    }

    public void setEvent(TradeEvent tradeEvent) {
        this.event = tradeEvent;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public Set<ItemToken> getTokens() {
        return this.tokens;
    }

    public Reservation tokens(Set<ItemToken> tokens) {
        this.setTokens(tokens);
        return this;
    }

    public Reservation addToken(ItemToken token) {
        this.tokens.add(token);
        return this;
    }

    public Reservation removeToken(ItemToken token) {
        this.tokens.remove(token);
        return this;
    }

    public void setTokens(Set<ItemToken> tokens) {
        this.tokens = tokens;
    }

    public Reservation totalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Reservation pricePer(Double pricePer) {
        this.pricePer = pricePer;
        return this;
    }

    public Double getPricePer() {
        return pricePer;
    }

    public void setPricePer(Double pricePer) {
        this.pricePer = pricePer;
    }

    public Reservation item(Item item) {
        this.item = item;
        return this;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reservation)) {
            return false;
        }
        return id != null && id.equals(((Reservation) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            reservationNumber,
            isActive,
            isCancelled,
            cancellationNote,
            pickupTime,
            createdDate,
            updatedDate,
            totalPrice,
            pricePer,
            item,
            seller,
            buyer,
            event,
            tokens
        );
    }

    @Override
    public String toString() {
        return (
            "Reservation{" +
            "id=" +
            id +
            ", reservationNumber='" +
            reservationNumber +
            '\'' +
            ", isActive=" +
            isActive +
            ", isCancelled=" +
            isCancelled +
            ", cancellationNote='" +
            cancellationNote +
            '\'' +
            ", pickupTime=" +
            pickupTime +
            ", createdDate=" +
            createdDate +
            ", updatedDate=" +
            updatedDate +
            ", totalPrice=" +
            totalPrice +
            ", pricePer=" +
            pricePer +
            ", item=" +
            item +
            ", seller=" +
            seller +
            ", buyer=" +
            buyer +
            ", event=" +
            event +
            ", tokens=" +
            tokens +
            '}'
        );
    }
}
