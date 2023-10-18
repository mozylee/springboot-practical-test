package practice.practicaltest.spring.domain.stock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static practice.practicaltest.spring.domain.product.ProductSellingType.SELLING;
import static practice.practicaltest.spring.domain.product.ProductType.BAKERY;
import static practice.practicaltest.spring.domain.product.ProductType.BOTTLED;
import static practice.practicaltest.spring.domain.product.ProductType.HANDMADE;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import practice.practicaltest.spring.domain.product.Product;

@ActiveProfiles("test")
@DataJpaTest
class StockRepositoryTest {

    @Autowired
    private StockRepository stockRepository;

    @Test
    @DisplayName("원하는 상품 번호를 가진 상품들을 조회한다.")
    void findAllByProductNumberIn() {
        // given
        List<Stock> givenStocks = List.of(Stock.create("001", 1),
                                          Stock.create("002", 2),
                                          Stock.create("003", 3));
        stockRepository.saveAll(givenStocks);

        List<Stock> foundStocks = givenStocks.stream()
                                             .limit(2)
                                             .toList();

        // when
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(foundStocks.stream()
                                                                                 .map(Stock::getProductNumber)
                                                                                 .toList());

        // then
        assertThat(stocks).hasSize(foundStocks.size())
                          .extracting("productNumber", "quantity")
                          .containsExactlyInAnyOrderElementsOf(foundStocks.stream()
                                                                          .map(stock -> tuple(stock.getProductNumber(),
                                                                                              stock.getQuantity()))
                                                                          .toList());
    }

}