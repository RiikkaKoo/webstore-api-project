package fi.metropolia.riikaka.webstore.controller;

import fi.metropolia.riikaka.webstore.entity.OrdersStatsView;
import fi.metropolia.riikaka.webstore.entity.StockSuppliersView;
import fi.metropolia.riikaka.webstore.repository.OrdersStatsViewRepository;
import fi.metropolia.riikaka.webstore.repository.StockSuppliersViewRepository;
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

    private final OrdersStatsViewRepository ordersStatsViewRepository;
    private final StockSuppliersViewRepository stockSuppliersViewRepository;

    public ReportController(OrdersStatsViewRepository ordersStatsViewRepository, StockSuppliersViewRepository stockSuppliersViewRepository) {
        this.ordersStatsViewRepository = ordersStatsViewRepository;
        this.stockSuppliersViewRepository = stockSuppliersViewRepository;
    }

    @GetMapping("/country/sales")
    public ResponseEntity<List<OrdersStatsView>> getCountrySalesReport() {
        List<OrdersStatsView> report = ordersStatsViewRepository.findAll();
        if (!report.isEmpty()) {
            return ResponseEntity.ok(report);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/country/{country}/sales")
    public ResponseEntity<OrdersStatsView> getCountrySalesReportByCountry(@PathVariable String country) {
        return ordersStatsViewRepository.findById(country)
                .map(report -> ResponseEntity.ok(report))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/stock/suppliers")
    public ResponseEntity<List<StockSuppliersView>> getStockSuppliersReport() {
        List<StockSuppliersView> report = stockSuppliersViewRepository.findAll();
        if (!report.isEmpty()) {
            return ResponseEntity.ok(report);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/stock/suppliers/{supplierId}")
    public ResponseEntity<List<StockSuppliersView>> getStockSuppliersBySupplierIdReport(@PathVariable Integer supplierId) {
        List<StockSuppliersView> report = stockSuppliersViewRepository.findAllBySupplierId(supplierId);
        if (!report.isEmpty()) {
            return ResponseEntity.ok(report);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/stock/{threshold}/suppliers")
    public ResponseEntity<List<StockSuppliersView>> getStockSuppliersBelowStockThresholdReport(@PathVariable Integer threshold) {
        List<StockSuppliersView> all = stockSuppliersViewRepository.findAll();
        List<StockSuppliersView> below = new ArrayList<StockSuppliersView>();
        for (StockSuppliersView line : all) {
            if (line.getStock_quantity() < threshold) {
                below.add(line);
            }
        }
        if (!below.isEmpty()) {
            return ResponseEntity.ok(below);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
