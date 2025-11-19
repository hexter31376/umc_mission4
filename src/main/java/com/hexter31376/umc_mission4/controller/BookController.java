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

    @Operation(
        summary = "도서 생성",
        description = "새로운 도서를 생성합니다. 도서 기본 정보(제목, 저자, 설명)와 함께 최소 1개 이상의 도서 아이템(ISBN, 가격, 수량)을 포함해야 합니다. " +
                     "도서 아이템은 실제 판매 가능한 재고 단위를 나타냅니다."
    )
    @PostMapping
    public ResponseEntity<ApiSuccessResponse<BookResponseDto>> create(@Valid @RequestBody BookCreateDto dto) {
        BookResponseDto created = bookService.create(dto);
        ApiSuccessResponse<BookResponseDto> payload = new ApiSuccessResponse<>(
            GeneralSuccessCode.CREATED.getCode(),
            GeneralSuccessCode.CREATED.getMessage(),
            created
        );
        return ResponseEntity.status(GeneralSuccessCode.CREATED.getStatus()).body(payload);
    }

    @Operation(
        summary = "도서 조회",
        description = "도서 ID로 도서 정보를 조회합니다. 도서의 기본 정보와 함께 등록된 모든 도서 아이템 정보도 함께 반환됩니다."
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<BookResponseDto>> get(@PathVariable Long id) {
        BookResponseDto dto = bookService.find(id);
        ApiSuccessResponse<BookResponseDto> payload = new ApiSuccessResponse<>(
            GeneralSuccessCode.OK.getCode(),
            GeneralSuccessCode.OK.getMessage(),
            dto
        );
        return ResponseEntity.ok(payload);
    }

    @Operation(
        summary = "도서 삭제",
        description = "도서 ID로 도서를 삭제합니다. 도서와 연관된 모든 도서 아이템도 함께 삭제됩니다(cascade)."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<Void>> delete(@PathVariable Long id) {
        bookService.delete(id);
        ApiSuccessResponse<Void> payload = new ApiSuccessResponse<>(
            GeneralSuccessCode.OK.getCode(),
            "도서가 성공적으로 삭제되었습니다.",
            null
        );
        return ResponseEntity.ok(payload);
    }
}
