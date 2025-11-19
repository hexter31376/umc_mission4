package com.hexter31376.umc_mission4.service;

import com.hexter31376.umc_mission4.domain.book.entity.Book;
import com.hexter31376.umc_mission4.domain.book.entity.BookItem;
import com.hexter31376.umc_mission4.dto.book.BookItemCreateDto;
import com.hexter31376.umc_mission4.dto.book.BookItemResponseDto;
import com.hexter31376.umc_mission4.repository.book.BookItemRepository;
import com.hexter31376.umc_mission4.repository.book.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BookItemService {
    private final BookItemRepository bookItemRepository;
    private final BookRepository bookRepository;

    public BookItemService(BookItemRepository bookItemRepository, BookRepository bookRepository) {
        this.bookItemRepository = bookItemRepository;
        this.bookRepository = bookRepository;
    }

    public BookItemResponseDto create(BookItemCreateDto dto) {
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + dto.getBookId()));

        BookItem bookItem = BookItem.builder()
                .book(book)
                .isbn(dto.getIsbn())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .build();

        BookItem saved = bookItemRepository.save(bookItem);
        return toResponseDto(saved);
    }

    public BookItemResponseDto find(Long id) {
        BookItem bookItem = bookItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("BookItem not found with id: " + id));
        return toResponseDto(bookItem);
    }

    public BookItemResponseDto update(Long id, Long price, Integer quantity) {
        BookItem bookItem = bookItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("BookItem not found with id: " + id));

        bookItem.updatePriceAndQuantity(price, quantity);
        return toResponseDto(bookItem);
    }

    public void delete(Long id) {
        BookItem bookItem = bookItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("BookItem not found with id: " + id));

        Long bookId = bookItem.getBook().getId();
        bookItemRepository.deleteById(id);

        // BookItem 삭제 후 Book을 다시 조회하여 BookItem이 없으면 Book도 삭제
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book != null && bookItemRepository.countByBookId(bookId) == 0) {
            bookRepository.delete(book);
        }
    }

    private BookItemResponseDto toResponseDto(BookItem bookItem) {
        return BookItemResponseDto.builder()
                .id(bookItem.getId())
                .isbn(bookItem.getIsbn())
                .price(bookItem.getPrice())
                .quantity(bookItem.getQuantity())
                .build();
    }
}

