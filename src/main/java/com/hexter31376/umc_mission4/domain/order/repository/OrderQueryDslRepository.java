package com.hexter31376.umc_mission4.domain.order.repository;

import com.hexter31376.umc_mission4.domain.order.entity.Order;
import com.hexter31376.umc_mission4.domain.order.entity.QOrder;
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
public class OrderQueryDslRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public OrderQueryDslRepository(JPAQueryFactory queryFactory, EntityManager em) {
        this.queryFactory = queryFactory;
        this.em = em;
    }

    @Transactional
    public Order save(Order order) {
        if (order.getId() == null) {
            em.persist(order);
            return order;
        }
        return em.merge(order);
    }

    public Optional<Order> findById(Long id) {
        Order o = queryFactory.selectFrom(QOrder.order).where(QOrder.order.id.eq(id)).fetchOne();
        return Optional.ofNullable(o);
    }

    public Page<Order> findAll(Pageable pageable) {
        QOrder o = QOrder.order;
        List<Order> content = queryFactory.selectFrom(o)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long total = queryFactory.select(o.count()).from(o).fetchOne();
        long totalCount = total != null ? total : 0L;
        return new PageImpl<>(content, pageable, totalCount);
    }

    public List<Order> findByMemberId(Long memberId) {
        QOrder o = QOrder.order;
        return queryFactory.selectFrom(o).where(o.member.id.eq(memberId)).fetch();
    }

    @Transactional
    public boolean deleteById(Long id) {
        QOrder o = QOrder.order;
        long affected = new JPADeleteClause(em, o).where(o.id.eq(id)).execute();
        return affected > 0L;
    }
}
