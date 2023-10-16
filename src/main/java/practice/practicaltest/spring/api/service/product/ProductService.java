package practice.practicaltest.spring.api.service.product;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practice.practicaltest.spring.api.service.product.dto.response.SellingProductResponse;
import practice.practicaltest.spring.domain.product.ProductRepository;
import practice.practicaltest.spring.domain.product.ProductSellingType;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public List<SellingProductResponse> getSellingProducts() {
        return productRepository.findAllByProductSellingTypeIn(ProductSellingType.forDisplay())
                                .stream()
                                .map(SellingProductResponse::of)
                                .toList();
    }

}
