package practice.practicaltest.spring.domain.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import practice.practicaltest.spring.domain.BaseEntity;
import practice.practicaltest.spring.domain.orderproduct.OrderProduct;
import practice.practicaltest.spring.domain.product.Product;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
@Entity
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private int totalPrice;

    private LocalDateTime orderDateTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    public void updateOrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts.addAll(orderProducts);
        this.totalPrice = orderProducts.stream()
                                       .map(OrderProduct::getProduct)
                                       .mapToInt(Product::getPrice)
                                       .sum();
    }

    public void addOrderProduct(OrderProduct orderProduct) {
        this.orderProducts.add(orderProduct);
        this.totalPrice = orderProduct.getProduct()
                                      .getPrice();
    }

    @Builder
    private Order(OrderStatus orderStatus, int totalPrice, LocalDateTime orderDateTime, List<OrderProduct> orderProducts) {
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.orderDateTime = orderDateTime;
        this.orderProducts = orderProducts;
    }

    public Order(List<Product> products, LocalDateTime orderDateTime) {
        this.orderStatus = OrderStatus.CREATED;
        this.totalPrice = this.calculateTotalPrice(products);
        this.orderDateTime = orderDateTime;
        this.orderProducts = products.stream()
                                     .map(product -> OrderProduct.builder()
                                                                 .order(this)
                                                                 .product(product)
                                                                 .build())
                                     .toList();
    }

    public static Order create(List<Product> products, LocalDateTime orderDateTime) {
        return new Order(products, orderDateTime);
    }

    public static Order create(LocalDateTime orderDateTime) {
        return Order.builder()
                    .orderStatus(OrderStatus.CREATED)
                    .orderDateTime(orderDateTime)
                    .orderProducts(new ArrayList<>())
                    .build();
    }

    private int calculateTotalPrice(List<Product> products) {
        return products.stream()
                       .mapToInt(Product::getPrice)
                       .sum();
    }

}
