package practice.practicaltest.spring.api.service.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static practice.practicaltest.spring.domain.product.ProductSellingType.SELLING;
import static practice.practicaltest.spring.domain.product.ProductType.BAKERY;
import static practice.practicaltest.spring.domain.product.ProductType.BOTTLED;
import static practice.practicaltest.spring.domain.product.ProductType.HANDMADE;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import practice.practicaltest.spring.api.service.order.dto.OrderCreateRequest;
import practice.practicaltest.spring.api.service.order.dto.OrderCreateResponse;
import practice.practicaltest.spring.domain.product.Product;
import practice.practicaltest.spring.domain.product.ProductRepository;
import practice.practicaltest.spring.domain.product.ProductType;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("상품 번호 리스트를 받아 주문을 생성한다.")
    public void createOrder() throws Exception {
        // given
        List<Product> products = List.of(createProduct("001", HANDMADE, 1000),
                                         createProduct("002", BOTTLED, 2000),
                                         createProduct("003", HANDMADE, 3000),
                                         createProduct("004", BAKERY, 4000),
                                         createProduct("005", HANDMADE, 5000));
        productRepository.saveAll(products);

        List<Product> orderProducts = products.stream()
                                              .limit(2)
                                              .toList();
        OrderCreateRequest request = OrderCreateRequest.builder()
                                                       .orderDateTime(LocalDateTime.MAX)
                                                       .productNumbers(orderProducts.stream()
                                                                                    .map(Product::getProductNumber)
                                                                                    .toList())
                                                       .build();

        // when
        OrderCreateResponse response = orderService.createOrder(request);

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getProducts()).hasSize(orderProducts.size())
                                          .extracting("productNumber", "price")
                                          .containsExactlyInAnyOrderElementsOf(orderProducts.stream()
                                                                                            .map(product -> tuple(product.getProductNumber(),
                                                                                                                  product.getPrice()))
                                                                                            .toList());
        assertThat(response.getTotalPrice()).isEqualTo(orderProducts.stream()
                                                                    .mapToInt(Product::getPrice)
                                                                    .sum());
    }

    @Test
    @DisplayName("중복이 존재하는 상품 번호 리스트를 받아 주문을 생성한다.")
    public void createOrder2() throws Exception {
        // given
        List<Product> products = List.of(createProduct("001", HANDMADE, 1000),
                                         createProduct("002", BOTTLED, 2000),
                                         createProduct("003", HANDMADE, 3000),
                                         createProduct("004", BAKERY, 4000),
                                         createProduct("005", HANDMADE, 5000));
        productRepository.saveAll(products);

        List<Product> orderProducts = List.of(products.get(0), products.get(0));
        OrderCreateRequest request = OrderCreateRequest.builder()
                                                       .orderDateTime(LocalDateTime.MAX)
                                                       .productNumbers(orderProducts.stream()
                                                                                    .map(Product::getProductNumber)
                                                                                    .toList())
                                                       .build();

        // when
        OrderCreateResponse response = orderService.createOrder(request);

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getProducts()).hasSize(orderProducts.size())
                                          .extracting("productNumber", "price")
                                          .containsExactlyInAnyOrderElementsOf(orderProducts.stream()
                                                                                            .map(product -> tuple(product.getProductNumber(),
                                                                                                                  product.getPrice()))
                                                                                            .toList());
        assertThat(response.getTotalPrice()).isEqualTo(orderProducts.stream()
                                                                    .mapToInt(Product::getPrice)
                                                                    .sum());
    }

    private static Product createProduct(String productNumber, ProductType productType, int price) {
        return Product.builder()
                      .productNumber(productNumber)
                      .productType(productType)
                      .productSellingType(SELLING)
                      .name("테스트")
                      .price(price)
                      .build();
    }

}