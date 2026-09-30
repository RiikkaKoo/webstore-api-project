package fi.metropolia.riikaka.webstore.repository;

import fi.metropolia.riikaka.webstore.entity.DeliveryOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryOrderRepository extends JpaRepository<DeliveryOrder, Integer> {
}
