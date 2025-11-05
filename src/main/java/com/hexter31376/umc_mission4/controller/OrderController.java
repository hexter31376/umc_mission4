package com.hexter31376.umc_mission4.controller;

import com.hexter31376.umc_mission4.dto.order.OrderCreateDto;
import com.hexter31376.umc_mission4.dto.order.OrderResponseDto;
import com.hexter31376.umc_mission4.global.apiPayload.ApiSuccessResponse;
import com.hexter31376.umc_mission4.global.apiPayload.code.GeneralSuccessCode;
import com.hexter31376.umc_mission4.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/direct")
    public ResponseEntity<ApiSuccessResponse<OrderResponseDto>> orderDirect(@Valid @RequestBody OrderCreateDto dto) {
        OrderResponseDto created = orderService.orderDirect(dto);
        ApiSuccessResponse<OrderResponseDto> payload = new ApiSuccessResponse<>(GeneralSuccessCode.CREATED.getCode(), GeneralSuccessCode.CREATED.getMessage(), created);
        return ResponseEntity.status(GeneralSuccessCode.CREATED.getStatus()).body(payload);
    }

    @PostMapping("/from-cart")
    public ResponseEntity<ApiSuccessResponse<OrderResponseDto>> orderFromCart(@Valid @RequestBody OrderCreateDto dto) {
        OrderResponseDto created = orderService.orderFromCart(dto);
        ApiSuccessResponse<OrderResponseDto> payload = new ApiSuccessResponse<>(GeneralSuccessCode.CREATED.getCode(), GeneralSuccessCode.CREATED.getMessage(), created);
        return ResponseEntity.status(GeneralSuccessCode.CREATED.getStatus()).body(payload);
    }
}
