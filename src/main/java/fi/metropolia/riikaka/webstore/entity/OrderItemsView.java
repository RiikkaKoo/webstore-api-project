package fi.metropolia.riikaka.webstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_items")
public class OrderItemsView {

    @Column(name = "customer_id")
    private int customerId;
    @Id
    private OrderItemsViewId orderItemsViewId;
    private String product_name;
    private int quantity;
    private float unit_price;
    private float total;

    public OrderItemsView() {
    }

    public OrderItemsView(OrderItemsViewId orderItemsViewId, int customerId, String product_name, int quantity, float unit_price, float total) {
        this.orderItemsViewId = orderItemsViewId;
        this.customerId = customerId;
        this.product_name = product_name;
        this.quantity = quantity;
        this.unit_price = unit_price;
        this.total = total;
    }

    public OrderItemsViewId getOrderItemsViewId() {
        return orderItemsViewId;
    }

    public void setOrderItemsViewId(OrderItemsViewId orderItemsViewId) {
        this.orderItemsViewId = orderItemsViewId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
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

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
}
