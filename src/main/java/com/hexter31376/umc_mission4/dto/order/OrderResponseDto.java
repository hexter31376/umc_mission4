package com.hexter31376.umc_mission4.dto.order;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDto {
    private Long id;
    private Long memberId;
    private Long totalPrice;
    private List<OrderItemResponseDto> items;
}

