package com.hexter31376.umc_mission4.service;

import com.hexter31376.umc_mission4.domain.cart.entity.Cart;
import com.hexter31376.umc_mission4.domain.cart.entity.CartItem;
import com.hexter31376.umc_mission4.domain.book.entity.BookItem;
import com.hexter31376.umc_mission4.domain.member.entity.Member;
import com.hexter31376.umc_mission4.dto.cart.CartAddItemDto;
import com.hexter31376.umc_mission4.dto.cart.CartItemResponseDto;
import com.hexter31376.umc_mission4.repository.cart.CartItemRepository;
import com.hexter31376.umc_mission4.repository.cart.CartRepository;
import com.hexter31376.umc_mission4.repository.book.BookItemRepository;
import com.hexter31376.umc_mission4.repository.member.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;
    private final BookItemRepository bookItemRepository;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, MemberRepository memberRepository, BookItemRepository bookItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.memberRepository = memberRepository;
        this.bookItemRepository = bookItemRepository;
    }

    public CartItemResponseDto addItem(CartAddItemDto dto) {
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + dto.getMemberId()));
        BookItem bookItem = bookItemRepository.findById(dto.getBookItemId())
                .orElseThrow(() -> new EntityNotFoundException("BookItem not found with id: " + dto.getBookItemId()));

        // Get or create cart
        Cart cart = cartRepository.findByMemberId(dto.getMemberId()).orElse(null);
        if (cart == null) {
            cart = Cart.builder().member(member).build();
            cart = cartRepository.save(cart);
        }

        long totalPrice = bookItem.getPrice() * dto.getQuantity();

        CartItem cartItem = CartItem.builder()
                .cart(cart)
                .bookItem(bookItem)
                .quantity(dto.getQuantity())
                .totalPrice(totalPrice)
                .build();

        cart.addCartItem(cartItem);
        CartItem saved = cartItemRepository.save(cartItem);

        return CartItemResponseDto.builder()
                .id(saved.getId())
                .bookItemId(saved.getBookItem().getId())
                .quantity(saved.getQuantity())
                .totalPrice(saved.getTotalPrice())
                .build();
    }

    public CartItemResponseDto findCartItem(Long id) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CartItem not found with id: " + id));

        return CartItemResponseDto.builder()
                .id(cartItem.getId())
                .bookItemId(cartItem.getBookItem().getId())
                .quantity(cartItem.getQuantity())
                .totalPrice(cartItem.getTotalPrice())
                .build();
    }

    public void removeCartItem(Long id) {
        if (!cartItemRepository.existsById(id)) {
            throw new EntityNotFoundException("CartItem not found with id: " + id);
        }
        cartItemRepository.deleteById(id);
    }
}
