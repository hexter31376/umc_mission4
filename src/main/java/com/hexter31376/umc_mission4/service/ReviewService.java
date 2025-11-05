package com.hexter31376.umc_mission4.service;

import com.hexter31376.umc_mission4.domain.book.entity.Book;
import com.hexter31376.umc_mission4.domain.book.entity.Review;
import com.hexter31376.umc_mission4.domain.member.entity.Member;
import com.hexter31376.umc_mission4.dto.review.ReviewCreateDto;
import com.hexter31376.umc_mission4.dto.review.ReviewResponseDto;
import com.hexter31376.umc_mission4.repository.book.BookRepository;
import com.hexter31376.umc_mission4.repository.book.ReviewRepository;
import com.hexter31376.umc_mission4.repository.member.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public ReviewService(ReviewRepository reviewRepository, BookRepository bookRepository, MemberRepository memberRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    public ReviewResponseDto create(ReviewCreateDto dto) {
        Book b = bookRepository.findById(dto.getBookId()).orElseThrow(() -> new EntityNotFoundException("Book not found"));
        Member m = memberRepository.findById(dto.getMemberId()).orElseThrow(() -> new EntityNotFoundException("Member not found"));
        Review r = Review.builder().book(b).member(m).rating(dto.getRating()).content(dto.getContent()).build();
        Review saved = reviewRepository.save(r);
        return ReviewResponseDto.builder().id(saved.getId()).bookId(b.getId()).memberId(m.getId()).rating(saved.getRating()).content(saved.getContent()).build();
    }
}
