package com.triippztech.freshtrade.service.dto.metrics;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.triippztech.freshtrade.domain.Item;

public class TopSellingItemsDTO {

    @JsonIgnoreProperties(
        value = {
            "owner",
            "location",
            "tradeEvent",
            "categories",
            "users",
            "createdDate",
            "updatedDate",
            "isActive",
            "itemCondition",
            "quantity",
            "price",
        },
        allowSetters = true
    )
    private Item item;

    private Long totalSold;

    public TopSellingItemsDTO() {}

    public TopSellingItemsDTO(Item item, Long totalSold) {
        this.item = item;
        this.totalSold = totalSold;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Long getTotalSold() {
        return totalSold;
    }

    public void setTotalSold(Long totalSold) {
        this.totalSold = totalSold;
    }
}
