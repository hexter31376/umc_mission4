package com.hexter31376.umc_mission4.dto.book;

import lombok.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookCreateDto {
    @NotBlank(message = "제목은 필수입니다")
    @Size(max = 100, message = "제목은 100자를 초과할 수 없습니다")
    private String title;

    @Size(max = 100, message = "저자명은 100자를 초과할 수 없습니다")
    private String author;

    @Size(max = 1000, message = "설명은 1000자를 초과할 수 없습니다")
    private String description;

    @NotEmpty(message = "최소 1개 이상의 도서 아이템이 필요합니다")
    @Valid
    private List<BookItemDto> bookItems;
}
