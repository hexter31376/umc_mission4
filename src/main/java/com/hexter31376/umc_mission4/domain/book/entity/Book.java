package com.hexter31376.umc_mission4.domain.book.entity;

import com.hexter31376.umc_mission4.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Table(name = "books")
public class Book extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100) // 20 → 100 등 현실적 길이
    private String title;

    @Builder.Default
    @Column(nullable = false, length = 100)
    private String author = "none";

    @Builder.Default
    @Column(nullable = false, length = 1000)
    private String description = "설명이 없습니다.";

    @Builder.Default
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BookItem> bookItems = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();
}