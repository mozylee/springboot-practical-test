package practice.practicaltest.spring.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static practice.practicaltest.spring.domain.order.OrderStatus.COMPLETED;
import static practice.practicaltest.spring.domain.order.OrderStatus.CREATED;
import static practice.practicaltest.spring.domain.order.OrderStatus.RECEIVED;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @Test
    @DisplayName("시작일부터 종료일까지 원하는 주문 상태에 대한 주문 목록을 조회한다.")
    void findOrderBy() throws Exception {
        // given
        int startDay = 10;
        int endDay = 15;
        List<Order> createdOrders = List.of(orderFixture(CREATED, dateTimeFixture(startDay)),
                                            orderFixture(CREATED, dateTimeFixture(startDay + 1)),
                                            orderFixture(CREATED, dateTimeFixture(endDay)),
                                            orderFixture(CREATED, dateTimeFixture(endDay + 1)));
        List<Order> receivedOrders = List.of(orderFixture(RECEIVED, dateTimeFixture(startDay)),
                                             orderFixture(RECEIVED, dateTimeFixture(startDay + 1)));
        List<Order> completedOrders = List.of(orderFixture(COMPLETED, dateTimeFixture(startDay)),
                                              orderFixture(COMPLETED, dateTimeFixture(startDay + 1)),
                                              orderFixture(COMPLETED, dateTimeFixture(endDay)));
        orderRepository.saveAll(Stream.of(createdOrders, receivedOrders, completedOrders)
                                      .flatMap(List::stream)
                                      .toList());

        LocalDateTime startDateTime = dateTimeFixture(startDay);
        LocalDateTime endDateTime = dateTimeFixture(endDay);
        OrderStatus orderStatus = CREATED;

        // when
        List<Order> orders = orderRepository.findAllBy(startDateTime, endDateTime, orderStatus);

        // then
        assertThat(orders).hasSize(2)
                          .extracting("orderStatus", "orderDateTime")
                          .containsExactlyInAnyOrderElementsOf(createdOrders.stream()
                                                                            .limit(2)
                                                                            .map(o -> tuple(o.getOrderStatus(), o.getOrderDateTime()))
                                                                            .toList());
    }

    private LocalDateTime dateTimeFixture(int day) {
        return LocalDateTime.of(2023, 10, day, 0, 0);
    }

    private Order orderFixture(OrderStatus orderStatus, LocalDateTime orderDateTime) {
        return Order.builder()
                    .orderStatus(orderStatus)
                    .orderDateTime(orderDateTime)
                    .build();
    }

}