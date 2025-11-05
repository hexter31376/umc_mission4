package com.hexter31376.umc_mission4.repository.order;

import com.hexter31376.umc_mission4.domain.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}

