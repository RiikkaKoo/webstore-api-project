package fi.metropolia.riikaka.webstore.service;

import fi.metropolia.riikaka.webstore.entity.DailySales;
import fi.metropolia.riikaka.webstore.entity.OrdersStatsView;
import fi.metropolia.riikaka.webstore.entity.StockSuppliersView;
import fi.metropolia.riikaka.webstore.repository.DailySalesRepository;
import fi.metropolia.riikaka.webstore.repository.OrdersStatsViewRepository;
import fi.metropolia.riikaka.webstore.repository.StockSuppliersViewRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {

    private final OrdersStatsViewRepository ordersStatsViewRepository;
    private final StockSuppliersViewRepository stockSuppliersViewRepository;
    private final DailySalesRepository dailySalesRepository;

    public ReportService(OrdersStatsViewRepository ordersStatsViewRepository, StockSuppliersViewRepository stockSuppliersViewRepository,
                            DailySalesRepository dailySalesRepository) {
        this.ordersStatsViewRepository = ordersStatsViewRepository;
        this.stockSuppliersViewRepository = stockSuppliersViewRepository;
        this.dailySalesRepository = dailySalesRepository;
    }

    public List<OrdersStatsView> findCountrySalesReport() {
        List<OrdersStatsView> report = ordersStatsViewRepository.findAll();
        if (!report.isEmpty()) {
            return report;
        } else {
            return null;
        }
    }

    public OrdersStatsView findCountrySalesReportByCountry(String country) {
        if (ordersStatsViewRepository.findById(country).isEmpty()) {
            return null;
        } else {
            return ordersStatsViewRepository.findById(country).get();
        }
    }

    public List<DailySales> findDailySalesReport() {
        List<DailySales> report = dailySalesRepository.findAll();
        if (!report.isEmpty()) {
            return report;
        } else {
            return null;
        }
    }

    public List<StockSuppliersView> findStockSuppliersReport() {
        List<StockSuppliersView> report = stockSuppliersViewRepository.findAll();
        if (!report.isEmpty()) {
            return report;
        } else {
            return null;
        }
    }

    public List<StockSuppliersView> findStockSuppliersBySupplierIdReport(int supplierId) {
        List<StockSuppliersView> report = stockSuppliersViewRepository.findAllBySupplierId(supplierId);
        if (!report.isEmpty()) {
            return report;
        } else {
            return null;
        }
    }

    public List<StockSuppliersView> findStockSuppliersBelowStockThresholdReport(int threshold) {
        List<StockSuppliersView> suppliersViews = stockSuppliersViewRepository.findItemsAndSuppliersForLowStock(threshold);
        if (!suppliersViews.isEmpty()) {
            return suppliersViews;
        } else {
            return null;
        }
    }
}
