package com.hexter31376.umc_mission4.domain.book.repository;

import com.hexter31376.umc_mission4.domain.book.entity.Book;
import com.hexter31376.umc_mission4.domain.book.entity.QBook;
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
public class BookQueryDslRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public BookQueryDslRepository(JPAQueryFactory queryFactory, EntityManager em) {
        this.queryFactory = queryFactory;
        this.em = em;
    }

    @Transactional
    public Book save(Book book) {
        if (book.getId() == null) {
            em.persist(book);
            return book;
        }
        return em.merge(book);
    }

    public Optional<Book> findById(Long id) {
        Book book = queryFactory.selectFrom(QBook.book)
                .where(QBook.book.id.eq(id))
                .fetchOne();
        return Optional.ofNullable(book);
    }

    public Page<Book> findAll(Pageable pageable) {
        QBook b = QBook.book;
        List<Book> content = queryFactory.selectFrom(b)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long total = queryFactory.select(b.count()).from(b).fetchOne();
        long totalCount = total != null ? total : 0L;
        return new PageImpl<>(content, pageable, totalCount);
    }

    public Page<Book> search(String title, String author, String description, Pageable pageable) {
        QBook b = QBook.book;
        com.querydsl.core.BooleanBuilder where = new com.querydsl.core.BooleanBuilder();
        if (title != null && !title.isBlank()) where.and(b.title.containsIgnoreCase(title));
        if (author != null && !author.isBlank()) where.and(b.author.containsIgnoreCase(author));
        if (description != null && !description.isBlank()) where.and(b.description.containsIgnoreCase(description));

        List<Book> content = queryFactory.selectFrom(b)
                .where(where)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long total = queryFactory.select(b.count()).from(b).where(where).fetchOne();
        long totalCount = total != null ? total : 0L;
        return new PageImpl<>(content, pageable, totalCount);
    }

    @Transactional
    public boolean deleteById(Long id) {
        Book existing = em.find(Book.class, id);
        if (existing == null) return false;
        em.remove(existing);
        return true;
    }
}
