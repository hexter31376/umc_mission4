package com.hexter31376.umc_mission4.domain.order.repository;

import com.hexter31376.umc_mission4.domain.order.entity.OrderItem;
import com.hexter31376.umc_mission4.domain.order.entity.QOrderItem;
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
public class OrderItemQueryDslRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public OrderItemQueryDslRepository(JPAQueryFactory queryFactory, EntityManager em) {
        this.queryFactory = queryFactory;
        this.em = em;
    }

    @Transactional
    public OrderItem save(OrderItem item) {
        if (item.getId() == null) {
            em.persist(item);
            return item;
        }
        return em.merge(item);
    }

    public Optional<OrderItem> findById(Long id) {
        OrderItem oi = queryFactory.selectFrom(QOrderItem.orderItem).where(QOrderItem.orderItem.id.eq(id)).fetchOne();
        return Optional.ofNullable(oi);
    }

    public Page<OrderItem> findAll(Pageable pageable) {
        QOrderItem oi = QOrderItem.orderItem;
        List<OrderItem> content = queryFactory.selectFrom(oi)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long total = queryFactory.select(oi.count()).from(oi).fetchOne();
        long totalCount = total != null ? total : 0L;
        return new PageImpl<>(content, pageable, totalCount);
    }

    public Page<OrderItem> search(Long orderId, Long bookItemId, Pageable pageable) {
        QOrderItem oi = QOrderItem.orderItem;
        com.querydsl.core.BooleanBuilder where = new com.querydsl.core.BooleanBuilder();
        if (orderId != null) where.and(oi.order.id.eq(orderId));
        if (bookItemId != null) where.and(oi.bookItem.id.eq(bookItemId));

        List<OrderItem> content = queryFactory.selectFrom(oi)
                .where(where)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long total = queryFactory.select(oi.count()).from(oi).where(where).fetchOne();
        long totalCount = total != null ? total : 0L;
        return new PageImpl<>(content, pageable, totalCount);
    }

    @Transactional
    public boolean deleteById(Long id) {
        OrderItem existing = em.find(OrderItem.class, id);
        if (existing == null) return false;
        em.remove(existing);
        return true;
    }
}
