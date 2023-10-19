package practice.practicaltest.spring.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static practice.practicaltest.spring.domain.product.ProductSellingType.HOLD;
import static practice.practicaltest.spring.domain.product.ProductSellingType.SELLING;
import static practice.practicaltest.spring.domain.product.ProductSellingType.STOP_SELLING;
import static practice.practicaltest.spring.domain.product.ProductType.BAKERY;
import static practice.practicaltest.spring.domain.product.ProductType.BOTTLED;
import static practice.practicaltest.spring.domain.product.ProductType.HANDMADE;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("원하는 판매 상태를 가진 상품들을 조회한다.")
    public void findAllBySellingStatusIn() throws Exception {
        // given
        List<Product> sellingProducts = List.of(createProduct("001", HANDMADE, SELLING, "아메리카노"),
                                                createProduct("002", BOTTLED, SELLING, "라떼"));
        List<Product> holdProducts = List.of(createProduct("003", HANDMADE, HOLD, "콜드 브루"));
        List<Product> stopSellingProducts = List.of(createProduct("004", BAKERY, STOP_SELLING, "감자빵"),
                                                    createProduct("005", HANDMADE, STOP_SELLING, "달고나 라떼"));

        productRepository.saveAll(Stream.of(sellingProducts, holdProducts, stopSellingProducts)
                                        .flatMap(List::stream)
                                        .toList());

        // when
        List<Product> products = productRepository.findAllByProductSellingTypeIn(ProductSellingType.forDisplay());

        // then
        assertThat(products).hasSize(sellingProducts.size() + holdProducts.size())
                            .extracting("name", "productType", "productSellingType")
                            .containsExactlyInAnyOrderElementsOf(Stream.of(sellingProducts, holdProducts)
                                                                       .flatMap(List::stream)
                                                                       .map(product -> tuple(product.getName(),
                                                                                             product.getProductType(),
                                                                                             product.getProductSellingType()))
                                                                       .toList());
    }

    @Test
    @DisplayName("원하는 상품 번호를 가진 상품들을 조회한다.")
    void findAllByProductNumberIn() {
        // given
        List<Product> sellingProducts = List.of(createProduct("001", HANDMADE, SELLING, "아메리카노"),
                                                createProduct("002", BOTTLED, SELLING, "라떼"),
                                                createProduct("003", BAKERY, SELLING, "라떼"));
        productRepository.saveAll(sellingProducts);

        List<Product> foundProducts = sellingProducts.stream()
                                                     .limit(2)
                                                     .toList();

        // when
        List<Product> products = productRepository.findAllByProductNumberIn(foundProducts.stream()
                                                                                         .map(Product::getProductNumber)
                                                                                         .toList());

        // then
        assertThat(products).hasSize(foundProducts.size())
                            .extracting("productNumber", "name", "productType", "productSellingType")
                            .containsExactlyInAnyOrderElementsOf(foundProducts.stream()
                                                                              .map(product -> tuple(product.getProductNumber(),
                                                                                                    product.getName(),
                                                                                                    product.getProductType(),
                                                                                                    product.getProductSellingType()))
                                                                              .toList());
    }

    @Test
    @DisplayName("최근 마지막으로 저장한 상품의 상품 번호를 읽어온다.")
    void findLatestProductNumber() {
        // given
        String givenProductNumber = "003";

        List<Product> sellingProducts = List.of(createProduct("001", HANDMADE, SELLING, "아메리카노"),
                                                createProduct("002", BOTTLED, SELLING, "라떼"),
                                                createProduct(givenProductNumber, BAKERY, SELLING, "라떼"));
        productRepository.saveAll(sellingProducts);

        // when
        String latestProductNumber = productRepository.findLatestProductNumber();

        // then
        assertThat(latestProductNumber).isEqualTo(givenProductNumber);
    }

    @Test
    @DisplayName("최근 마지막으로 저장한 상품이 없는 경우, 상품 번호는 null을 반환한다.")
    void findLatestProductNumberWhenEmptyIsNull() {
        // given

        // when
        String latestProductNumber = productRepository.findLatestProductNumber();

        // then
        assertThat(latestProductNumber).isNull();
    }

    private static Product createProduct(String productNumber, ProductType productType, ProductSellingType productSellingType, String name) {
        return Product.builder()
                      .productNumber(productNumber)
                      .productType(productType)
                      .productSellingType(productSellingType)
                      .name(name)
                      .build();
    }

}