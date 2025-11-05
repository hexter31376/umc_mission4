package com.hexter31376.umc_mission4.dto.order;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponseDto {
    private Long id;
    private Long bookItemId;
    private Long price;
    private Integer quantity;
}

