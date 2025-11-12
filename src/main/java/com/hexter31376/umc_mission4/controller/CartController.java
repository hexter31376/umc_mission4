package com.hexter31376.umc_mission4.controller;

import com.hexter31376.umc_mission4.dto.cart.CartAddItemDto;
import com.hexter31376.umc_mission4.dto.cart.CartItemResponseDto;
import com.hexter31376.umc_mission4.global.apiPayload.ApiSuccessResponse;
import com.hexter31376.umc_mission4.global.apiPayload.code.GeneralSuccessCode;
import com.hexter31376.umc_mission4.service.CartService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Validated
@RestController
@RequestMapping("/api/carts")
@Tag(name = "장바구니 API", description = "장바구니에 아이템을 추가하고 조회하는 API")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Operation(summary = "장바구니 아이템 추가", description = "회원의 장바구니에 도서(품목)를 추가합니다. CartAddItemDto를 전송하세요.")
    @PostMapping("/items")
    public ResponseEntity<ApiSuccessResponse<CartItemResponseDto>> addItem(@Valid @RequestBody CartAddItemDto dto) {
        CartItemResponseDto created = cartService.addItem(dto);
        ApiSuccessResponse<CartItemResponseDto> payload = new ApiSuccessResponse<>(GeneralSuccessCode.CREATED.getCode(), GeneralSuccessCode.CREATED.getMessage(), created);
        return ResponseEntity.status(GeneralSuccessCode.CREATED.getStatus()).body(payload);
    }
}
