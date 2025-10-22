package com.hexter31376.umc_mission4.domain.order.infrastructure;

import com.hexter31376.umc_mission4.domain.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

    @Query("SELECT oi FROM OrderItem oi")
    List<OrderItem> findAllByJpqlCustom();

    @Query("SELECT DISTINCT oi FROM OrderItem oi LEFT JOIN FETCH oi.order LEFT JOIN FETCH oi.bookItem bi LEFT JOIN FETCH bi.book")
    List<OrderItem> findAllWithOrderAndBookItem();

    @Query("SELECT oi FROM OrderItem oi LEFT JOIN FETCH oi.order LEFT JOIN FETCH oi.bookItem bi LEFT JOIN FETCH bi.book WHERE oi.id = :id")
    Optional<OrderItem> findByIdWithOrderAndBookItem(@Param("id") Long id);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.id = :orderId")
    List<OrderItem> findByOrderIdJpql(@Param("orderId") Long orderId);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.bookItem.id = :bookItemId")
    List<OrderItem> findByBookItemIdJpql(@Param("bookItemId") Long bookItemId);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.price >= :minPrice")
    List<OrderItem> findByPriceGreaterThanEqualJpql(@Param("minPrice") Long minPrice);

    @Modifying
    @Transactional
    @Query("DELETE FROM OrderItem oi WHERE oi.id = :id")
    void deleteByIdByJpql(@Param("id") Long id);
}
