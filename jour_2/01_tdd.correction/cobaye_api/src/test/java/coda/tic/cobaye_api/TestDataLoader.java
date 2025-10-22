package coda.tic.cobaye_api;

import coda.tic.cobaye_api.model.Order;
import coda.tic.cobaye_api.model.Product;
import coda.tic.cobaye_api.model.User;
import coda.tic.cobaye_api.repository.OrderRepository;
import coda.tic.cobaye_api.repository.ProductRepository;
import coda.tic.cobaye_api.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class TestDataLoader {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    public TestDataLoader(UserRepository userRepository,
                          ProductRepository productRepository,
                          OrderRepository orderRepository,
                          ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.objectMapper = objectMapper;
    }

    public void resetData() throws IOException {
        // Clear all repositories
        clearAllData();

        // Load fresh data from data.json
        loadData();
    }

    private void clearAllData() {
        userRepository.findAll().forEach(user -> userRepository.deleteById(user.getId()));
        productRepository.findAll().forEach(product -> productRepository.deleteById(product.getId()));
        orderRepository.findAll().forEach(order -> orderRepository.deleteById(order.getId()));
    }

    private void loadData() throws IOException {
        File dataFile = new File("/var/data.json");

        if (!dataFile.exists()) {
            System.out.println("Warning: /var/data.json not found for test data loading");
            return;
        }

        JsonNode rootNode = objectMapper.readTree(dataFile);

        // Load users
        JsonNode usersNode = rootNode.get("users");
        if (usersNode != null && usersNode.isArray()) {
            for (JsonNode userNode : usersNode) {
                User user = objectMapper.treeToValue(userNode, User.class);
                userRepository.save(user);
            }
        }

        // Load products
        JsonNode productsNode = rootNode.get("products");
        if (productsNode != null && productsNode.isArray()) {
            for (JsonNode productNode : productsNode) {
                Product product = objectMapper.treeToValue(productNode, Product.class);
                productRepository.save(product);
            }
        }

        // Load orders
        JsonNode ordersNode = rootNode.get("orders");
        if (ordersNode != null && ordersNode.isArray()) {
            for (JsonNode orderNode : ordersNode) {
                Order order = objectMapper.treeToValue(orderNode, Order.class);
                orderRepository.save(order);
            }
        }
    }
}
