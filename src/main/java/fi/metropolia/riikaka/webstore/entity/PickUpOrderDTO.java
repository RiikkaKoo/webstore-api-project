package fi.metropolia.riikaka.webstore.entity;

import java.time.LocalDate;
import java.util.List;

public class PickUpOrderDTO {

    private Customer customer;
    private LocalDate final_pickup_date;
    private String pickup_location;
    private List<ItemDTO> items;

    public PickUpOrderDTO() {
    }

    public PickUpOrderDTO(Customer customer, LocalDate final_pickup_date, String pickup_location, List<ItemDTO> items) {
        this.customer = customer;
        this.final_pickup_date = final_pickup_date;
        this.pickup_location = pickup_location;
        this.items = items;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<ItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemDTO> items) {
        this.items = items;
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
