package practice.practicaltest.spring.api.service.order.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import practice.practicaltest.spring.api.service.product.dto.response.SellingProductResponse;
import practice.practicaltest.spring.domain.order.Order;
import practice.practicaltest.spring.domain.orderproduct.OrderProduct;
import practice.practicaltest.spring.domain.product.Product;


@ToString
@Getter
@NoArgsConstructor
public class OrderCreateResponse {

    private Long id;

    private int totalPrice;

    private LocalDateTime orderDateTime;

    private List<SellingProductResponse> products = new ArrayList<>();

    @Builder
    private OrderCreateResponse(Long id, int totalPrice, LocalDateTime orderDateTime, List<SellingProductResponse> products) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.orderDateTime = orderDateTime;
        this.products = products;
    }

    public static OrderCreateResponse of(Order order) {
        List<SellingProductResponse> products = order.getOrderProducts()
                                                     .stream()
                                                     .map(OrderProduct::getProduct)
                                                     .map(SellingProductResponse::of)
                                                     .toList();

        return OrderCreateResponse.builder()
                                  .id(order.getId())
                                  .totalPrice(order.getTotalPrice())
                                  .orderDateTime(order.getOrderDateTime())
                                  .products(products)
                                  .build();
    }

}
