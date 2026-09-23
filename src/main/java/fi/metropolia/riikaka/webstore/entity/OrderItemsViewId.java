package fi.metropolia.riikaka.webstore.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class OrderItemsViewId {

    private int productId;
    private int orderId;

    public OrderItemsViewId() {
    }

    public OrderItemsViewId(int productId, int orderId) {
        this.productId = productId;
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
