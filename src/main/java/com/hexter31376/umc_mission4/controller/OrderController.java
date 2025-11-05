package com.hexter31376.umc_mission4.controller;

import com.hexter31376.umc_mission4.dto.order.OrderCreateDto;
import com.hexter31376.umc_mission4.dto.order.OrderResponseDto;
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
    public ResponseEntity<OrderResponseDto> orderDirect(@Valid @RequestBody OrderCreateDto dto) {
        return ResponseEntity.ok(orderService.orderDirect(dto));
    }

    @PostMapping("/from-cart")
    public ResponseEntity<OrderResponseDto> orderFromCart(@Valid @RequestBody OrderCreateDto dto) {
        return ResponseEntity.ok(orderService.orderFromCart(dto));
    }
}
