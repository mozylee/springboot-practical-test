package practice.practicaltest.spring.domain.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import practice.practicaltest.spring.domain.product.Product;
import practice.practicaltest.spring.domain.product.ProductSellingType;
import practice.practicaltest.spring.domain.product.ProductType;

class OrderTest {

    @Test
    @DisplayName("주문 생성 시, 상품 리스트에서 주문의 총 금액을 계산한다.")
    public void calculateTotalPrice() throws Exception {
        // given
        List<Product> products = List.of(getProductFixture("001", 1200),
                                         getProductFixture("002", 230),
                                         getProductFixture("003", 4));
        LocalDateTime orderDateTime = LocalDateTime.now();
        // when
        Order order = Order.create(products, orderDateTime);

        // then
        assertThat(order.getTotalPrice()).isEqualTo(products.stream()
                                                            .mapToInt(Product::getPrice)
                                                            .sum());
    }

    @Test
    @DisplayName("주문 생성 시, 주문 상태는 'CREATED'이다.")
    public void created() throws Exception {
        // given
        List<Product> products = List.of(getProductFixture("001", 1200),
                                         getProductFixture("002", 230),
                                         getProductFixture("003", 4));
        LocalDateTime orderDateTime = LocalDateTime.now();
        // when
        Order order = Order.create(products, orderDateTime);

        // then
        assertThat(order.getOrderStatus()).isEqualByComparingTo(OrderStatus.CREATED);
    }

    @Test
    @DisplayName("주문 생성 시, 주문 등록 시간을 설정한다.")
    public void getOrderDateTime() throws Exception {
        // given
        List<Product> products = List.of(getProductFixture("001", 1200),
                                         getProductFixture("002", 230),
                                         getProductFixture("003", 4));
        LocalDateTime orderDateTime = LocalDateTime.now();

        // when
        Order order = Order.create(products, orderDateTime);

        // then
        assertThat(order.getOrderDateTime()).isEqualTo(orderDateTime);
    }

    @Test
    @DisplayName("주문 생성 시, 주문 상품 목록을 생성한다.")
    public void getOrderProducts() throws Exception {
        // given
        List<Product> products = List.of(getProductFixture("001", 1200),
                                         getProductFixture("002", 230),
                                         getProductFixture("003", 4));
        LocalDateTime orderDateTime = LocalDateTime.now();

        // when
        Order order = Order.create(products, orderDateTime);

        // then
        assertThat(order.getOrderDateTime()).isEqualTo(orderDateTime);
    }

    private Product getProductFixture(String productNumber, int price) {
        return Product.builder()
                      .productType(ProductType.HANDMADE)
                      .productNumber(productNumber)
                      .price(price)
                      .productSellingType(ProductSellingType.SELLING)
                      .name("TEST")
                      .build();
    }

}