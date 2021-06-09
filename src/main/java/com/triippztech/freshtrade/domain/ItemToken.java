package com.triippztech.freshtrade.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ItemToken.
 */
@Entity
@Table(name = "item_token")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ItemToken implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "token_name", nullable = false)
    private String tokenName;

    @NotNull
    @Column(name = "token_code", nullable = false, unique = true)
    private String tokenCode;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "updated_date")
    private ZonedDateTime updatedDate;

    //    @ManyToMany
    //    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    //    @JoinTable(
    //        name = "rel_item_token__owner",
    //        joinColumns = @JoinColumn(name = "item_token_id"),
    //        inverseJoinColumns = @JoinColumn(name = "owner_id")
    //    )
    //    private Set<User> owners = new HashSet<>();

    @ManyToOne
    private User owner;

    @ManyToOne
    @JsonIgnoreProperties(value = { "images", "tokens", "owner", "location", "tradeEvent", "categories", "users" }, allowSetters = true)
    private Item item;

    @ManyToOne
    @JsonIgnoreProperties(value = { "tokens" }, allowSetters = true)
    private Reservation reservation;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ItemToken id(Long id) {
        this.id = id;
        return this;
    }

    public String getTokenName() {
        return this.tokenName;
    }

    public ItemToken tokenName(String tokenName) {
        this.tokenName = tokenName;
        return this;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getTokenCode() {
        return this.tokenCode;
    }

    public ItemToken tokenCode(String tokenCode) {
        this.tokenCode = tokenCode;
        return this;
    }

    public void setTokenCode(String tokenCode) {
        this.tokenCode = tokenCode;
    }

    public ZonedDateTime getCreatedDate() {
        return this.createdDate;
    }

    public ItemToken createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getUpdatedDate() {
        return this.updatedDate;
    }

    public ItemToken updatedDate(ZonedDateTime updatedDate) {
        this.updatedDate = updatedDate;
        return this;
    }

    public void setUpdatedDate(ZonedDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Item getItem() {
        return this.item;
    }

    public ItemToken item(Item item) {
        this.setItem(item);
        return this;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public ItemToken owner(User owner) {
        this.owner = owner;
        return this;
    }

    public ItemToken reservation(Reservation reservation) {
        this.reservation = reservation;
        return this;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemToken)) {
            return false;
        }
        return id != null && id.equals(((ItemToken) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemToken{" +
            "id=" + getId() +
            ", tokenName='" + getTokenName() + "'" +
            ", tokenCode='" + getTokenCode() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            "}";
    }
}
