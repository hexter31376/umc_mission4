package com.hexter31376.umc_mission4.repository.cart;

import com.hexter31376.umc_mission4.domain.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}

