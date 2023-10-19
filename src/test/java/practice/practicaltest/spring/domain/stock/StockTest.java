package practice.practicaltest.spring.domain.stock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StockTest {

    @Test
    @DisplayName("재고의 수량이 제공된 수량보다 작은지 확인한다.")
    public void isQuantityLessThan() throws Exception {
        // given
        Stock stock = Stock.create("001", 1);
        int requiredQuantity = 2;

        // when
        boolean isQuantityLessThan = stock.isQuantityLessThan(requiredQuantity);

        // then
        assertThat(isQuantityLessThan).isTrue();
    }

    @Test
    @DisplayName("재고를 주어진 개수만큼 차감할 수 있다.")
    public void deductQuantity() throws Exception {
        // given
        Stock stock = Stock.create("001", 2);
        int requiredQuantity = stock.getQuantity() - 1;

        // when
        stock.deductQuantity(requiredQuantity);

        // then
        assertThat(stock.getQuantity()).isEqualTo(1);
    }

    @Test
    @DisplayName("재고보다 큰 수량으로 차감 시도하는 경우 예외가 발생한다.")
    public void deductQuantity2() throws Exception {
        // given
        Stock stock = Stock.create("001", 2);
        int requiredQuantity = stock.getQuantity() + 1;

        // when // then
        assertThatIllegalArgumentException().isThrownBy(() -> stock.deductQuantity(requiredQuantity))
                                            .withMessage("재고보다 많은 수의 수량으로 차감할 수 없습니다.");
    }

}