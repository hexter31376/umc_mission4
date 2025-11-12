package com.hexter31376.umc_mission4.controller;

import com.hexter31376.umc_mission4.dto.book.BookCreateDto;
import com.hexter31376.umc_mission4.dto.book.BookResponseDto;
import com.hexter31376.umc_mission4.global.apiPayload.ApiSuccessResponse;
import com.hexter31376.umc_mission4.global.apiPayload.code.GeneralSuccessCode;
import com.hexter31376.umc_mission4.service.BookService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Validated
@RestController
@RequestMapping("/api/books")
@Tag(name = "도서 API", description = "도서 생성 및 조회 관련 API")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "도서 생성", description = "새로운 도서를 생성합니다. 요청 바디는 BookCreateDto 형식입니다.")
    @PostMapping
    public ResponseEntity<ApiSuccessResponse<BookResponseDto>> create(@Valid @RequestBody BookCreateDto dto) {
        BookResponseDto created = bookService.create(dto);
        ApiSuccessResponse<BookResponseDto> payload = new ApiSuccessResponse<>(GeneralSuccessCode.CREATED.getCode(), GeneralSuccessCode.CREATED.getMessage(), created);
        return ResponseEntity.status(GeneralSuccessCode.CREATED.getStatus()).body(payload);
    }

    @Operation(summary = "도서 조회", description = "ID로 도서를 조회합니다. 성공 시 BookResponseDto 반환")
    @GetMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<BookResponseDto>> get(@PathVariable Long id) {
        BookResponseDto dto = bookService.find(id);
        ApiSuccessResponse<BookResponseDto> payload = new ApiSuccessResponse<>(GeneralSuccessCode.OK.getCode(), GeneralSuccessCode.OK.getMessage(), dto);
        return ResponseEntity.ok(payload);
    }
}
