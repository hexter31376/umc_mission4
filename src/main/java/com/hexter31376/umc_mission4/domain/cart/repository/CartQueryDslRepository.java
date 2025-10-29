package com.hexter31376.umc_mission4.domain.cart.repository;

import com.hexter31376.umc_mission4.domain.cart.entity.Cart;
import com.hexter31376.umc_mission4.domain.cart.entity.QCart;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPADeleteClause;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class CartQueryDslRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public CartQueryDslRepository(JPAQueryFactory queryFactory, EntityManager em) {
        this.queryFactory = queryFactory;
        this.em = em;
    }

    @Transactional
    public Cart save(Cart cart) {
        if (cart.getId() == null) {
            em.persist(cart);
            return cart;
        }
        return em.merge(cart);
    }

    public Optional<Cart> findById(Long id) {
        Cart c = queryFactory.selectFrom(QCart.cart).where(QCart.cart.id.eq(id)).fetchOne();
        return Optional.ofNullable(c);
    }

    public Optional<Cart> findByMemberId(Long memberId) {
        Cart c = queryFactory.selectFrom(QCart.cart).where(QCart.cart.member.id.eq(memberId)).fetchOne();
        return Optional.ofNullable(c);
    }

    @Transactional
    public boolean deleteById(Long id) {
        QCart c = QCart.cart;
        long affected = new JPADeleteClause(em, c).where(c.id.eq(id)).execute();
        return affected > 0L;
    }
}
