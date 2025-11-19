package com.hexter31376.umc_mission4.dto.book;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponseDto {
    private Long id;
    private String title;
    private String author;
    private String description;
    private List<BookItemResponseDto> bookItems;
}

