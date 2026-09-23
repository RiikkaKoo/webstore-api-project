package fi.metropolia.riikaka.webstore.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;

@Embeddable
public class OrderItemId implements Serializable {

    @ManyToOne(cascade = CascadeType.ALL)
    private Order order;

    @ManyToOne(cascade = {
            CascadeType.PERSIST,
            CascadeType.REFRESH,
            CascadeType.MERGE,
            CascadeType.DETACH
    })
    private Product product;

    public OrderItemId() {
    }

    public OrderItemId(Order order, Product product) {
        this.order = order;
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
