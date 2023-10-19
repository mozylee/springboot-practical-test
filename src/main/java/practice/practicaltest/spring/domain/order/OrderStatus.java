package practice.practicaltest.spring.domain.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {

    CREATED("주문 생성"),

    CANCELED("주문 취소"),

    RECEIVED("주문 접수"),

    COMPLETED("처리 완료");

    private final String description;

}
