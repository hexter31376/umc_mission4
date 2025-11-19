package com.hexter31376.umc_mission4.dto.book;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookItemResponseDto {
    private Long id;
    private String isbn;
    private Long price;
    private Integer quantity;
}

