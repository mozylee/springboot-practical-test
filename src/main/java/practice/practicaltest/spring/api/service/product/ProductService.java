package practice.practicaltest.spring.api.service.product;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.practicaltest.spring.api.service.product.dto.request.ProductCreateRequest;
import practice.practicaltest.spring.api.service.product.dto.response.SellingProductResponse;
import practice.practicaltest.spring.domain.product.Product;
import practice.practicaltest.spring.domain.product.ProductRepository;
import practice.practicaltest.spring.domain.product.ProductSellingType;

@RequiredArgsConstructor
@Service
public class ProductService {

    public static final String INITIAL_PRODUCT_NUMBER = "001";

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<SellingProductResponse> getSellingProducts() {
        return productRepository.findAllByProductSellingTypeIn(ProductSellingType.forDisplay())
                                .stream()
                                .map(SellingProductResponse::of)
                                .toList();
    }

    @Transactional
    public SellingProductResponse createProduct(ProductCreateRequest request) {
        String nextProductNumber = getNextProductNumber();

        Product product = Product.builder()
                                 .productNumber(nextProductNumber)
                                 .productType(request.getProductType())
                                 .productSellingType(request.getProductSellingType())
                                 .name(request.getName())
                                 .price(request.getPrice())
                                 .build();

        return SellingProductResponse.of(productRepository.save(product));
    }

    private String getNextProductNumber() {
        String latestProductNumber = productRepository.findLatestProductNumber();

        if (Objects.isNull(latestProductNumber)) {
            return INITIAL_PRODUCT_NUMBER;
        }

        int latestProductNumberInt = Integer.parseInt(latestProductNumber);
        int nextProductNumberInt = latestProductNumberInt + 1;

        return String.format("%03d", nextProductNumberInt);
    }

}
