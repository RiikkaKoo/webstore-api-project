package fi.metropolia.riikaka.webstore.repository;

import fi.metropolia.riikaka.webstore.entity.DailySales;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailySalesRepository extends JpaRepository<DailySales, Integer> {
}
