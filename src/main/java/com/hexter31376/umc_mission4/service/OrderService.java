package com.hexter31376.umc_mission4.service;

import com.hexter31376.umc_mission4.domain.order.entity.Order;
import com.hexter31376.umc_mission4.domain.order.entity.OrderItem;
import com.hexter31376.umc_mission4.domain.book.entity.BookItem;
import com.hexter31376.umc_mission4.domain.member.entity.Member;
import com.hexter31376.umc_mission4.dto.order.OrderCreateDto;
import com.hexter31376.umc_mission4.dto.order.OrderItemDto;
import com.hexter31376.umc_mission4.dto.order.OrderItemResponseDto;
import com.hexter31376.umc_mission4.dto.order.OrderResponseDto;
import com.hexter31376.umc_mission4.repository.order.OrderRepository;
import com.hexter31376.umc_mission4.repository.book.BookItemRepository;
import com.hexter31376.umc_mission4.repository.member.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final BookItemRepository bookItemRepository;

    public OrderService(OrderRepository orderRepository, MemberRepository memberRepository, BookItemRepository bookItemRepository) {
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
        this.bookItemRepository = bookItemRepository;
    }

    public OrderResponseDto create(OrderCreateDto dto) {
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + dto.getMemberId()));

        // Calculate total price and validate book items
        long totalPrice = 0L;
        List<BookItem> bookItems = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();

        for (OrderItemDto itemDto : dto.getOrderItems()) {
            BookItem bookItem = bookItemRepository.findById(itemDto.getBookItemId())
                    .orElseThrow(() -> new EntityNotFoundException("BookItem not found with id: " + itemDto.getBookItemId()));

            long itemTotal = bookItem.getPrice() * itemDto.getQuantity();
            totalPrice += itemTotal;

            bookItems.add(bookItem);
            quantities.add(itemDto.getQuantity());
        }

        // Create order
        Order order = Order.builder()
                .member(member)
                .totalPrice(totalPrice)
                .build();

        // Add order items
        for (int i = 0; i < bookItems.size(); i++) {
            OrderItem item = OrderItem.builder()
                    .order(order)
                    .bookItem(bookItems.get(i))
                    .quantity(quantities.get(i))
                    .price(bookItems.get(i).getPrice())
                    .build();
            order.addOrderItem(item);
        }

        Order saved = orderRepository.save(order);
        return toResponseDto(saved);
    }

    public OrderResponseDto find(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
        return toResponseDto(order);
    }

    private OrderResponseDto toResponseDto(Order order) {
        List<OrderItemResponseDto> items = new ArrayList<>();
        for (OrderItem item : order.getOrderItems()) {
            items.add(OrderItemResponseDto.builder()
                    .id(item.getId())
                    .bookItemId(item.getBookItem().getId())
                    .price(item.getPrice())
                    .quantity(item.getQuantity())
                    .build());
        }

        return OrderResponseDto.builder()
                .id(order.getId())
                .memberId(order.getMember().getId())
                .totalPrice(order.getTotalPrice())
                .items(items)
                .build();
    }
}
