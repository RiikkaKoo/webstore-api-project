package fi.metropolia.riikaka.webstore.repository;

import fi.metropolia.riikaka.webstore.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
