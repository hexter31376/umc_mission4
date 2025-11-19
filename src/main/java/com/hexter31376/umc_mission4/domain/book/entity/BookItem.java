package com.hexter31376.umc_mission4.domain.book.entity;

import com.hexter31376.umc_mission4.domain.order.entity.OrderItem;
import com.hexter31376.umc_mission4.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Table(name = "book_items")
public class BookItem extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 32)
    @Column(nullable = false, length = 32)
    private String isbn;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false, name = "price")
    private Long price;

    @Builder.Default
    @NotNull
    @Min(0)
    @Column(nullable = false)
    private Integer quantity = 0;

    @NotNull
    @JoinColumn(name = "book_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Book book;

    @Builder.Default
    @OneToMany(mappedBy = "bookItem", fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    // Update methods
    public void updatePrice(Long price) {
        this.price = price;
    }

    public void updateQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void updatePriceAndQuantity(Long price, Integer quantity) {
        this.price = price;
        this.quantity = quantity;
    }
}