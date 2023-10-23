package practice.practicaltest.spring.api.service.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static practice.practicaltest.spring.domain.product.ProductSellingType.SELLING;
import static practice.practicaltest.spring.domain.product.ProductType.HANDMADE;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import practice.practicaltest.spring.domain.order.Order;
import practice.practicaltest.spring.domain.order.OrderRepository;
import practice.practicaltest.spring.domain.order.OrderStatus;
import practice.practicaltest.spring.domain.product.Product;
import practice.practicaltest.spring.domain.product.ProductRepository;
import practice.practicaltest.spring.domain.product.ProductType;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class OrderStatisticsServiceTest {

    @Autowired
    private OrderStatisticsService orderStatisticsService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("완료된 주문들을 조회하여 매출 통계 메일을 발송한다.")
    void getStatistics() throws Exception {
        // given
        List<Product> products = List.of(productFixture("001", 1000),
                                         productFixture("002", 2000),
                                         productFixture("003", 3000));
        productRepository.saveAll(products);

        LocalDateTime now = dateTimeFixture(10);
        List<Order> orders = orderRepository.saveAll(List.of(orderFixture(products, now.minusDays(1)),
                                                             orderFixture(products, now),
                                                             orderFixture(products, now),
                                                             orderFixture(products, now.plusDays(1))));

        // when
        boolean isProcessed = orderStatisticsService.sendOrderStatisticsMail(dateTimeFixture(10).toLocalDate(),
                                                                             "test@test.com");

        // then
        assertThat(isProcessed).isTrue();
    }

    private static Product productFixture(String productNumber, int price) {
        return Product.builder()
                      .productNumber(productNumber)
                      .productType(HANDMADE)
                      .productSellingType(SELLING)
                      .name("테스트")
                      .price(price)
                      .build();
    }

    private LocalDateTime dateTimeFixture(int day) {
        return LocalDateTime.of(2023, 10, day, 0, 0);
    }

    private Order orderFixture(List<Product> products, LocalDateTime orderDateTime) {
        Order order = Order.create(products, orderDateTime);
        order.updateOrderStatus(OrderStatus.COMPLETED);

        return order;
    }

}