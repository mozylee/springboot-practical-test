package practice.practicaltest.spring.api.controller.product;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import practice.practicaltest.spring.api.ApiResponse;
import practice.practicaltest.spring.api.service.product.ProductService;
import practice.practicaltest.spring.api.service.product.dto.request.ProductCreateRequest;
import practice.practicaltest.spring.api.service.product.dto.response.SellingProductResponse;

@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@RestController
public class ProductController {

    private final ProductService productService;

    @GetMapping("/selling")
    public ApiResponse<List<SellingProductResponse>> getSellingProducts() {
        return ApiResponse.ok(productService.getSellingProducts());
    }

    @PostMapping
    public ApiResponse<SellingProductResponse> createProduct(@Valid @RequestBody ProductCreateRequest productCreateRequest) {
        return ApiResponse.ok(productService.createProduct(productCreateRequest));
    }

}
