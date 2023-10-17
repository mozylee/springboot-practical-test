package practice.practicaltest.spring.api.service.order;

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
import practice.practicaltest.spring.domain.orderproduct.OrderProduct;
import practice.practicaltest.spring.domain.product.Product;
import practice.practicaltest.spring.domain.product.ProductRepository;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final ProductRepository productRepository;

    private final OrderRepository orderRepository;

    @Transactional
    public OrderCreateResponse createOrder(OrderCreateRequest request) {
        List<Product> duplicatedProducts = findProductsBy(request);
        Order order = Order.create(duplicatedProducts,
                                   request.getOrderDateTime());

        return OrderCreateResponse.of(orderRepository.save(order));
    }

    private List<Product> findProductsBy(OrderCreateRequest request) {
        List<String> productNumbers = request.getProductNumbers();
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);

        // 중복 상품 처리
        Map<String, Product> productMap = products.stream()
                                                  .collect(Collectors.toMap(Product::getProductNumber, p -> p));

        List<Product> duplicatedProducts = productNumbers.stream()
                                                         .map(productMap::get)
                                                         .toList();

        return duplicatedProducts;
    }

}
