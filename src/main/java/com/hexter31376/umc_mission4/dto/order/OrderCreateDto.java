package com.hexter31376.umc_mission4.dto.order;

import lombok.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCreateDto {
    @NotNull(message = "회원 ID는 필수입니다")
    private Long memberId;

    @NotEmpty(message = "최소 1개 이상의 주문 아이템이 필요합니다")
    @Valid
    private List<OrderItemDto> orderItems;
}
