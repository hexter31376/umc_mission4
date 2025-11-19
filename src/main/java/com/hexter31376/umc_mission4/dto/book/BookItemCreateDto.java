package com.hexter31376.umc_mission4.dto.book;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookItemCreateDto {
    @NotNull(message = "도서 ID는 필수입니다")
    private Long bookId;

    @NotBlank(message = "ISBN은 필수입니다")
    @Size(max = 32, message = "ISBN은 32자를 초과할 수 없습니다")
    private String isbn;

    @NotNull(message = "가격은 필수입니다")
    @PositiveOrZero(message = "가격은 0 이상이어야 합니다")
    private Long price;

    @NotNull(message = "수량은 필수입니다")
    @Min(value = 0, message = "수량은 0 이상이어야 합니다")
    private Integer quantity;
}

