package fi.metropolia.riikaka.webstore.entity;

import jakarta.persistence.*;

@Entity
@Table(name="supplieraddresses")
public class SupplierAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    private Supplier supplier;
    private String street_address;
    private String postal_code;
    private String city;
    private String country;

    public SupplierAddress() {
    }

    public SupplierAddress(Supplier supplier, String street_address, String postal_code, String city, String country) {
        this.supplier = supplier;
        this.street_address = street_address;
        this.postal_code = postal_code;
        this.city = city;
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
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
