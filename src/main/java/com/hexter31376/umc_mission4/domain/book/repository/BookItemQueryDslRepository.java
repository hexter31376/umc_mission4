package com.hexter31376.umc_mission4.domain.book.repository;

import com.hexter31376.umc_mission4.domain.book.entity.BookItem;
import com.hexter31376.umc_mission4.domain.book.entity.QBookItem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class BookItemQueryDslRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public BookItemQueryDslRepository(JPAQueryFactory queryFactory, EntityManager em) {
        this.queryFactory = queryFactory;
        this.em = em;
    }

    @Transactional
    public BookItem save(BookItem item) {
        if (item.getId() == null) {
            em.persist(item);
            return item;
        }
        return em.merge(item);
    }

    public Optional<BookItem> findById(Long id) {
        BookItem item = queryFactory.selectFrom(QBookItem.bookItem)
                .where(QBookItem.bookItem.id.eq(id))
                .fetchOne();
        return Optional.ofNullable(item);
    }

    public Page<BookItem> findAll(Pageable pageable) {
        QBookItem bi = QBookItem.bookItem;
        List<BookItem> content = queryFactory.selectFrom(bi)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long total = queryFactory.select(bi.count()).from(bi).fetchOne();
        long totalCount = total != null ? total : 0L;
        return new PageImpl<>(content, pageable, totalCount);
    }

    public Page<BookItem> search(String isbn, Long minPrice, Long maxPrice, Pageable pageable) {
        QBookItem bi = QBookItem.bookItem;
        com.querydsl.core.BooleanBuilder where = new com.querydsl.core.BooleanBuilder();
        if (isbn != null && !isbn.isBlank()) where.and(bi.isbn.containsIgnoreCase(isbn));
        if (minPrice != null) where.and(bi.price.goe(minPrice));
        if (maxPrice != null) where.and(bi.price.loe(maxPrice));

        List<BookItem> content = queryFactory.selectFrom(bi)
                .where(where)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long total = queryFactory.select(bi.count()).from(bi).where(where).fetchOne();
        long totalCount = total != null ? total : 0L;
        return new PageImpl<>(content, pageable, totalCount);
    }

    @Transactional
    public boolean deleteById(Long id) {
        BookItem existing = em.find(BookItem.class, id);
        if (existing == null) return false;
        em.remove(existing);
        return true;
    }
}
