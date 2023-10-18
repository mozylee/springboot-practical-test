package practice.practicaltest.spring.api.service.order.dto;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class OrderCreateRequest {

    @NotEmpty(message = "상품 번호 리스트는 필수값입니다.")
    private List<String> productNumbers;

    @PastOrPresent(message = "주문 시간은 미래로 설정할 수 없습니다.")
    private LocalDateTime orderDateTime;

    @Builder
    public OrderCreateRequest(List<String> productNumbers, LocalDateTime orderDateTime) {
        this.productNumbers = productNumbers;
        this.orderDateTime = orderDateTime;
    }

}
