package fi.metropolia.riikaka.webstore.repository;

import fi.metropolia.riikaka.webstore.entity.OrderItemsView;
import fi.metropolia.riikaka.webstore.entity.OrderItemsViewId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemsViewRepository extends JpaRepository<OrderItemsView, OrderItemsViewId> {
    List<OrderItemsView> findByOrderItemsViewIdOrderId(Integer orderId);
}
