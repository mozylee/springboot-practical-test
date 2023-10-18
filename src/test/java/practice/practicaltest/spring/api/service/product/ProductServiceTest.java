package practice.practicaltest.spring.api.service.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static practice.practicaltest.spring.domain.product.ProductSellingType.SELLING;
import static practice.practicaltest.spring.domain.product.ProductType.BAKERY;
import static practice.practicaltest.spring.domain.product.ProductType.HANDMADE;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import practice.practicaltest.spring.api.service.product.dto.request.ProductCreateRequest;
import practice.practicaltest.spring.api.service.product.dto.response.SellingProductResponse;
import practice.practicaltest.spring.domain.product.Product;
import practice.practicaltest.spring.domain.product.ProductRepository;
import practice.practicaltest.spring.domain.product.ProductSellingType;
import practice.practicaltest.spring.domain.product.ProductType;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("신규 상품을 등록할 때, 등록된 상품 번호는 가장 최근 삽입된 상품의 상품 번호에서 1 증가한 값이다.")
    public void createProduct() throws Exception {
        // given
        String productNumber = "002";
        Product product = createProduct("001", HANDMADE, SELLING, "아메리카노");
        productRepository.save(product);

        ProductCreateRequest request = ProductCreateRequest.builder()
                                                           .productType(BAKERY)
                                                           .productSellingType(SELLING)
                                                           .name("TEST")
                                                           .price(9999)
                                                           .build();

        // when
        SellingProductResponse response = productService.createProduct(request);

        // then
        assertThat(response).extracting("productNumber", "productType", "productSellingType", "name", "price")
                            .containsExactlyInAnyOrder(productNumber,
                                                       request.getProductType(),
                                                       request.getProductSellingType(),
                                                       request.getName(),
                                                       request.getPrice());

        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(2)
                            .extracting("productNumber", "productType", "productSellingType", "name", "price")
                            .containsExactlyInAnyOrder(
                                    tuple(productNumber, request.getProductType(), request.getProductSellingType(), request.getName(),
                                          request.getPrice()),
                                    tuple(product.getProductNumber(), product.getProductType(), product.getProductSellingType(),
                                          product.getName(), product.getPrice())
                            );
    }

    @Test
    @DisplayName("신규 상품을 등록할 때, 이미 저장된 상품이 하나도 없으면 등록된 상품 번호는 '001'이다.")
    public void createProductWhenProductsIsEmpty() throws Exception {
        // given
        String givenProductNumber = "001";

        ProductCreateRequest request = ProductCreateRequest.builder()
                                                           .productType(BAKERY)
                                                           .productSellingType(SELLING)
                                                           .name("TEST")
                                                           .price(9999)
                                                           .build();

        // when
        SellingProductResponse response = productService.createProduct(request);

        // then
        assertThat(response).extracting("productNumber", "productType", "productSellingType", "name", "price")
                            .containsExactlyInAnyOrder(givenProductNumber,
                                                       request.getProductType(),
                                                       request.getProductSellingType(),
                                                       request.getName(),
                                                       request.getPrice());

        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(1)
                            .extracting("productNumber", "productType", "productSellingType", "name", "price")
                            .containsExactlyInAnyOrder(tuple(givenProductNumber,
                                                             request.getProductType(),
                                                             request.getProductSellingType(),
                                                             request.getName(),
                                                             request.getPrice()));
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