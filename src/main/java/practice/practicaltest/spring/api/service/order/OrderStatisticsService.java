package practice.practicaltest.spring.api.service.order;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practice.practicaltest.spring.api.service.mail.MailService;
import practice.practicaltest.spring.domain.order.Order;
import practice.practicaltest.spring.domain.order.OrderRepository;
import practice.practicaltest.spring.domain.order.OrderStatus;

@RequiredArgsConstructor
@Service
public class OrderStatisticsService {

    private final OrderRepository orderRepository;

    private final MailService mailService;

    public boolean sendOrderStatisticsMail(LocalDate orderDate, String email) {
        List<Order> orders = orderRepository.findAllBy(orderDate.atStartOfDay(),
                                                       orderDate.plusDays(1).atStartOfDay(),
                                                       OrderStatus.COMPLETED);

        int totalAmount = orders.stream()
                                .mapToInt(Order::getTotalPrice)
                                .sum();

        boolean sendResult = mailService.sendMail("no-reply@cafekiosk.com",
                                                  email,
                                                  "매출 통계",
                                                  "");

        if (!sendResult) {
            throw new IllegalStateException();
        }

        return true;
    }

}
