package practice.practicaltest.spring.api.service.product.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import practice.practicaltest.spring.domain.product.Product;
import practice.practicaltest.spring.domain.product.ProductSellingType;
import practice.practicaltest.spring.domain.product.ProductType;

@ToString
@Getter
public class SellingProductResponse {

    private Long id;

    private String productNumber;

    private ProductType productType;

    private ProductSellingType productSellingType;

    private String name;

    private int price;

    @Builder
    private SellingProductResponse(Long id, String productNumber, ProductType productType, ProductSellingType productSellingType, String name,
                                   int price) {
        this.id = id;
        this.productNumber = productNumber;
        this.productType = productType;
        this.productSellingType = productSellingType;
        this.name = name;
        this.price = price;
    }

    public static SellingProductResponse of(Product product) {
        return SellingProductResponse.builder()
                                     .id(product.getId())
                                     .productNumber(product.getProductNumber())
                                     .productType(product.getProductType())
                                     .productSellingType(product.getProductSellingType())
                                     .name(product.getName())
                                     .price(product.getPrice())
                                     .build();
    }

}
