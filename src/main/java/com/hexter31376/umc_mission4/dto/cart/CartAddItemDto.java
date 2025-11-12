package com.hexter31376.umc_mission4.dto.cart;

import lombok.*;

import jakarta.validation.constraints.*;

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
    @Min(1)
    private Integer quantity;
}
