package fi.metropolia.riikaka.webstore.entity;

import java.util.List;

public class DeliveryOrderDTO {

    private Customer customer;
    private String street_address;
    private String postal_code;
    private String city;
    private String country;
    private List<ItemDTO> items;

    public DeliveryOrderDTO() {
    }

    public DeliveryOrderDTO(Customer customer, String street_address, String postal_code, String city, String country, List<ItemDTO> items) {
        this.customer = customer;
        this.street_address = street_address;
        this.postal_code = postal_code;
        this.city = city;
        this.country = country;
        this.items = items;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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

    public List<ItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }


}
