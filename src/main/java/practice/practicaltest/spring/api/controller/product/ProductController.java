package practice.practicaltest.spring.api.controller.product;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import practice.practicaltest.spring.api.service.product.ProductService;
import practice.practicaltest.spring.api.service.product.dto.response.SellingProductResponse;

@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@RestController
public class ProductController {

    private final ProductService productService;

    @GetMapping("/selling")
    public List<SellingProductResponse> getSellingProducts() {
        return productService.getSellingProducts();
    }

}
