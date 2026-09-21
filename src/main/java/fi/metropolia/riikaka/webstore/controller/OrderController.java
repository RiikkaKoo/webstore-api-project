package fi.metropolia.riikaka.webstore.controller;

import fi.metropolia.riikaka.webstore.entity.Order;
import fi.metropolia.riikaka.webstore.entity.OrderItem;
import fi.metropolia.riikaka.webstore.entity.OrderItemId;
import fi.metropolia.riikaka.webstore.repository.OrderItemRepository;
import fi.metropolia.riikaka.webstore.repository.OrderRepository;
import fi.metropolia.riikaka.webstore.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public OrderController(OrderRepository orderRepository, OrderItemRepository orderItemRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getOrders() {
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Integer id) {
        return orderRepository.findById(id)
                .map(order -> ResponseEntity.ok(order))
                .orElse(ResponseEntity.notFound().build());
    }

    /* Tämä täytyy korjata (JPQL?)
    @GetMapping("/{id}/items")
    public ResponseEntity<List<OrderItem>> getOrderItems(@PathVariable Integer id) {
        if (!orderRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        List<OrderItem> items =
                orderItemRepository.findItemsByOrderId(id);

        return ResponseEntity.ok(items);
    }

     */

    @PostMapping("/{id}/items/add/{productId}")
    public ResponseEntity<OrderItem> addProductToOrder(@PathVariable Integer id, @PathVariable Integer productId, @RequestBody OrderItem newOrderItem) {
        try {
            OrderItemId orderItemId = new OrderItemId(orderRepository.getReferenceById(id), productRepository.getReferenceById(productId));
            float unit_price = productRepository.getReferenceById(productId).getPrice();
            OrderItem orderItem = new OrderItem(orderItemId, newOrderItem.getQuantity(), unit_price);
            OrderItem saved = orderItemRepository.save(orderItem);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}/items/remove/{productId}")
    public ResponseEntity<String> removeProductFromOrder(@PathVariable Integer id, @PathVariable Integer productId) {
        try {
            OrderItemId orderItemId = new OrderItemId(orderRepository.getReferenceById(id), productRepository.getReferenceById(productId));
            if (orderItemRepository.existsById(orderItemId)) {
                orderItemRepository.deleteById(orderItemId);
                return ResponseEntity.ok("Product with ID " + productId +" was successfully removed from order " + id);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Order item was not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not delete order item");
        }
    }

    @PostMapping
    public ResponseEntity<Order> postOrder(@RequestBody Order newOrder) {
        try {
            Order order = orderRepository.save(newOrder);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> putOrder(@PathVariable Integer id, @RequestBody Order updatedOrder) {
        return orderRepository.findById(id).map(order -> {
            order.setCustomer(updatedOrder.getCustomer());
            order.setOrder_date(updatedOrder.getOrder_date());
            order.setDelivery_date(updatedOrder.getDelivery_date());
            order.setShipping_address(updatedOrder.getShipping_address());
            order.setStatus(updatedOrder.getStatus());
            return ResponseEntity.ok(orderRepository.save(order));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Integer id) {
        try {
            if (orderRepository.existsById(id)) {
                orderRepository.deleteById(id);
                return ResponseEntity.ok("Order with ID " + id +" was successfully deleted");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Order with ID " + id + " was not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not delete order");
        }
    }

}
