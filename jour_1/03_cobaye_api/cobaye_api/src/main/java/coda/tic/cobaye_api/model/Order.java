package coda.tic.cobaye_api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Order {
    private Integer id;
    @JsonProperty("user_id")
    private Integer userId;
    private List<OrderItem> products;
    private Double total;

    public Order() {
    }

    public Order(Integer id, Integer userId, List<OrderItem> products, Double total) {
        this.id = id;
        this.userId = userId;
        this.products = products;
        this.total = total;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<OrderItem> getProducts() {
        return products;
    }

    public void setProducts(List<OrderItem> products) {
        this.products = products;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
