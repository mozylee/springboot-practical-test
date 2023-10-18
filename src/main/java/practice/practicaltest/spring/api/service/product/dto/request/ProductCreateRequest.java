package practice.practicaltest.spring.api.service.product.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import practice.practicaltest.spring.domain.product.ProductSellingType;
import practice.practicaltest.spring.domain.product.ProductType;

@Getter
@NoArgsConstructor
public class ProductCreateRequest {

    @NotNull(message = "상품 타입은 필수값입니다.")
    private ProductType productType;

    @NotNull(message = "상품 판매 타입은 필수값입니다.")
    private ProductSellingType productSellingType;

    @NotBlank(message = "상품 이름은 필수값입니다.")
    private String name;

    @Positive(message = "상품 가격은 0 초과여야 합니다.")
    private int price;

    @Builder
    private ProductCreateRequest(ProductType productType, ProductSellingType productSellingType, String name, int price) {
        this.productType = productType;
        this.productSellingType = productSellingType;
        this.name = name;
        this.price = price;
    }

}
