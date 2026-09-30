package fi.metropolia.riikaka.webstore.controller;

import fi.metropolia.riikaka.webstore.entity.*;
import fi.metropolia.riikaka.webstore.repository.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final PickupOrderRepository pickupOrderRepository;
    private final DeliveryOrderRepository deliveryOrderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemsViewRepository orderItemsViewRepository;
    private final ProductRepository productRepository;

    public OrderController(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                           ProductRepository productRepository, OrderItemsViewRepository orderItemsViewRepository,
                           PickupOrderRepository pickupOrderRepository, DeliveryOrderRepository deliveryOrderRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.orderItemsViewRepository = orderItemsViewRepository;
        this.pickupOrderRepository = pickupOrderRepository;
        this.deliveryOrderRepository = deliveryOrderRepository;
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

    @GetMapping("/{id}/items")
    public ResponseEntity<List<OrderItemsView>> getOrderItems(@PathVariable Integer id) {
        if (orderRepository.existsById(id)) {
            List<OrderItemsView> orderItems = orderItemsViewRepository.findByOrderItemsViewIdOrderId(id);
            return ResponseEntity.ok(orderItems);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/items/add/{productId}")
    public ResponseEntity<OrderItemDTO> addProductToOrder(@PathVariable int id, @PathVariable int productId, @RequestBody OrderItem newOrderItem) {
        try {
            OrderItemId orderItemId = new OrderItemId(orderRepository.getReferenceById(id), productRepository.getReferenceById(productId));
            float unit_price = productRepository.getReferenceById(productId).getPrice();
            OrderItem orderItem = new OrderItem(orderItemId, newOrderItem.getQuantity(), unit_price);
            OrderItem saved = orderItemRepository.save(orderItem);

            OrderItemDTO response = new OrderItemDTO(id, productId, productRepository.getReferenceById(productId).getName(), saved.getQuantity(), saved.getUnit_price());
            return ResponseEntity.ok(response);
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

    @PostMapping("/pickup")
    public ResponseEntity<Order> postPickupOrder(@RequestBody PickUpOrder newOrder) {
        try{
            newOrder.setFinal_pickup_date(LocalDate.now().plusWeeks(2));
            Order saved = orderRepository.save(newOrder);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/delivery")
    public ResponseEntity<Order> postDeliveryOrder(@RequestBody DeliveryOrder newOrder) {
        try{
            Order saved = orderRepository.save(newOrder);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/pickup/{id}")
    public ResponseEntity<PickUpOrder> putPickupOrder(@PathVariable Integer id, @RequestBody PickUpOrder updatedOrder) {
        return pickupOrderRepository.findById(id).map(order -> {
            order.setCustomer(updatedOrder.getCustomer());
            order.setOrder_date(updatedOrder.getOrder_date());
            order.setStatus(updatedOrder.getStatus());
            order.setFinal_pickup_date(updatedOrder.getFinal_pickup_date());
            order.setPickup_location(updatedOrder.getPickup_location());
            return ResponseEntity.ok(orderRepository.save(order));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/delivery/{id}")
    public ResponseEntity<DeliveryOrder> putDeliveryOrder(@PathVariable Integer id, @RequestBody DeliveryOrder updatedOrder) {
        return deliveryOrderRepository.findById(id).map(order -> {
            order.setCustomer(updatedOrder.getCustomer());
            order.setOrder_date(updatedOrder.getOrder_date());
            order.setStatus(updatedOrder.getStatus());
            order.setShipping_address(updatedOrder.getShipping_address());
            order.setDelivery_date(updatedOrder.getDelivery_date());
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
