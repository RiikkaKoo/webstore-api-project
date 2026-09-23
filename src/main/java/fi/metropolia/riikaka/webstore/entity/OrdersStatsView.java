package fi.metropolia.riikaka.webstore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders_stats")
public class OrdersStatsView {

    @Id
    private String country;
    private int orders;
    private float sales;

    public OrdersStatsView() {
    }

    public OrdersStatsView(String country, int orders, float sales) {
        this.country = country;
        this.orders = orders;
        this.sales = sales;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getOrders() {
        return orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }

    public float getSales() {
        return sales;
    }

    public void setSales(float sales) {
        this.sales = sales;
    }
}
