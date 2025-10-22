package com.hexter31376.umc_mission4.domain.book.infrastructure;

import com.hexter31376.umc_mission4.domain.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long>{

    // 기본 모든 엔티티 조회 (간단한 JPQL)
    @Query("SELECT b FROM Book b")
    List<Book> findAllByJpqlCustom();

    // bookItems, reviews 를 한 번에 가져오는 fetch join (중복 제거를 위해 distinct 사용)
    @Query("SELECT DISTINCT b FROM Book b LEFT JOIN FETCH b.bookItems LEFT JOIN FETCH b.reviews")
    List<Book> findAllWithItemsAndReviews();

    // 단일 조회시 연관 엔티티까지 fetch
    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.bookItems LEFT JOIN FETCH b.reviews WHERE b.id = :id")
    Optional<Book> findByIdWithItemsAndReviews(@Param("id") Long id);

    // 제목 부분 검색 JPQL
    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Book> findByTitleContainingJpql(@Param("title") String title);

    // JPQL로 삭제 (여기서는 id 필드명이 'id'라 가정)
    @Modifying
    @Transactional
    @Query("DELETE FROM Book b WHERE b.id = :id")
    void deleteByIdByJpql(@Param("id") Long id);
}
