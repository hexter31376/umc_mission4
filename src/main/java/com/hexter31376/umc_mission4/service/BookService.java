package com.hexter31376.umc_mission4.service;

import com.hexter31376.umc_mission4.domain.book.entity.Book;
import com.hexter31376.umc_mission4.dto.book.BookCreateDto;
import com.hexter31376.umc_mission4.dto.book.BookResponseDto;
import com.hexter31376.umc_mission4.repository.book.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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
        Book saved = bookRepository.save(book);
        return BookResponseDto.builder().id(saved.getId()).title(saved.getTitle()).author(saved.getAuthor()).description(saved.getDescription()).build();
    }

    public BookResponseDto find(Long id) {
        Book b = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book not found"));
        return BookResponseDto.builder().id(b.getId()).title(b.getTitle()).author(b.getAuthor()).description(b.getDescription()).build();
    }
}

