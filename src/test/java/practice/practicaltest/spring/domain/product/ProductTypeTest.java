package practice.practicaltest.spring.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;
import org.junit.jupiter.params.provider.ValueSource;

class ProductTypeTest {

    @ParameterizedTest
    @EnumSource(mode = Mode.INCLUDE, names = {"HANDMADE"})
    @DisplayName("상품 타입이 재고 타입인지를 체크한다.")
    public void containsStockType(ProductType productType) throws Exception {
        // given
        ProductType givenType = productType;

        // when
        boolean isStockType = ProductType.containsStockType(givenType);

        // then
        assertThat(isStockType).isFalse();
    }


    @ParameterizedTest
    @EnumSource(mode = Mode.INCLUDE, names = {"BOTTLED", "BAKERY"})
    @DisplayName("상품 타입이 재고 타입이 아닌지를 체크한다.")
    public void containsStockType2(ProductType productType) throws Exception {
        // given
        ProductType givenType = productType;

        // when
        boolean isStockType = ProductType.containsStockType(givenType);

        // then
        assertThat(isStockType).isTrue();
    }

}