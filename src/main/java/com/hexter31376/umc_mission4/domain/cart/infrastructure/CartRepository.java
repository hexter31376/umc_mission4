package com.hexter31376.umc_mission4.domain.cart.infrastructure;

import com.hexter31376.umc_mission4.domain.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long>{

    @Query("SELECT c FROM Cart c")
    List<Cart> findAllByJpqlCustom();

    @Query("SELECT DISTINCT c FROM Cart c LEFT JOIN FETCH c.member LEFT JOIN FETCH c.cartItems")
    List<Cart> findAllWithMemberAndItems();

    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.member LEFT JOIN FETCH c.cartItems WHERE c.id = :id")
    Optional<Cart> findByIdWithMemberAndItems(@Param("id") Long id);

    @Query("SELECT c FROM Cart c WHERE c.member.id = :memberId")
    Optional<Cart> findByMemberIdJpql(@Param("memberId") Long memberId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Cart c WHERE c.id = :id")
    void deleteByIdByJpql(@Param("id") Long id);
}
