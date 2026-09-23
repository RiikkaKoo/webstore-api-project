package fi.metropolia.riikaka.webstore.repository;

import fi.metropolia.riikaka.webstore.entity.OrdersStatsView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersStatsViewRepository extends JpaRepository<OrdersStatsView, String> {
}
