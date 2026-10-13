package fi.metropolia.riikaka.webstore.controller;

import fi.metropolia.riikaka.webstore.entity.DailySales;
import fi.metropolia.riikaka.webstore.entity.OrdersStatsView;
import fi.metropolia.riikaka.webstore.entity.StockSuppliersView;
import fi.metropolia.riikaka.webstore.repository.DailySalesRepository;
import fi.metropolia.riikaka.webstore.repository.OrdersStatsViewRepository;
import fi.metropolia.riikaka.webstore.repository.StockSuppliersViewRepository;
import fi.metropolia.riikaka.webstore.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/country/sales")
    public ResponseEntity<List<OrdersStatsView>> getCountrySalesReport() {
        List<OrdersStatsView> report = reportService.findCountrySalesReport();
        if (report != null) {
            return ResponseEntity.ok(report);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/country/{country}/sales")
    public ResponseEntity<OrdersStatsView> getCountrySalesReportByCountry(@PathVariable String country) {
        OrdersStatsView ordersStatsView = reportService.findCountrySalesReportByCountry(country);
        if (ordersStatsView == null) {
            return ResponseEntity.notFound().build();
        } else {
           return ResponseEntity.ok(ordersStatsView);
        }
    }

    @GetMapping("/dailysales")
    public ResponseEntity<List<DailySales>> getDailySalesReport() {
        List<DailySales> report = reportService.findDailySalesReport();
        if (report == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(report);
        }
    }

    @GetMapping("/stock/suppliers")
    public ResponseEntity<List<StockSuppliersView>> getStockSuppliersReport() {
        List<StockSuppliersView> report = reportService.findStockSuppliersReport();
        if (report == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(report);
        }
    }

    @GetMapping("/stock/suppliers/{supplierId}")
    public ResponseEntity<List<StockSuppliersView>> getStockSuppliersBySupplierIdReport(@PathVariable Integer supplierId) {
        List<StockSuppliersView> report = reportService.findStockSuppliersBySupplierIdReport(supplierId);
        if (report == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(report);
        }
    }

    @GetMapping("/stock/{threshold}/suppliers")
    public ResponseEntity<List<StockSuppliersView>> getStockSuppliersBelowStockThresholdReport(@PathVariable Integer threshold) {
        List<StockSuppliersView> all = reportService.findStockSuppliersBelowStockThresholdReport(threshold);
        if (all == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(all);
        }
    }
}
