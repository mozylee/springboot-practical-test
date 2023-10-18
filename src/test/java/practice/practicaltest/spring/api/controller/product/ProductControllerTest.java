package practice.practicaltest.spring.api.controller.product;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import practice.practicaltest.spring.api.service.product.ProductService;
import practice.practicaltest.spring.api.service.product.dto.request.ProductCreateRequest;
import practice.practicaltest.spring.domain.product.ProductSellingType;
import practice.practicaltest.spring.domain.product.ProductType;

@ActiveProfiles("test")
@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    @DisplayName("신규 상품을 등록한다.")
    public void createProduct() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                                                           .productType(ProductType.HANDMADE)
                                                           .productSellingType(ProductSellingType.SELLING)
                                                           .name("TEST")
                                                           .price(4000)
                                                           .build();

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products")
                                              .content(objectMapper.writeValueAsString(request))
                                              .contentType(MediaType.APPLICATION_JSON))
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

}