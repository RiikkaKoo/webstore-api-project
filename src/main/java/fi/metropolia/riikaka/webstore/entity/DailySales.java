package fi.metropolia.riikaka.webstore.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="daily_sales")
public class DailySales {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private LocalDate report_date;
    private int orders_total;
    private float sales_total;

    public DailySales() {
    }

    public DailySales(int id, LocalDate report_date, int orders_total, float sales_total) {
        this.id = id;
        this.report_date = report_date;
        this.orders_total = orders_total;
        this.sales_total = sales_total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getReport_date() {
        return report_date;
    }

    public void setReport_date(LocalDate report_date) {
        this.report_date = report_date;
    }

    public int getOrders_total() {
        return orders_total;
    }

    public void setOrders_total(int orders_total) {
        this.orders_total = orders_total;
    }

    public float getSales_total() {
        return sales_total;
    }

    public void setSales_total(float sales_total) {
        this.sales_total = sales_total;
    }
}
