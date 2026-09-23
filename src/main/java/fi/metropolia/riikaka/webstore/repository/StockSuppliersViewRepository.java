package fi.metropolia.riikaka.webstore.repository;

import fi.metropolia.riikaka.webstore.entity.StockSuppliersView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockSuppliersViewRepository extends JpaRepository<StockSuppliersView, Integer> {
    List<StockSuppliersView> findAllBySupplierId(Integer supplierId);
}
