package fi.metropolia.riikaka.webstore.repository;

import fi.metropolia.riikaka.webstore.entity.PickUpOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PickupOrderRepository extends JpaRepository<PickUpOrder, Integer> {
}
