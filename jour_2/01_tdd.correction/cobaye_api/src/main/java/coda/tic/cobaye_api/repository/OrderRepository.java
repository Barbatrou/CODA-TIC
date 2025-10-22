package coda.tic.cobaye_api.repository;

import coda.tic.cobaye_api.model.Order;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class OrderRepository {
    private final Map<Integer, Order> orders = new ConcurrentHashMap<>();

    public List<Order> findAll() {
        return new ArrayList<>(orders.values());
    }

    public Optional<Order> findById(Integer id) {
        return Optional.ofNullable(orders.get(id));
    }

    public List<Order> findByUserId(Integer userId) {
        return orders.values().stream()
                .filter(o -> o.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public Order save(Order order) {
        if (order.getId() == null) {
            order.setId(generateId());
        }
        orders.put(order.getId(), order);
        return order;
    }

    public void deleteById(Integer id) {
        orders.remove(id);
    }

    public boolean existsById(Integer id) {
        return orders.containsKey(id);
    }

    private Integer generateId() {
        return orders.keySet().stream()
                .max(Integer::compareTo)
                .map(id -> id + 1)
                .orElse(1);
    }
}
