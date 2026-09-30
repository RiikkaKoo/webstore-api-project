package fi.metropolia.riikaka.webstore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "pickup_orders")
public class PickUpOrder extends Order {

    private LocalDate final_pickup_date;
    private String pickup_location;

    public PickUpOrder() {
    }

    public LocalDate getFinal_pickup_date() {
        return final_pickup_date;
    }

    public void setFinal_pickup_date(LocalDate final_pickup_date) {
        this.final_pickup_date = final_pickup_date;
    }

    public String getPickup_location() {
        return pickup_location;
    }

    public void setPickup_location(String pickup_location) {
        this.pickup_location = pickup_location;
    }
}
