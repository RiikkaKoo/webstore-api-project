package fi.metropolia.riikaka.webstore.repository;

import fi.metropolia.riikaka.webstore.entity.StockSuppliersView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockSuppliersViewRepository extends JpaRepository<StockSuppliersView, Integer> {
    List<StockSuppliersView> findAllBySupplierId(Integer supplierId);

    @Query("SELECT sp FROM StockSuppliersView sp WHERE sp.stock_quantity <= :threshold")
    List<StockSuppliersView> findItemsAndSuppliersForLowStock(@Param("threshold") int threshold);
}
