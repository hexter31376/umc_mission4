package com.hexter31376.umc_mission4.repository.book;

import com.hexter31376.umc_mission4.domain.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}

