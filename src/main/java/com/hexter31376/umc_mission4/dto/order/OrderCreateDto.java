package com.hexter31376.umc_mission4.dto.order;

import lombok.*;

import jakarta.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCreateDto {
    // place order directly for a single book item, or place order from cart
    @NotNull
    private Long memberId;

    // optional: if provided, create order from these cartItemIds
    private List<Long> cartItemIds;

    // optional: direct order of a bookItem
    private Long bookItemId;

    private Integer quantity;
}

