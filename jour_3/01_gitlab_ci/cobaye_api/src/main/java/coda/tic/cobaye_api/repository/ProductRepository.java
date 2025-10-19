package coda.tic.cobaye_api.repository;

import coda.tic.cobaye_api.model.Product;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class ProductRepository {
    private final Map<Integer, Product> products = new ConcurrentHashMap<>();

    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    public Optional<Product> findById(Integer id) {
        return Optional.ofNullable(products.get(id));
    }

    public List<Product> findByCategory(String category) {
        return products.values().stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public Product save(Product product) {
        if (product.getId() == null) {
            product.setId(generateId());
        }
        products.put(product.getId(), product);
        return product;
    }

    public void deleteById(Integer id) {
        products.remove(id);
    }

    public boolean existsById(Integer id) {
        return products.containsKey(id);
    }

    private Integer generateId() {
        return products.keySet().stream()
                .max(Integer::compareTo)
                .map(id -> id + 1)
                .orElse(1);
    }
}
