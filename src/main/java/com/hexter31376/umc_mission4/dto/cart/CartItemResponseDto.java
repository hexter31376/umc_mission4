package com.hexter31376.umc_mission4.dto.cart;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponseDto {
    private Long id;
    private Long bookItemId;
    private Integer quantity;
    private Long totalPrice;
}

