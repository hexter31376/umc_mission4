package com.hexter31376.umc_mission4.domain.book.repository;

import com.hexter31376.umc_mission4.domain.book.entity.Review;
import com.hexter31376.umc_mission4.domain.book.entity.QReview;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPADeleteClause;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class ReviewQueryDslRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public ReviewQueryDslRepository(JPAQueryFactory queryFactory, EntityManager em) {
        this.queryFactory = queryFactory;
        this.em = em;
    }

    @Transactional
    public Review save(Review review) {
        if (review.getId() == null) {
            em.persist(review);
            return review;
        }
        return em.merge(review);
    }

    public Optional<Review> findById(Long id) {
        Review r = queryFactory.selectFrom(QReview.review).where(QReview.review.id.eq(id)).fetchOne();
        return Optional.ofNullable(r);
    }

    public Page<Review> findAll(Pageable pageable) {
        QReview r = QReview.review;
        List<Review> content = queryFactory.selectFrom(r)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long total = queryFactory.select(r.count()).from(r).fetchOne();
        long totalCount = total != null ? total : 0L;
        return new PageImpl<>(content, pageable, totalCount);
    }

    public Page<Review> search(Integer minRating, Integer maxRating, Long bookId, Long memberId, Pageable pageable) {
        QReview r = QReview.review;
        com.querydsl.core.BooleanBuilder where = new com.querydsl.core.BooleanBuilder();
        if (minRating != null) where.and(r.rating.goe(minRating));
        if (maxRating != null) where.and(r.rating.loe(maxRating));
        if (bookId != null) where.and(r.book.id.eq(bookId));
        if (memberId != null) where.and(r.member.id.eq(memberId));

        List<Review> content = queryFactory.selectFrom(r)
                .where(where)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long total = queryFactory.select(r.count()).from(r).where(where).fetchOne();
        long totalCount = total != null ? total : 0L;
        return new PageImpl<>(content, pageable, totalCount);
    }

    @Transactional
    public boolean deleteById(Long id) {
        QReview r = QReview.review;
        long affected = new JPADeleteClause(em, r).where(r.id.eq(id)).execute();
        return affected > 0L;
    }
}
