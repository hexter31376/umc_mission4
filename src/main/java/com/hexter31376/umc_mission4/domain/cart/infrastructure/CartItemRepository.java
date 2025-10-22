package com.hexter31376.umc_mission4.domain.cart.infrastructure;

import com.hexter31376.umc_mission4.domain.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("SELECT DISTINCT ci FROM CartItem ci LEFT JOIN FETCH ci.cart LEFT JOIN FETCH ci.bookItem bi LEFT JOIN FETCH bi.book")
    List<CartItem> findAllWithCartAndBookItem();

    @Query("SELECT ci FROM CartItem ci LEFT JOIN FETCH ci.cart LEFT JOIN FETCH ci.bookItem bi LEFT JOIN FETCH bi.book WHERE ci.id = :id")
    Optional<CartItem> findByIdWithCartAndBookItem(@Param("id") Long id);

    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.id = :cartId")
    List<CartItem> findByCartId(@Param("cartId") Long cartId);

    @Query("SELECT ci FROM CartItem ci WHERE ci.bookItem.id = :bookItemId")
    List<CartItem> findByBookItemId(@Param("bookItemId") Long bookItemId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem ci WHERE ci.id = :id")
    void deleteByIdByJpql(@Param("id") Long id);
}