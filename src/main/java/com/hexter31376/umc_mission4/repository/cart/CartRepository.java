package com.hexter31376.umc_mission4.repository.cart;

import com.hexter31376.umc_mission4.domain.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}

