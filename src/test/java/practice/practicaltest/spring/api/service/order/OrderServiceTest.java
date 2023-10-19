package practice.practicaltest.spring.api.service.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
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
import practice.practicaltest.spring.domain.stock.Stock;
import practice.practicaltest.spring.domain.stock.StockRepository;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

    @Test
    @DisplayName("상품 번호 리스트를 받아 주문을 생성한다.")
    public void createOrder() throws Exception {
        // given
        List<Product> products = List.of(createProduct("001", HANDMADE, 1000),
                                         createProduct("002", HANDMADE, 2000),
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
                                         createProduct("002", HANDMADE, 2000),
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

    @Test
    @DisplayName("재고와 관련된 상품이 포함되어 있는 주문 번호를 받아 주문을 생성한다.")
    public void createOrder3() throws Exception {
        // given
        List<Product> products = List.of(createProduct("001", BAKERY, 1000),
                                         createProduct("002", BOTTLED, 2000),
                                         createProduct("003", HANDMADE, 3000),
                                         createProduct("004", BAKERY, 4000),
                                         createProduct("005", HANDMADE, 5000));
        productRepository.saveAll(products);

        List<Stock> stocks = List.of(Stock.create("001", 2),
                                     Stock.create("002", 2));
        stockRepository.saveAll(stocks);

        List<Product> orderProducts = List.of(products.get(0), products.get(0), products.get(1), products.get(2));
        OrderCreateRequest request = OrderCreateRequest.builder()
                                                       .orderDateTime(LocalDateTime.MAX)
                                                       .productNumbers(orderProducts.stream()
                                                                                    .map(Product::getProductNumber)
                                                                                    .toList())
                                                       .build();

        // when
        OrderCreateResponse response = orderService.createOrder(request);

        // then
        // order.products
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

        // stocks
        List<Stock> foundStocks = stockRepository.findAll();
        assertThat(foundStocks).hasSize(stocks.size())
                               .extracting("productNumber", "quantity")
                               .containsExactlyInAnyOrder(
                                       tuple("001", 0),
                                       tuple("002", 1)
                               );
    }

    @Test
    @DisplayName("재고가 부족한 상품으로 주문을 생성하려는 경우 예외가 발생한다.")
    public void createOrderWithNoStock() throws Exception {
        // given
        List<Product> products = List.of(createProduct("001", BAKERY, 1000),
                                         createProduct("002", BOTTLED, 2000),
                                         createProduct("003", HANDMADE, 3000),
                                         createProduct("004", BAKERY, 4000),
                                         createProduct("005", HANDMADE, 5000));
        productRepository.saveAll(products);

        List<Stock> stocks = List.of(Stock.create("001", 1),
                                     Stock.create("002", 2));
        stockRepository.saveAll(stocks);

        List<Product> orderProducts = List.of(products.get(0), products.get(0), products.get(1), products.get(2));
        OrderCreateRequest request = OrderCreateRequest.builder()
                                                       .orderDateTime(LocalDateTime.MAX)
                                                       .productNumbers(orderProducts.stream()
                                                                                    .map(Product::getProductNumber)
                                                                                    .toList())
                                                       .build();

        // when // then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.createOrder(request))
                                            .withMessage("재고가 부족한 상품이 있습니다.");
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