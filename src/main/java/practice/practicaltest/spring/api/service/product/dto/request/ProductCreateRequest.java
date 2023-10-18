package practice.practicaltest.spring.api.service.product.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import practice.practicaltest.spring.domain.product.ProductSellingType;
import practice.practicaltest.spring.domain.product.ProductType;

@Getter
@NoArgsConstructor
public class ProductCreateRequest {

    private ProductType productType;

    private ProductSellingType productSellingType;

    private String name;

    private int price;

    @Builder
    private ProductCreateRequest(ProductType productType, ProductSellingType productSellingType, String name, int price) {
        this.productType = productType;
        this.productSellingType = productSellingType;
        this.name = name;
        this.price = price;
    }

}
