package com.hexter31376.umc_mission4.dto.order;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDto {
    @NotNull(message = "도서 아이템 ID는 필수입니다")
    private Long bookItemId;

    @NotNull(message = "수량은 필수입니다")
    @Min(value = 1, message = "수량은 1 이상이어야 합니다")
    private Integer quantity;
}

