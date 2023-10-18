package practice.practicaltest.spring.api.service.order;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.practicaltest.spring.api.service.order.dto.OrderCreateRequest;
import practice.practicaltest.spring.api.service.order.dto.OrderCreateResponse;
import practice.practicaltest.spring.domain.order.Order;
import practice.practicaltest.spring.domain.order.OrderRepository;
import practice.practicaltest.spring.domain.product.Product;
import practice.practicaltest.spring.domain.product.ProductRepository;
import practice.practicaltest.spring.domain.product.ProductType;
import practice.practicaltest.spring.domain.stock.Stock;
import practice.practicaltest.spring.domain.stock.StockRepository;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final ProductRepository productRepository;

    private final OrderRepository orderRepository;

    private final StockRepository stockRepository;

    @Transactional
    public OrderCreateResponse createOrder(OrderCreateRequest request) {
        List<Product> products = findProductsBy(request.getProductNumbers());
        deductStockQuantities(products);

        Order order = Order.create(products, request.getOrderDateTime());

        return OrderCreateResponse.of(orderRepository.save(order));
    }

    private List<Product> findProductsBy(List<String> productNumbers) {
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);

        // 중복 상품 처리
        Map<String, Product> productMap = products.stream()
                                                  .collect(Collectors.toMap(Product::getProductNumber, p -> p));

        List<Product> duplicatedProducts = productNumbers.stream()
                                                         .map(productMap::get)
                                                         .toList();

        return duplicatedProducts;
    }

    private void deductStockQuantities(List<Product> products) {
        List<String> stockProductNumbers = extractStockProductNumbers(products);

        List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProductNumbers);

        Map<String, Stock> productStockMap = createProductStockMapBy(stocks);
        Map<String, Long> requiredQuantityMap = createRequiredQuantityMapBy(stockProductNumbers);

        for (String stockProductNumber : new HashSet<>(stockProductNumbers)) {
            Stock stock = productStockMap.get(stockProductNumber);
            int requiredQuantity = requiredQuantityMap.get(stockProductNumber).intValue();

            if (stock.isQuantityLessThan(requiredQuantity)) {
                throw new IllegalArgumentException("재고가 부족한 상품이 있습니다.");
            }

            stock.deductQuantity(requiredQuantity);
        }
    }

    private static List<String> extractStockProductNumbers(List<Product> products) {
        List<String> stockProductNumbers = products.stream()
                                                   .filter(product -> ProductType.containsStockType(product.getProductType()))
                                                   .map(Product::getProductNumber)
                                                   .toList();
        return stockProductNumbers;
    }

    private static Map<String, Stock> createProductStockMapBy(List<Stock> stocks) {
        return stocks.stream()
                     .collect(Collectors.toMap(Stock::getProductNumber, p -> p));
    }

    private static Map<String, Long> createRequiredQuantityMapBy(List<String> stockProductNumbers) {
        return stockProductNumbers.stream()
                                  .collect(Collectors.groupingBy(p -> p, Collectors.counting()));
    }

}
