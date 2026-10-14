package fi.metropolia.riikaka.webstore.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_orders")
public class DeliveryOrder extends Order {

    @Column(insertable = false)
    private LocalDateTime delivery_date;
    private String street_address;
    private String postal_code;
    private String city;
    private String country;

    public DeliveryOrder() {
    }

    public DeliveryOrder(LocalDateTime delivery_date, String street_address, String postal_code, String city, String country) {
        this.delivery_date = delivery_date;
        this.street_address = street_address;
        this.postal_code = postal_code;
        this.city = city;
        this.country = country;
    }

    public LocalDateTime getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(LocalDateTime delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getStreet_address() {
        return street_address;
    }

    public void setStreet_address(String street_address) {
        this.street_address = street_address;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
