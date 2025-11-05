package com.hexter31376.umc_mission4.service;

import com.hexter31376.umc_mission4.domain.cart.entity.CartItem;
import com.hexter31376.umc_mission4.domain.order.entity.Order;
import com.hexter31376.umc_mission4.domain.order.entity.OrderItem;
import com.hexter31376.umc_mission4.domain.book.entity.BookItem;
import com.hexter31376.umc_mission4.domain.member.entity.Member;
import com.hexter31376.umc_mission4.dto.order.OrderCreateDto;
import com.hexter31376.umc_mission4.dto.order.OrderItemResponseDto;
import com.hexter31376.umc_mission4.dto.order.OrderResponseDto;
import com.hexter31376.umc_mission4.repository.cart.CartItemRepository;
import com.hexter31376.umc_mission4.repository.order.OrderItemRepository;
import com.hexter31376.umc_mission4.repository.order.OrderRepository;
import com.hexter31376.umc_mission4.repository.book.BookItemRepository;
import com.hexter31376.umc_mission4.repository.member.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final MemberRepository memberRepository;
    private final BookItemRepository bookItemRepository;
    private final CartItemRepository cartItemRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, MemberRepository memberRepository, BookItemRepository bookItemRepository, CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.memberRepository = memberRepository;
        this.bookItemRepository = bookItemRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public OrderResponseDto orderDirect(OrderCreateDto dto) {
        Member member = memberRepository.findById(dto.getMemberId()).orElseThrow(() -> new EntityNotFoundException("Member not found"));
        BookItem bookItem = bookItemRepository.findById(dto.getBookItemId()).orElseThrow(() -> new EntityNotFoundException("BookItem not found"));
        int quantity = dto.getQuantity() == null ? 1 : dto.getQuantity();

        Order order = Order.builder().member(member).totalPrice(bookItem.getPrice() * (long) quantity).build();
        orderRepository.save(order);

        OrderItem oi = OrderItem.builder().order(order).bookItem(bookItem).price(bookItem.getPrice()).quantity(quantity).build();
        OrderItem savedItem = orderItemRepository.save(oi);

        OrderResponseDto response = OrderResponseDto.builder()
                .id(order.getId())
                .memberId(member.getId())
                .totalPrice(order.getTotalPrice())
                .items(List.of(OrderItemResponseDto.builder().id(savedItem.getId()).bookItemId(bookItem.getId()).price(savedItem.getPrice()).quantity(savedItem.getQuantity()).build()))
                .build();
        return response;
    }

    public OrderResponseDto orderFromCart(OrderCreateDto dto) {
        Member member = memberRepository.findById(dto.getMemberId()).orElseThrow(() -> new EntityNotFoundException("Member not found"));
        List<Long> cartIds = dto.getCartItemIds();
        if (cartIds == null || cartIds.isEmpty()) throw new IllegalArgumentException("cartItemIds required for cart ordering");

        List<CartItem> cartItems = cartItemRepository.findAllById(cartIds);
        if (cartItems.isEmpty()) throw new IllegalArgumentException("No cart items found");

        long total = 0L;
        for (CartItem ci : cartItems) {
            BookItem bi = ci.getBookItem();
            total += bi.getPrice() * ci.getQuantity();
        }

        Order order = Order.builder().member(member).totalPrice(total).build();
        orderRepository.save(order);

        List<OrderItemResponseDto> createdItems = new ArrayList<>();
        for (CartItem ci : cartItems) {
            BookItem bi = ci.getBookItem();
            OrderItem oi = OrderItem.builder().order(order).bookItem(bi).price(bi.getPrice()).quantity(ci.getQuantity()).build();
            OrderItem saved = orderItemRepository.save(oi);
            createdItems.add(OrderItemResponseDto.builder().id(saved.getId()).bookItemId(bi.getId()).price(saved.getPrice()).quantity(saved.getQuantity()).build());
            // remove cart item
            cartItemRepository.delete(ci);
        }

        OrderResponseDto response = OrderResponseDto.builder()
                .id(order.getId())
                .memberId(member.getId())
                .totalPrice(order.getTotalPrice())
                .items(createdItems)
                .build();
        return response;
    }
}
