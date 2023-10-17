package practice.practicaltest.spring.api.service.order.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class OrderCreateRequest {

    private List<String> productNumbers;

    private LocalDateTime orderDateTime;

    @Builder
    public OrderCreateRequest(List<String> productNumbers, LocalDateTime orderDateTime) {
        this.productNumbers = productNumbers;
        this.orderDateTime = orderDateTime;
    }

}
