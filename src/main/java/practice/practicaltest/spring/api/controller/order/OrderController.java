package practice.practicaltest.spring.api.controller.order;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import practice.practicaltest.spring.api.ApiResponse;
import practice.practicaltest.spring.api.service.order.OrderService;
import practice.practicaltest.spring.api.service.order.dto.OrderCreateRequest;
import practice.practicaltest.spring.api.service.order.dto.OrderCreateResponse;

@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@RestController
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order")
    public ApiResponse<OrderCreateResponse> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        return ApiResponse.ok(orderService.createOrder(request));
    }

}
