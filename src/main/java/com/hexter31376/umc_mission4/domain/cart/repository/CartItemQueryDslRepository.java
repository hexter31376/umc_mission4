package com.hexter31376.umc_mission4.domain.cart.repository;

import com.hexter31376.umc_mission4.domain.cart.entity.CartItem;
import com.hexter31376.umc_mission4.domain.cart.entity.QCartItem;
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
public class CartItemQueryDslRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public CartItemQueryDslRepository(JPAQueryFactory queryFactory, EntityManager em) {
        this.queryFactory = queryFactory;
        this.em = em;
    }

    @Transactional
    public CartItem save(CartItem item) {
        if (item.getId() == null) {
            em.persist(item);
            return item;
        }
        return em.merge(item);
    }

    public Optional<CartItem> findById(Long id) {
        CartItem ci = queryFactory.selectFrom(QCartItem.cartItem).where(QCartItem.cartItem.id.eq(id)).fetchOne();
        return Optional.ofNullable(ci);
    }

    public Page<CartItem> findAll(Pageable pageable) {
        QCartItem ci = QCartItem.cartItem;
        List<CartItem> content = queryFactory.selectFrom(ci)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long total = queryFactory.select(ci.count()).from(ci).fetchOne();
        long totalCount = total != null ? total : 0L;
        return new PageImpl<>(content, pageable, totalCount);
    }

    public Page<CartItem> search(Long cartId, Long bookItemId, Pageable pageable) {
        QCartItem ci = QCartItem.cartItem;
        com.querydsl.core.BooleanBuilder where = new com.querydsl.core.BooleanBuilder();
        if (cartId != null) where.and(ci.cart.id.eq(cartId));
        if (bookItemId != null) where.and(ci.bookItem.id.eq(bookItemId));

        List<CartItem> content = queryFactory.selectFrom(ci)
                .where(where)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long total = queryFactory.select(ci.count()).from(ci).where(where).fetchOne();
        long totalCount = total != null ? total : 0L;
        return new PageImpl<>(content, pageable, totalCount);
    }

    @Transactional
    public boolean deleteById(Long id) {
        QCartItem ci = QCartItem.cartItem;
        long affected = new JPADeleteClause(em, ci).where(ci.id.eq(id)).execute();
        return affected > 0L;
    }
}
