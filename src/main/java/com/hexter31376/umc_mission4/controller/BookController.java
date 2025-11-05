package com.hexter31376.umc_mission4.controller;

import com.hexter31376.umc_mission4.dto.book.BookCreateDto;
import com.hexter31376.umc_mission4.dto.book.BookResponseDto;
import com.hexter31376.umc_mission4.global.apiPayload.ApiSuccessResponse;
import com.hexter31376.umc_mission4.global.apiPayload.code.GeneralSuccessCode;
import com.hexter31376.umc_mission4.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<ApiSuccessResponse<BookResponseDto>> create(@Valid @RequestBody BookCreateDto dto) {
        BookResponseDto created = bookService.create(dto);
        ApiSuccessResponse<BookResponseDto> payload = new ApiSuccessResponse<>(GeneralSuccessCode.CREATED.getCode(), GeneralSuccessCode.CREATED.getMessage(), created);
        return ResponseEntity.status(GeneralSuccessCode.CREATED.getStatus()).body(payload);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<BookResponseDto>> get(@PathVariable Long id) {
        BookResponseDto dto = bookService.find(id);
        ApiSuccessResponse<BookResponseDto> payload = new ApiSuccessResponse<>(GeneralSuccessCode.OK.getCode(), GeneralSuccessCode.OK.getMessage(), dto);
        return ResponseEntity.ok(payload);
    }
}
