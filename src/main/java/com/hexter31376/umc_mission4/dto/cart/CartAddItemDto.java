package com.hexter31376.umc_mission4.dto.cart;

import lombok.*;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartAddItemDto {
    @NotNull
    private Long memberId;

    @NotNull
    private Long bookItemId;

    @NotNull
    private Integer quantity;
}

