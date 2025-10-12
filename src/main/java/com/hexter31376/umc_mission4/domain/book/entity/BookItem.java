package com.hexter31376.umc_mission4.domain.book.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "book_items")
public class BookItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "book_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;

    // 같은 책이어도 출판 종류에 따라 이 번호로 식별 가능
    @Column(name = "isbn")
    private String isbn;

    @Column(name = "price")
    private Double price;

    @Column(name = "stock_qty")
    private Integer stockQty;
}
