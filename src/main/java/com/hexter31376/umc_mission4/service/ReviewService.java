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
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@Validated
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
        Book b = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + dto.getBookId()));
        Member m = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + dto.getMemberId()));

        Review r = Review.builder()
                .book(b)
                .member(m)
                .rating(dto.getRating())
                .content(dto.getContent())
                .build();

        Review saved = reviewRepository.save(r);
        return toResponseDto(saved);
    }

    public ReviewResponseDto find(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + id));
        return toResponseDto(review);
    }

    public ReviewResponseDto update(Long id, Integer rating, String content) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + id));

        review.updateRatingAndContent(rating, content);
        return toResponseDto(review);
    }

    public void delete(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new EntityNotFoundException("Review not found with id: " + id);
        }
        reviewRepository.deleteById(id);
    }

    private ReviewResponseDto toResponseDto(Review review) {
        return ReviewResponseDto.builder()
                .id(review.getId())
                .bookId(review.getBook().getId())
                .memberId(review.getMember().getId())
                .rating(review.getRating())
                .content(review.getContent())
                .build();
    }
}
