package coda.tic.cobaye_api.controller;

import coda.tic.cobaye_api.model.Order;
import coda.tic.cobaye_api.repository.OrderRepository;
import coda.tic.cobaye_api.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final OrderService orderService;

    public OrderController(OrderRepository orderRepository, OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> getAllOrders(@RequestParam(name = "user_id", required = false) Integer userId, Principal principal) {
        if (userId != null) {
            return orderRepository.findByUserId(userId);
        }
        return orderRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Integer id, Principal principal) {
        return orderRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order, Principal principal) {
        OrderService.ValidationStatus validation = orderService.validateOrder(order.getProducts());

        if (validation != OrderService.ValidationStatus.VALID) {
            if (validation == OrderService.ValidationStatus.PRODUCT_NOT_FOUND) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        double total = orderService.calculateTotal(order.getProducts());
        order.setTotal(total);

        orderService.deductInventory(order.getProducts());

        Order savedOrder = orderRepository.save(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Integer id, @RequestBody Order order, Principal principal) {
        return orderRepository.findById(id)
                .map(existingOrder -> {
                    if (order.getUserId() != null) {
                        existingOrder.setUserId(order.getUserId());
                    }
                    if (order.getProducts() != null) {
                        existingOrder.setProducts(order.getProducts());
                    }
                    if (order.getTotal() != null) {
                        existingOrder.setTotal(order.getTotal());
                    }
                Order updatedOrder = orderRepository.save(existingOrder);
                    return ResponseEntity.ok(updatedOrder);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id, Principal principal) {
        if (!orderRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        orderRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
