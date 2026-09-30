package fi.metropolia.riikaka.webstore.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_orders")
public class DeliveryOrder extends Order {

    @Column(insertable = false)
    private LocalDateTime delivery_date;
    @ManyToOne(fetch= FetchType.LAZY, optional = false)
    private CustomerAddress shipping_address;

    public DeliveryOrder() {
    }

    public DeliveryOrder(LocalDateTime delivery_date, CustomerAddress shipping_address) {
        this.delivery_date = delivery_date;
        this.shipping_address = shipping_address;
    }

    public LocalDateTime getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(LocalDateTime delivery_date) {
        this.delivery_date = delivery_date;
    }

    public CustomerAddress getShipping_address() {
        return shipping_address;
    }

    public void setShipping_address(CustomerAddress shipping_address) {
        this.shipping_address = shipping_address;
    }
}
