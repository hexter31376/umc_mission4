package com.hexter31376.umc_mission4.controller;

import com.hexter31376.umc_mission4.dto.book.BookCreateDto;
import com.hexter31376.umc_mission4.dto.book.BookResponseDto;
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
    public ResponseEntity<BookResponseDto> create(@Valid @RequestBody BookCreateDto dto) {
        return ResponseEntity.ok(bookService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.find(id));
    }
}

