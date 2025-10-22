package coda.tic.cobaye_api.service;

import coda.tic.cobaye_api.model.Product;
import coda.tic.cobaye_api.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public enum ValidationStatus {
        VALID,
        INVALID_PERCENTAGE,
        CATEGORY_NOT_FOUND
    }

    /**
     * Validates price adjustment parameters
     * @param category Category to adjust
     * @param percentage Percentage to apply
     * @return ValidationStatus enum value
     */
    public ValidationStatus validatePriceAdjustment(String category, Double percentage) {
        if (percentage == null || percentage <= -100.0) {
            return ValidationStatus.INVALID_PERCENTAGE;
        }

        List<Product> products = productRepository.findByCategory(category);
        if (products.isEmpty()) {
            return ValidationStatus.CATEGORY_NOT_FOUND;
        }

        return ValidationStatus.VALID;
    }

    /**
     * Adjusts prices for all products in a category by a given percentage
     * @param category Category to adjust
     * @param percentage Percentage to apply (positive for increase, negative for decrease)
     * @return List of updated products
     */
    public List<Product> adjustCategoryPrices(String category, Double percentage) {
        List<Product> products = productRepository.findByCategory(category);

        for (Product product : products) {
            double newPrice = calculateNewPrice(product.getPrice(), percentage);
            product.setPrice(newPrice);
            productRepository.save(product);
        }

        return products;
    }

    /**
     * Calculates new price based on percentage adjustment
     * @param currentPrice Current price
     * @param percentage Percentage to apply
     * @return New price rounded to 2 decimal places
     */
    public double calculateNewPrice(double currentPrice, double percentage) {
        double multiplier = 1.0 + (percentage / 100.0);
        double newPrice = currentPrice * multiplier;
        return roundToTwoDecimals(newPrice);
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
