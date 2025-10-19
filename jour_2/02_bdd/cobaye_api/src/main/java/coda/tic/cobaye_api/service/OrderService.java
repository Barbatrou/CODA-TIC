package coda.tic.cobaye_api.service;

import coda.tic.cobaye_api.model.OrderItem;
import coda.tic.cobaye_api.model.Product;
import coda.tic.cobaye_api.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final ProductRepository productRepository;

    public OrderService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public enum ValidationStatus {
        VALID,
        INVALID_REQUEST,
        PRODUCT_NOT_FOUND,
        INSUFFICIENT_STOCK
    }

    /**
     * Validates that all products in the order exist and have sufficient stock
     * @param orderItems List of items to validate
     * @return ValidationStatus enum value
     */
    public ValidationStatus validateOrder(List<OrderItem> orderItems) {
        if (orderItems == null || orderItems.isEmpty()) {
            return ValidationStatus.INVALID_REQUEST;
        }

        for (OrderItem item : orderItems) {
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                return ValidationStatus.INVALID_REQUEST;
            }

            Optional<Product> productOpt = productRepository.findById(item.getId());
            if (productOpt.isEmpty()) {
                return ValidationStatus.PRODUCT_NOT_FOUND;
            }

            Product product = productOpt.get();
            if (product.getQuantity() < item.getQuantity()) {
                return ValidationStatus.INSUFFICIENT_STOCK;
            }
        }

        return ValidationStatus.VALID;
    }

    /**
     * Calculates the total price for an order
     * @param orderItems List of items in the order
     * @return Total price rounded to 2 decimal places
     */
    public double calculateTotal(List<OrderItem> orderItems) {
        double total = 0.0;

        for (OrderItem item : orderItems) {
            Product product = productRepository.findById(item.getId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + item.getId()));

            total += product.getPrice() * item.getQuantity();
        }

        return roundToTwoDecimals(total);
    }

    /**
     * Deducts ordered quantities from product inventory
     * @param orderItems List of items to deduct
     */
    public void deductInventory(List<OrderItem> orderItems) {
        for (OrderItem item : orderItems) {
            Product product = productRepository.findById(item.getId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + item.getId()));

            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRepository.save(product);
        }
    }

    /**
     * Rounds a double value to 2 decimal places
     * @param value Value to round
     * @return Rounded value
     */
    public double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
