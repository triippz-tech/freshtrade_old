package com.triippztech.freshtrade.service.dto.item;

import java.util.UUID;

public class ItemReservationDTO {

    private Boolean wasReserved;
    private String errorMessage;
    private Integer reservedAmount;
    private UUID reservationId;
    private String reservationNumber;

    public ItemReservationDTO() {}

    public ItemReservationDTO wasReserved(boolean wasReserved) {
        this.wasReserved = wasReserved;
        return this;
    }

    public ItemReservationDTO errorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public ItemReservationDTO reservedAmount(Integer reservedAmount) {
        this.reservedAmount = reservedAmount;
        return this;
    }

    public ItemReservationDTO reservationId(UUID reservationId) {
        this.reservationId = reservationId;
        return this;
    }

    public ItemReservationDTO reservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
        return this;
    }

    public Boolean getWasReserved() {
        return wasReserved;
    }

    public void setWasReserved(Boolean wasReserved) {
        this.wasReserved = wasReserved;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getReservedAmount() {
        return reservedAmount;
    }

    public void setReservedAmount(Integer reservedAmount) {
        this.reservedAmount = reservedAmount;
    }

    public UUID getReservationId() {
        return reservationId;
    }

    public void setReservationId(UUID reservationId) {
        this.reservationId = reservationId;
    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }
}
