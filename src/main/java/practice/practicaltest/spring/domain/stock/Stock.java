package practice.practicaltest.spring.domain.stock;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import practice.practicaltest.spring.domain.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Stock extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productNumber;

    private int quantity;

    public static Stock create(String productNumber, int quantity) {
        return Stock.builder()
                    .productNumber(productNumber)
                    .quantity(quantity)
                    .build();
    }

    public boolean isQuantityLessThan(int requiredQuantity) {
        return this.quantity < requiredQuantity;
    }

    public void deductQuantity(int requiredQuantity) {
        if (requiredQuantity > this.quantity) {
            throw new IllegalArgumentException("재고보다 많은 수의 수량으로 차감할 수 없습니다.");
        }

        this.quantity -= requiredQuantity;
    }

    @Builder
    private Stock(String productNumber, int quantity) {
        this.productNumber = productNumber;
        this.quantity = quantity;
    }

}
