package fi.metropolia.riikaka.webstore.service;

import fi.metropolia.riikaka.webstore.entity.*;
import fi.metropolia.riikaka.webstore.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final PickupOrderRepository pickupOrderRepository;
    private final DeliveryOrderRepository deliveryOrderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemsViewRepository orderItemsViewRepository;
    private final ProductRepository productRepository;

    public OrderService (OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                         ProductRepository productRepository, OrderItemsViewRepository orderItemsViewRepository,
                         PickupOrderRepository pickupOrderRepository, DeliveryOrderRepository deliveryOrderRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.orderItemsViewRepository = orderItemsViewRepository;
        this.pickupOrderRepository = pickupOrderRepository;
        this.deliveryOrderRepository = deliveryOrderRepository;
    }

    public List<Order> findAllOrders() {
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()) {
            return null;
        } else {
            return orders;
        }
    }

    public Order findOrderById(int id) {
        if (orderRepository.findById(id).isEmpty()) {
            return null;
        } else {
            return orderRepository.findById(id).get();
        }
    }

    public List<OrderItemsView> findOrderItems(int id) {
        if (orderRepository.existsById(id)) {
            List<OrderItemsView> orderItems = orderItemsViewRepository.findByOrderItemsViewIdOrderId(id);
            if (orderItems.isEmpty()) {
                return null;
            } else {
                return orderItems;
            }
        } else {
            return null;
        }
    }

    @Transactional
    public OrderItemDTO addProductToOrder(int id, ItemDTO newOrderItem) {
        try {
            // Decrease stock
            Product product = productRepository.findById(newOrderItem.getProduct_id());
            if (product == null) {
                throw new EntityNotFoundException(
                        "Product with ID " + newOrderItem.getProduct_id() + " was not found"
                );
            }
            if (newOrderItem.getQuantity() <= 0 || newOrderItem.getQuantity() > 100) {
                throw new IllegalArgumentException(
                        "Invalid quantity was given"
                );
            }
            int new_stock_quantity = product.getStock_quantity() - newOrderItem.getQuantity();
            if (new_stock_quantity < 0) {
                throw new IllegalArgumentException(
                        "Not enough stock"
                );
            }
            product.setStock_quantity(new_stock_quantity);
            Product updatedProduct = productRepository.save(product);

            // Save item to order
            OrderItemId orderItemId = new OrderItemId(orderRepository.getReferenceById(id), updatedProduct);
            OrderItem orderItem = new OrderItem(orderItemId, newOrderItem.getQuantity(), product.getPrice());
            OrderItem saved = orderItemRepository.save(orderItem);

            OrderItemDTO response = new OrderItemDTO(id, newOrderItem.getProduct_id(), productRepository.findById(newOrderItem.getProduct_id()).getName(), saved.getQuantity(), saved.getUnit_price());
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw (e);
        }
    }

    @Transactional
    public String removeProductFromOrder(int id, int productId) {
        try {
            OrderItemId orderItemId = new OrderItemId(orderRepository.getReferenceById(id), productRepository.findById(productId));
            if (orderItemRepository.existsById(orderItemId)) {

                // Increase stock
                OrderItem orderItem = orderItemRepository.findByOrderItemId(orderItemId);
                Product product = productRepository.findById(productId);
                product.setStock_quantity(product.getStock_quantity() + orderItem.getQuantity());
                productRepository.save(product);

                orderItemRepository.deleteById(orderItemId);
                return "Product with ID " + productId +" was successfully removed from order with ID " + id;
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw (e);
        }
    }

    @Transactional
    public PickUpOrderDTO addPickupOrder(PickUpOrderDTO newOrder) {
        try {
            PickUpOrder order = new PickUpOrder();
            order.setCustomer(newOrder.getCustomer());
            order.setFinal_pickup_date(LocalDate.now().plusWeeks(2));
            Order addedOrder = orderRepository.save(order);

            for (ItemDTO i : newOrder.getItems()) {
                // Decrease stock
                Product product = productRepository.findById(i.getProduct_id());
                if (product == null) {
                    throw new EntityNotFoundException(
                            "Product with ID " + i.getProduct_id() + " was not found"
                    );
                }
                if (i.getQuantity() <= 0 || i.getQuantity() > 100) {
                    throw new IllegalArgumentException(
                            "Invalid quantity was given"
                    );
                }
                int new_stock_quantity = product.getStock_quantity() - i.getQuantity();
                if (new_stock_quantity < 0) {
                    throw new IllegalArgumentException(
                            "Not enough stock"
                    );
                }
                product.setStock_quantity(new_stock_quantity);
                Product updatedProduct = productRepository.save(product);

                // Save item to order
                OrderItemId orderItemId = new OrderItemId(orderRepository.getReferenceById(addedOrder.getId()), updatedProduct);
                OrderItem orderItem = new OrderItem(orderItemId, i.getQuantity(), product.getPrice());
                orderItemRepository.save(orderItem);
            }

            newOrder.setFinal_pickup_date(order.getFinal_pickup_date());
            newOrder.setCustomer(order.getCustomer());
            orderRepository.save(addedOrder);
            return newOrder;
        } catch (Exception e) {
            e.printStackTrace();
            throw (e);
        }
    }

    @Transactional
    public DeliveryOrderDTO addDeliveryOrder(DeliveryOrderDTO newOrder) {
        try {
            DeliveryOrder order = new DeliveryOrder(null, newOrder.getStreet_address(), newOrder.getPostal_code(), newOrder.getCity(), newOrder.getCountry());
            order.setCustomer(newOrder.getCustomer());
            Order addedOrder = orderRepository.save(order);

            for (ItemDTO i : newOrder.getItems()) {
                // Decrease stock
                Product product = productRepository.findById(i.getProduct_id());
                if (product == null) {
                    throw new EntityNotFoundException(
                            "Product with ID " + i.getProduct_id() + " was not found"
                    );
                }
                if (i.getQuantity() <= 0 || i.getQuantity() > 100) {
                    throw new IllegalArgumentException(
                            "Invalid quantity was given"
                    );
                }
                int new_stock_quantity = product.getStock_quantity() - i.getQuantity();
                if (new_stock_quantity < 0) {
                    throw new IllegalArgumentException(
                            "Not enough stock"
                    );
                }
                product.setStock_quantity(new_stock_quantity);
                Product updatedProduct = productRepository.save(product);

                // Save item to order
                OrderItemId orderItemId = new OrderItemId(orderRepository.getReferenceById(addedOrder.getId()), updatedProduct);
                OrderItem orderItem = new OrderItem(orderItemId, i.getQuantity(), product.getPrice());
                orderItemRepository.save(orderItem);
            }

            orderRepository.save(addedOrder);
            return newOrder;
        } catch (Exception e) {
            e.printStackTrace();
            throw (e);
        }
    }

    @Transactional
    public PickUpOrder updatePickupOrder(int id, PickUpOrder updatedOrder) {
        PickUpOrder pickUpOrder = pickupOrderRepository.getReferenceById(id);
        pickUpOrder.setStatus(updatedOrder.getStatus() != null ? updatedOrder.getStatus() : pickUpOrder.getStatus());
        pickUpOrder.setFinal_pickup_date(updatedOrder.getFinal_pickup_date() != null ? updatedOrder.getFinal_pickup_date() : pickUpOrder.getFinal_pickup_date());
        pickUpOrder.setPickup_location(updatedOrder.getPickup_location() != null ? updatedOrder.getPickup_location() : pickUpOrder.getPickup_location());
        try {
            return orderRepository.save(pickUpOrder);
        } catch (Exception e) {
            e.printStackTrace();
            throw (e);
        }
    }

    @Transactional
    public DeliveryOrder updateDeliveryOrder(int id, DeliveryOrder updatedOrder) {
        DeliveryOrder deliveryOrder = deliveryOrderRepository.getReferenceById(id);
        deliveryOrder.setStatus(updatedOrder.getStatus() != null ? updatedOrder.getStatus() : deliveryOrder.getStatus());
        deliveryOrder.setDelivery_date(updatedOrder.getDelivery_date() != null ? updatedOrder.getDelivery_date() : deliveryOrder.getDelivery_date());
        deliveryOrder.setStreet_address(updatedOrder.getStreet_address() != null ? updatedOrder.getStreet_address() : deliveryOrder.getStreet_address());
        deliveryOrder.setCity(updatedOrder.getCity() != null ? updatedOrder.getCity() : deliveryOrder.getCity());
        deliveryOrder.setPostal_code(updatedOrder.getPostal_code() != null ? updatedOrder.getPostal_code() : deliveryOrder.getPostal_code());
        deliveryOrder.setCountry(updatedOrder.getCountry() != null ? updatedOrder.getCountry() : deliveryOrder.getCountry());
        try {
            return orderRepository.save(deliveryOrder);
        } catch (Exception e) {
            e.printStackTrace();
            throw (e);
        }
    }

    @Transactional
    public String removeOrder(int id) {
        try {
            if (orderRepository.existsById(id)) {
                orderRepository.deleteById(id);
                return "Order with ID " + id +" was successfully deleted";
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw (e);
        }
    }
}
