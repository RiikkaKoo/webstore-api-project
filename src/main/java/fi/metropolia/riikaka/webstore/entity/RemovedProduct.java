package fi.metropolia.riikaka.webstore.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="removed_products")
public class RemovedProduct {
    @Id
    private int id;

    private String name;
    private String description;
    private float price;
    @ManyToOne
    private Supplier supplier;
    private LocalDateTime removal_date;

    public RemovedProduct() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public LocalDateTime getRemoval_date() {
        return removal_date;
    }

    public void setRemoval_date(LocalDateTime removal_date) {
        this.removal_date = removal_date;
    }
}
