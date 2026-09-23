package fi.metropolia.riikaka.webstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "stock_suppliers")
public class StockSuppliersView {

    @Id
    @Column(name = "product_id")
    private int productId;
    private String product_name;
    private int stock_quantity;
    @Column(name = "supplier_id")
    private int supplierId;
    private String supplier;
    private String contact_name;
    private String phone;
    private String email;
    private String street_address;
    private String city;
    private String country;

    public StockSuppliersView() {
    }

    public StockSuppliersView(int product_id, String product_name, int stock_quantity, int supplier_id, String supplier,
                              String contact_name, String phone, String email, String street_address, String city, String country) {
        this.productId = product_id;
        this.product_name = product_name;
        this.stock_quantity = stock_quantity;
        this.supplierId = supplier_id;
        this.supplier = supplier;
        this.contact_name = contact_name;
        this.phone = phone;
        this.email = email;
        this.street_address = street_address;
        this.city = city;
        this.country = country;
    }

    public int getProduct_id() {
        return productId;
    }

    public void setProduct_id(int product_id) {
        this.productId = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getStock_quantity() {
        return stock_quantity;
    }

    public void setStock_quantity(int stock_quantity) {
        this.stock_quantity = stock_quantity;
    }

    public int getSupplier_id() {
        return supplierId;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplierId = supplier_id;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStreet_address() {
        return street_address;
    }

    public void setStreet_address(String street_address) {
        this.street_address = street_address;
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
