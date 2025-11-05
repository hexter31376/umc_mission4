package com.hexter31376.umc_mission4.repository.book;

import com.hexter31376.umc_mission4.domain.book.entity.BookItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookItemRepository extends JpaRepository<BookItem, Long> {
}

