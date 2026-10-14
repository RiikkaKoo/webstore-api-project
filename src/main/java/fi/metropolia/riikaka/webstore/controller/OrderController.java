package fi.metropolia.riikaka.webstore.controller;

import fi.metropolia.riikaka.webstore.entity.*;
import fi.metropolia.riikaka.webstore.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getOrders() {
        List<Order> orders = orderService.findAllOrders();
        if (orders == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(orders);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Integer id) {
        Order order = orderService.findOrderById(id);
        if (order == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(order);
        }
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<List<OrderItemsView>> getOrderItems(@PathVariable Integer id) {
        List<OrderItemsView> orderItems = orderService.findOrderItems(id);
        if (orderItems == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(orderItems);
        }
    }

    @PostMapping("/{id}/items/add")
    public ResponseEntity<OrderItemDTO> postProductToOrder(@PathVariable int id, @RequestBody ItemDTO newOrderItem) {
        OrderItemDTO orderItem = orderService.addProductToOrder(id, newOrderItem);
        if (orderItem == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } else {
            return ResponseEntity.ok(orderItem);
        }
    }

    @DeleteMapping("/{id}/items/remove/{productId}")
    public ResponseEntity<String> deleteProductFromOrder(@PathVariable Integer id, @PathVariable Integer productId) {
        String message = orderService.removeProductFromOrder(id, productId);
        if (message != null) {
            if(message.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Order item was not found");
            } else {
                return ResponseEntity.ok(message);
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not delete order item");
        }
    }

    @PostMapping("/pickup")
    public ResponseEntity<PickUpOrderDTO> postPickupOrder(@RequestBody PickUpOrderDTO newOrder) {
        PickUpOrderDTO order = orderService.addPickupOrder(newOrder);
        if (order == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } else {
            return ResponseEntity.ok(order);
        }
    }

    @PostMapping("/delivery")
    public ResponseEntity<DeliveryOrderDTO> postDeliveryOrder(@RequestBody DeliveryOrderDTO newOrder) {
        DeliveryOrderDTO order = orderService.addDeliveryOrder(newOrder);
        if (order == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } else {
            return ResponseEntity.ok(order);
        }
    }

    @PutMapping("/pickup/{id}")
    public ResponseEntity<PickUpOrder> putPickupOrder(@PathVariable Integer id, @RequestBody PickUpOrder updatedOrder) {
        PickUpOrder pickUpOrder = orderService.updatePickupOrder(id, updatedOrder);
        if (pickUpOrder == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(pickUpOrder);
        }
    }

    @PutMapping("/delivery/{id}")
    public ResponseEntity<DeliveryOrder> putDeliveryOrder(@PathVariable Integer id, @RequestBody DeliveryOrder updatedOrder) {
        DeliveryOrder deliveryOrder = orderService.updateDeliveryOrder(id, updatedOrder);
        if (deliveryOrder == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(deliveryOrder);
        }
    }

    /*
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Integer id) {
        String message = orderService.removeOrder(id);
        if (message != null) {
            if (message.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Order with ID " + id + " was not found");
            } else {
                return ResponseEntity.ok(message);
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not delete order");
        }
    }
    */

}
