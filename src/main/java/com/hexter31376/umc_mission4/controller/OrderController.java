package com.hexter31376.umc_mission4.controller;

import com.hexter31376.umc_mission4.dto.order.OrderCreateDto;
import com.hexter31376.umc_mission4.dto.order.OrderResponseDto;
import com.hexter31376.umc_mission4.global.apiPayload.ApiSuccessResponse;
import com.hexter31376.umc_mission4.global.apiPayload.code.GeneralSuccessCode;
import com.hexter31376.umc_mission4.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Validated
@RestController
@RequestMapping("/api/orders")
@Tag(name = "주문 API", description = "직접 주문 및 장바구니 기반 주문 관련 API")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "직접 주문", description = "bookItemId와 quantity를 전송하여 바로 주문합니다.")
    @PostMapping("/direct")
    public ResponseEntity<ApiSuccessResponse<OrderResponseDto>> orderDirect(@Valid @RequestBody OrderCreateDto dto) {
        OrderResponseDto created = orderService.orderDirect(dto);
        ApiSuccessResponse<OrderResponseDto> payload = new ApiSuccessResponse<>(GeneralSuccessCode.CREATED.getCode(), GeneralSuccessCode.CREATED.getMessage(), created);
        return ResponseEntity.status(GeneralSuccessCode.CREATED.getStatus()).body(payload);
    }

    @Operation(summary = "장바구니 주문", description = "cartItemIds를 전송하여 장바구니의 항목으로 주문합니다.")
    @PostMapping("/from-cart")
    public ResponseEntity<ApiSuccessResponse<OrderResponseDto>> orderFromCart(@Valid @RequestBody OrderCreateDto dto) {
        OrderResponseDto created = orderService.orderFromCart(dto);
        ApiSuccessResponse<OrderResponseDto> payload = new ApiSuccessResponse<>(GeneralSuccessCode.CREATED.getCode(), GeneralSuccessCode.CREATED.getMessage(), created);
        return ResponseEntity.status(GeneralSuccessCode.CREATED.getStatus()).body(payload);
    }
}
