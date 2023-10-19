package practice.practicaltest.spring.api.controller.order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import practice.practicaltest.spring.api.service.order.OrderService;
import practice.practicaltest.spring.api.service.order.dto.OrderCreateRequest;

@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @Test
    @DisplayName("신규 주문을 등록한다.")
    public void createProduct() throws Exception {
        // given
        LocalDateTime now = LocalDateTime.now();
        OrderCreateRequest request = OrderCreateRequest.builder()
                                                       .productNumbers(List.of("001"))
                                                       .orderDateTime(now)
                                                       .build();

        // when // then
        mockMvc.perform(post("/api/v1/orders/order")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isOk());
    }

    @Test
    @DisplayName("신규 주문을 등록할 때, 상품 번호는 1개 이상이어야 한다.")
    public void createProduct1() throws Exception {
        // given
        LocalDateTime now = LocalDateTime.now();
        OrderCreateRequest request = OrderCreateRequest.builder()
                                                       .productNumbers(List.of())
                                                       .orderDateTime(now)
                                                       .build();

        // when // then
        mockMvc.perform(post("/api/v1/orders/order")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.code").value("400"))
               .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
               .andExpect(jsonPath("$.message").value("상품 번호 리스트는 필수값입니다."))
               .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("신규 주문을 등록할 때, 주문 시간은 과거 혹은 현재여야 한다.")
    public void createProduct2() throws Exception {
        // given
        LocalDateTime now = LocalDateTime.MAX;
        OrderCreateRequest request = OrderCreateRequest.builder()
                                                       .productNumbers(List.of("001"))
                                                       .orderDateTime(now)
                                                       .build();

        // when // then
        mockMvc.perform(post("/api/v1/orders/order")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.code").value("400"))
               .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
               .andExpect(jsonPath("$.message").value("주문 시간은 미래로 설정할 수 없습니다."))
               .andExpect(jsonPath("$.data").isEmpty());
    }

}