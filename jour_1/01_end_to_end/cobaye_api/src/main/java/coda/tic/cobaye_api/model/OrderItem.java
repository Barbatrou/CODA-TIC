package coda.tic.cobaye_api.model;

public class OrderItem {
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
