package com.triippztech.freshtrade.service.dto.metrics;

public class SellerKPIsDTO {

    private Double totalRevenue;
    private Double monthlyRevenue;
    private Integer pending7DReservations;
    private Integer totalActiveReservations;
    private Integer uniqueBuyers;
    private Integer returningBuyers;
    private Integer totalCancellations;
    private Integer totalCompletedReservations;

    public SellerKPIsDTO() {}

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public SellerKPIsDTO totalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
        return this;
    }

    public Double getMonthlyRevenue() {
        return monthlyRevenue;
    }

    public void setMonthlyRevenue(Double monthlyRevenue) {
        this.monthlyRevenue = monthlyRevenue;
    }

    public SellerKPIsDTO monthlyRevenue(Double monthlyRevenue) {
        this.monthlyRevenue = monthlyRevenue;
        return this;
    }

    public Integer getPending7DReservations() {
        return pending7DReservations;
    }

    public void setPending7DReservations(Integer pending7DReservations) {
        this.pending7DReservations = pending7DReservations;
    }

    public SellerKPIsDTO pending7DReservations(Integer pending7DReservations) {
        this.pending7DReservations = pending7DReservations;
        return this;
    }

    public Integer getTotalActiveReservations() {
        return totalActiveReservations;
    }

    public void setTotalActiveReservations(Integer totalActiveReservations) {
        this.totalActiveReservations = totalActiveReservations;
    }

    public SellerKPIsDTO totalActiveReservations(Integer totalActiveReservations) {
        this.totalActiveReservations = totalActiveReservations;
        return this;
    }

    public Integer getUniqueBuyers() {
        return uniqueBuyers;
    }

    public void setUniqueBuyers(Integer uniqueBuyers) {
        this.uniqueBuyers = uniqueBuyers;
    }

    public SellerKPIsDTO uniqueBuyers(Integer uniqueBuyers) {
        this.uniqueBuyers = uniqueBuyers;
        return this;
    }

    public Integer getReturningBuyers() {
        return returningBuyers;
    }

    public void setReturningBuyers(Integer returningBuyers) {
        this.returningBuyers = returningBuyers;
    }

    public SellerKPIsDTO returningBuyers(Integer returningBuyers) {
        this.returningBuyers = returningBuyers;
        return this;
    }

    public Integer getTotalCancellations() {
        return totalCancellations;
    }

    public void setTotalCancellations(Integer totalCancellations) {
        this.totalCancellations = totalCancellations;
    }

    public SellerKPIsDTO totalCancellations(Integer totalCancellations) {
        this.totalCancellations = totalCancellations;
        return this;
    }

    public Integer getTotalCompletedReservations() {
        return totalCompletedReservations;
    }

    public void setTotalCompletedReservations(Integer totalCompletedReservations) {
        this.totalCompletedReservations = totalCompletedReservations;
    }

    public SellerKPIsDTO totalCompletedReservations(Integer totalCompletedReservations) {
        this.totalCompletedReservations = totalCompletedReservations;
        return this;
    }

    @Override
    public String toString() {
        return (
            "SellerKPIsDTO{" +
            "totalRevenue=" +
            totalRevenue +
            ", monthlyRevenue=" +
            monthlyRevenue +
            ", pending7DReservations=" +
            pending7DReservations +
            ", totalActiveReservations=" +
            totalActiveReservations +
            ", uniqueBuyers=" +
            uniqueBuyers +
            ", returningBuyers=" +
            returningBuyers +
            ", totalCancellations=" +
            totalCancellations +
            ", totalCompletedReservations=" +
            totalCompletedReservations +
            '}'
        );
    }
}
