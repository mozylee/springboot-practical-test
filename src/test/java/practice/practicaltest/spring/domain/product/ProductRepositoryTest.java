package practice.practicaltest.spring.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static practice.practicaltest.spring.domain.product.ProductSellingType.HOLD;
import static practice.practicaltest.spring.domain.product.ProductSellingType.SELLING;
import static practice.practicaltest.spring.domain.product.ProductSellingType.STOP_SELLING;
import static practice.practicaltest.spring.domain.product.ProductType.BAKERY;
import static practice.practicaltest.spring.domain.product.ProductType.BOTTLED;
import static practice.practicaltest.spring.domain.product.ProductType.HANDMADE;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("원하는 판매 상태를 가진 상품들을 조회한다.")
    public void findAllBySellingStatusIn() throws Exception {
        // given
        List<Product> sellingProducts = List.of(Product.builder()
                                                       .productType(HANDMADE)
                                                       .productSellingType(SELLING)
                                                       .name("아메리카노")
                                                       .build(),
                                                Product.builder()
                                                       .productType(BOTTLED)
                                                       .productSellingType(SELLING)
                                                       .name("라떼")
                                                       .build());
        List<Product> holdProducts = List.of(Product.builder()
                                                    .productType(HANDMADE)
                                                    .productSellingType(HOLD)
                                                    .name("콜드 브루")
                                                    .build());
        List<Product> stopSellingProducts = List.of(Product.builder()
                                                           .productType(BAKERY)
                                                           .productSellingType(STOP_SELLING)
                                                           .name("감자빵")
                                                           .build(),
                                                    Product.builder()
                                                           .productType(HANDMADE)
                                                           .productSellingType(STOP_SELLING)
                                                           .name("달고나 라떼")
                                                           .build());
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
        List<Product> sellingProducts = List.of(Product.builder()
                                                       .productNumber("001")
                                                       .productType(HANDMADE)
                                                       .productSellingType(SELLING)
                                                       .name("아메리카노")
                                                       .build(),
                                                Product.builder()
                                                       .productNumber("002")
                                                       .productType(BOTTLED)
                                                       .productSellingType(SELLING)
                                                       .name("라떼")
                                                       .build(),
                                                Product.builder()
                                                       .productNumber("003")
                                                       .productType(BAKERY)
                                                       .productSellingType(SELLING)
                                                       .name("라떼")
                                                       .build());
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

}