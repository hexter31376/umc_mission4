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
@Tag(name = "주문 API", description = "주문 생성 및 조회 관련 API")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(
        summary = "주문 생성",
        description = "회원 ID와 주문할 도서 아이템 목록(bookItemId, quantity)을 전송하여 주문을 생성합니다. 여러 개의 도서 아이템을 한 번에 주문할 수 있습니다."
    )
    @PostMapping
    public ResponseEntity<ApiSuccessResponse<OrderResponseDto>> create(@Valid @RequestBody OrderCreateDto dto) {
        OrderResponseDto created = orderService.create(dto);
        ApiSuccessResponse<OrderResponseDto> payload = new ApiSuccessResponse<>(
            GeneralSuccessCode.CREATED.getCode(),
            GeneralSuccessCode.CREATED.getMessage(),
            created
        );
        return ResponseEntity.status(GeneralSuccessCode.CREATED.getStatus()).body(payload);
    }

    @Operation(
        summary = "주문 조회",
        description = "주문 ID로 주문 정보를 조회합니다. 주문에 포함된 모든 주문 아이템 정보도 함께 반환됩니다."
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<OrderResponseDto>> get(@PathVariable Long id) {
        OrderResponseDto dto = orderService.find(id);
        ApiSuccessResponse<OrderResponseDto> payload = new ApiSuccessResponse<>(
            GeneralSuccessCode.OK.getCode(),
            GeneralSuccessCode.OK.getMessage(),
            dto
        );
        return ResponseEntity.ok(payload);
    }
}
