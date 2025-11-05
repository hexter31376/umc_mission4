package com.hexter31376.umc_mission4.repository.order;

import com.hexter31376.umc_mission4.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

