package com.hexter31376.umc_mission4.domain.order.infrastructure;

import com.hexter31376.umc_mission4.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>{

    @Query("SELECT o FROM Order o")
    List<Order> findAllByJpqlCustom();

    // member, orderItems 를 한 번에 fetch join (중복 제거 위해 DISTINCT)
    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.member LEFT JOIN FETCH o.orderItems")
    List<Order> findAllWithMemberAndItems();

    // 단일 조회시 연관 엔티티까지 fetch
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.member LEFT JOIN FETCH o.orderItems WHERE o.id = :id")
    Optional<Order> findByIdWithMemberAndItems(@Param("id") Long id);

    // 회원 기준 조회 (한 회원의 주문들)
    @Query("SELECT o FROM Order o WHERE o.member.id = :memberId")
    List<Order> findByMemberIdJpql(@Param("memberId") Long memberId);

    // 총액 기준 검색 (예: 최소 총액)
    @Query("SELECT o FROM Order o WHERE o.totalPrice >= :minTotalPrice")
    List<Order> findByTotalPriceGreaterThanEqualJpql(@Param("minTotalPrice") Long minTotalPrice);

    // JPQL 삭제
    @Modifying
    @Transactional
    @Query("DELETE FROM Order o WHERE o.id = :id")
    void deleteByIdByJpql(@Param("id") Long id);
}
