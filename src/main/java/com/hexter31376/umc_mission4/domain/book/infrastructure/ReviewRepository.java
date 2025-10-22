package com.hexter31376.umc_mission4.domain.book.infrastructure;

import com.hexter31376.umc_mission4.domain.book.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>{

    @Query("SELECT r FROM Review r")
    List<Review> findAllByJpqlCustom();

    @Query("SELECT DISTINCT r FROM Review r LEFT JOIN FETCH r.book LEFT JOIN FETCH r.member")
    List<Review> findAllWithBookAndMember();

    @Query("SELECT r FROM Review r LEFT JOIN FETCH r.book LEFT JOIN FETCH r.member WHERE r.id = :id")
    Optional<Review> findByIdWithBookAndMember(@Param("id") Long id);

    @Query("SELECT r FROM Review r WHERE r.book.id = :bookId")
    List<Review> findByBookIdJpql(@Param("bookId") Long bookId);

    @Query("SELECT r FROM Review r WHERE r.member.id = :memberId")
    List<Review> findByMemberIdJpql(@Param("memberId") Long memberId);

    @Query("SELECT r FROM Review r WHERE r.rating >= :minRating")
    List<Review> findByRatingGreaterThanEqualJpql(@Param("minRating") Integer minRating);

    @Modifying
    @Transactional
    @Query("DELETE FROM Review r WHERE r.id = :id")
    void deleteByIdByJpql(@Param("id") Long id);
}
