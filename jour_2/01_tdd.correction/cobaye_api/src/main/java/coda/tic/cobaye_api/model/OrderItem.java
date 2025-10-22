package coda.tic.cobaye_api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderItem {
    @JsonProperty("productId")
    private Integer id;
    private Integer quantity;

    public OrderItem() {
    }

    public OrderItem(Integer id, Integer quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
