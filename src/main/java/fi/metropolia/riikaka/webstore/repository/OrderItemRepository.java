package fi.metropolia.riikaka.webstore.repository;

import fi.metropolia.riikaka.webstore.entity.Order;
import fi.metropolia.riikaka.webstore.entity.OrderItem;
import fi.metropolia.riikaka.webstore.entity.OrderItemId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {
    List<OrderItem> findByOrderItemIdOrder(Order order);
    /*
    List<OrderItem> findItemsByOrderId(Integer orderId);

     */
}
