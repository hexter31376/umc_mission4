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

    @Operation(
        summary = "장바구니 아이템 추가",
        description = "회원의 장바구니에 도서 아이템을 추가합니다. 회원 ID, 도서 아이템 ID, 수량을 전송해야 합니다. " +
                     "회원의 장바구니가 없으면 자동으로 생성됩니다. 추가된 장바구니 아이템 정보가 반환됩니다."
    )
    @PostMapping("/items")
    public ResponseEntity<ApiSuccessResponse<CartItemResponseDto>> addItem(@Valid @RequestBody CartAddItemDto dto) {
        CartItemResponseDto created = cartService.addItem(dto);
        ApiSuccessResponse<CartItemResponseDto> payload = new ApiSuccessResponse<>(
            GeneralSuccessCode.CREATED.getCode(),
            GeneralSuccessCode.CREATED.getMessage(),
            created
        );
        return ResponseEntity.status(GeneralSuccessCode.CREATED.getStatus()).body(payload);
    }

    @Operation(
        summary = "장바구니 아이템 조회",
        description = "장바구니 아이템 ID로 특정 장바구니 아이템을 조회합니다."
    )
    @GetMapping("/items/{id}")
    public ResponseEntity<ApiSuccessResponse<CartItemResponseDto>> getItem(@PathVariable Long id) {
        CartItemResponseDto dto = cartService.findCartItem(id);
        ApiSuccessResponse<CartItemResponseDto> payload = new ApiSuccessResponse<>(
            GeneralSuccessCode.OK.getCode(),
            GeneralSuccessCode.OK.getMessage(),
            dto
        );
        return ResponseEntity.ok(payload);
    }

    @Operation(
        summary = "장바구니 아이템 삭제",
        description = "장바구니에서 특정 아이템을 삭제합니다."
    )
    @DeleteMapping("/items/{id}")
    public ResponseEntity<ApiSuccessResponse<Void>> removeItem(@PathVariable Long id) {
        cartService.removeCartItem(id);
        ApiSuccessResponse<Void> payload = new ApiSuccessResponse<>(
            GeneralSuccessCode.OK.getCode(),
            "장바구니 아이템이 성공적으로 삭제되었습니다.",
            null
        );
        return ResponseEntity.ok(payload);
    }
}
