package practice.practicaltest.spring.domain.product;

import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductType {

    HANDMADE("제조 음료"),

    BOTTLED("병 음료"),

    BAKERY("베이커리");

    private final String description;

    public static boolean containsStockType(ProductType productType) {
        return Set.of(BOTTLED, BAKERY)
                  .contains(productType);
    }

}
