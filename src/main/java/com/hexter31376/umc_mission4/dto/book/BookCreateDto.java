package com.hexter31376.umc_mission4.dto.book;

import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookCreateDto {
    @NotBlank
    @Size(max = 100)
    private String title;

    @Size(max = 100)
    private String author;

    @Size(max = 1000)
    private String description;
}
