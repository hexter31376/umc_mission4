package com.hexter31376.umc_mission4.controller;

import com.hexter31376.umc_mission4.dto.cart.CartAddItemDto;
import com.hexter31376.umc_mission4.dto.cart.CartItemResponseDto;
import com.hexter31376.umc_mission4.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/items")
    public ResponseEntity<CartItemResponseDto> addItem(@Valid @RequestBody CartAddItemDto dto) {
        return ResponseEntity.ok(cartService.addItem(dto));
    }
}
