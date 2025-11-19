package com.hexter31376.umc_mission4.service;

import com.hexter31376.umc_mission4.domain.book.entity.Book;
import com.hexter31376.umc_mission4.domain.book.entity.BookItem;
import com.hexter31376.umc_mission4.dto.book.BookCreateDto;
import com.hexter31376.umc_mission4.dto.book.BookItemDto;
import com.hexter31376.umc_mission4.dto.book.BookItemResponseDto;
import com.hexter31376.umc_mission4.dto.book.BookResponseDto;
import com.hexter31376.umc_mission4.repository.book.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.stream.Collectors;

@Service
@Transactional
@Validated
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookResponseDto create(BookCreateDto dto) {
        Book book = Book.builder()
                .title(dto.getTitle())
                .author(dto.getAuthor() == null ? "none" : dto.getAuthor())
                .description(dto.getDescription() == null ? "설명이 없습니다." : dto.getDescription())
                .build();

        // Add BookItems
        for (BookItemDto itemDto : dto.getBookItems()) {
            BookItem bookItem = BookItem.builder()
                    .isbn(itemDto.getIsbn())
                    .price(itemDto.getPrice())
                    .quantity(itemDto.getQuantity())
                    .book(book)
                    .build();
            book.addBookItem(bookItem);
        }

        Book saved = bookRepository.save(book);
        return toResponseDto(saved);
    }

    public BookResponseDto find(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));
        return toResponseDto(book);
    }

    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    private BookResponseDto toResponseDto(Book book) {
        return BookResponseDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .description(book.getDescription())
                .bookItems(book.getBookItems().stream()
                        .map(item -> BookItemResponseDto.builder()
                                .id(item.getId())
                                .isbn(item.getIsbn())
                                .price(item.getPrice())
                                .quantity(item.getQuantity())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
