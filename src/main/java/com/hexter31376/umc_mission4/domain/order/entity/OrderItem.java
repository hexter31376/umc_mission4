package com.hexter31376.umc_mission4.domain.order.entity;

import com.hexter31376.umc_mission4.domain.book.entity.BookItem;
import com.hexter31376.umc_mission4.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Table(name = "order_items")
public class OrderItem extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "price")
    private Long price;

    @Column(nullable = false)
    private Integer quantity;

    @JoinColumn(name = "order_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Order order;

    @JoinColumn(name = "book_item_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private BookItem bookItem;
}
