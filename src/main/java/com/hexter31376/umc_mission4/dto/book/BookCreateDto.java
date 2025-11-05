package com.hexter31376.umc_mission4.dto.book;

import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookCreateDto {
    @NotBlank
    private String title;

    private String author;

    private String description;
}

