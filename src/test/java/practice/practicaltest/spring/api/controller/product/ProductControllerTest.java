package practice.practicaltest.spring.api.controller.product;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import practice.practicaltest.spring.api.service.product.ProductService;
import practice.practicaltest.spring.api.service.product.dto.request.ProductCreateRequest;
import practice.practicaltest.spring.api.service.product.dto.response.SellingProductResponse;
import practice.practicaltest.spring.domain.product.ProductSellingType;
import practice.practicaltest.spring.domain.product.ProductType;

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
        mockMvc.perform(post("/api/v1/products")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isOk());
    }

    @Test
    @DisplayName("신규 상품을 등록할 때, 상품 타입은 필수 값이다.")
    public void createProduct1() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                                                           .productType(null)
                                                           .productSellingType(ProductSellingType.SELLING)
                                                           .name("TEST")
                                                           .price(4000)
                                                           .build();

        // when // then
        mockMvc.perform(post("/api/v1/products")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.code").value("400"))
               .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
               .andExpect(jsonPath("$.message").value("상품 타입은 필수값입니다."))
               .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("신규 상품을 등록할 때, 상품 판매 타입은 필수 값이다.")
    public void createProduct2() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                                                           .productType(ProductType.HANDMADE)
                                                           .productSellingType(null)
                                                           .name("TEST")
                                                           .price(4000)
                                                           .build();

        // when // then
        mockMvc.perform(post("/api/v1/products")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.code").value("400"))
               .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
               .andExpect(jsonPath("$.message").value("상품 판매 타입은 필수값입니다."))
               .andExpect(jsonPath("$.data").isEmpty());
    }

    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\b"})
    @ParameterizedTest(name = "{index}: name = ({0})")
    @DisplayName("신규 상품을 등록할 때, 상품 이름은 필수 값이다.")
    public void createProduct3(String name) throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                                                           .productType(ProductType.HANDMADE)
                                                           .productSellingType(ProductSellingType.SELLING)
                                                           .name(name)
                                                           .price(4000)
                                                           .build();

        // when // then
        mockMvc.perform(post("/api/v1/products")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.code").value("400"))
               .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
               .andExpect(jsonPath("$.message").value("상품 이름은 필수값입니다."))
               .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("신규 상품을 등록할 때, 상품 가격은 0 초과여야 한다.")
    public void createProduct4() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                                                           .productType(ProductType.HANDMADE)
                                                           .productSellingType(ProductSellingType.SELLING)
                                                           .name("TEST")
                                                           .price(0)
                                                           .build();

        // when // then
        mockMvc.perform(post("/api/v1/products")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.code").value("400"))
               .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
               .andExpect(jsonPath("$.message").value("상품 가격은 0 초과여야 합니다."))
               .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("판매 상품을 조회한다.")
    public void getSellingProducts() throws Exception {
        // given
        List<SellingProductResponse> productResponses = List.of();

        // when // then
        when(productService.getSellingProducts()).thenReturn(productResponses);
        mockMvc.perform(get("/api/v1/products/selling"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.data").isArray());
    }

}