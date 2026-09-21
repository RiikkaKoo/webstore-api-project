package fi.metropolia.riikaka.webstore.entity;

import jakarta.persistence.*;

@Entity
@Table(name="orderitems")
public class OrderItem {

    @EmbeddedId
    private OrderItemId orderItemId;

    private int quantity;
    private float unit_price;

    public OrderItem() {
    }

    public OrderItem(OrderItemId orderItemId, int quantity, float unit_price) {
        this.orderItemId = orderItemId;
        this.quantity = quantity;
        this.unit_price = unit_price;
    }

    public OrderItemId getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(OrderItemId orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(float unit_price) {
        this.unit_price = unit_price;
    }
}

