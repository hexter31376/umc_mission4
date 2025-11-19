package com.hexter31376.umc_mission4.domain.cart.entity;

import com.hexter31376.umc_mission4.domain.member.entity.Member;
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
@Table(name = "carts")
public class Cart extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @JoinColumn(name = "member_id", nullable = false, unique = true)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CartItem> cartItems = new ArrayList<>();

    // Helper method to add CartItem
    public void addCartItem(CartItem cartItem) {
        this.cartItems.add(cartItem);
    }
}