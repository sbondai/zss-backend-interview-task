package com.zssinterview.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.zssinterview.bookCategory.BookCategory;

public interface BookRepository extends JpaRepository<Book, Long> {

	Book findByTitle(String string);

	Page<Book> findByBookCategory(BookCategory inDB, Pageable pageable);

}
