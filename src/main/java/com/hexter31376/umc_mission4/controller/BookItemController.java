package com.hexter31376.umc_mission4.controller;

import com.hexter31376.umc_mission4.dto.book.BookItemCreateDto;
import com.hexter31376.umc_mission4.dto.book.BookItemResponseDto;
import com.hexter31376.umc_mission4.dto.book.BookItemUpdateDto;
import com.hexter31376.umc_mission4.global.apiPayload.ApiSuccessResponse;
import com.hexter31376.umc_mission4.global.apiPayload.code.GeneralSuccessCode;
import com.hexter31376.umc_mission4.service.BookItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/book-items")
@Tag(name = "도서 아이템 API", description = "도서 재고 단위(전자책, 종이책, 출판연도별 등) 관리 API")
public class BookItemController {
    private final BookItemService bookItemService;

    public BookItemController(BookItemService bookItemService) {
        this.bookItemService = bookItemService;
    }

    @Operation(
        summary = "도서 아이템 생성",
        description = "기존 도서에 새로운 판매 단위를 추가합니다. 예: 전자책 버전, 종이책 버전, 다른 출판연도 등. " +
                     "도서 ID, ISBN, 가격, 수량을 전송해야 합니다."
    )
    @PostMapping
    public ResponseEntity<ApiSuccessResponse<BookItemResponseDto>> create(@Valid @RequestBody BookItemCreateDto dto) {
        BookItemResponseDto created = bookItemService.create(dto);
        ApiSuccessResponse<BookItemResponseDto> payload = new ApiSuccessResponse<>(
            GeneralSuccessCode.CREATED.getCode(),
            GeneralSuccessCode.CREATED.getMessage(),
            created
        );
        return ResponseEntity.status(GeneralSuccessCode.CREATED.getStatus()).body(payload);
    }

    @Operation(
        summary = "도서 아이템 조회",
        description = "도서 아이템 ID로 특정 판매 단위의 상세 정보를 조회합니다."
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<BookItemResponseDto>> get(@PathVariable Long id) {
        BookItemResponseDto dto = bookItemService.find(id);
        ApiSuccessResponse<BookItemResponseDto> payload = new ApiSuccessResponse<>(
            GeneralSuccessCode.OK.getCode(),
            GeneralSuccessCode.OK.getMessage(),
            dto
        );
        return ResponseEntity.ok(payload);
    }

    @Operation(
        summary = "도서 아이템 수정",
        description = "도서 아이템의 가격과 재고 수량을 수정합니다. ISBN과 연결된 도서는 변경할 수 없습니다."
    )
    @PutMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<BookItemResponseDto>> update(
            @PathVariable Long id,
            @Valid @RequestBody BookItemUpdateDto dto) {
        BookItemResponseDto updated = bookItemService.update(id, dto.getPrice(), dto.getQuantity());
        ApiSuccessResponse<BookItemResponseDto> payload = new ApiSuccessResponse<>(
            GeneralSuccessCode.OK.getCode(),
            "도서 아이템이 성공적으로 수정되었습니다.",
            updated
        );
        return ResponseEntity.ok(payload);
    }

    @Operation(
        summary = "도서 아이템 삭제",
        description = "도서 아이템을 삭제합니다. 만약 해당 도서(Book)가 더 이상 판매 단위를 가지지 않게 되면, " +
                     "도서 자체도 자동으로 삭제됩니다. (전자책과 종이책이 모두 품절되어 삭제된 경우 등)"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<Void>> delete(@PathVariable Long id) {
        bookItemService.delete(id);
        ApiSuccessResponse<Void> payload = new ApiSuccessResponse<>(
            GeneralSuccessCode.OK.getCode(),
            "도서 아이템이 성공적으로 삭제되었습니다.",
            null
        );
        return ResponseEntity.ok(payload);
    }
}

