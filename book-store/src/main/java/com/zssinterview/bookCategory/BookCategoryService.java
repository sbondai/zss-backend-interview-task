package com.zssinterview.bookCategory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.zssinterview.book.Book;
import com.zssinterview.book.BookRepository;
import com.zssinterview.error.NotFoundException;

@Service
public class BookCategoryService {

	BookCategoryRepository bookCategoryRepository;

	BookRepository bookRepository;

	public BookCategoryService(BookCategoryRepository bookCategoryRepository, BookRepository bookRepository) {
		super();
		this.bookCategoryRepository = bookCategoryRepository;
		this.bookRepository = bookRepository;
	}

	public void save(BookCategory bookCategory) {

		bookCategoryRepository.save(bookCategory);
	}

	public Page<Book> getBooksOfCategory(String category, Pageable pageable) {

		BookCategory inDB = bookCategoryRepository.findByCategoryTitle(category);
		if (inDB == null) {
			throw new NotFoundException(category + " not found");
		}
		return bookRepository.findByBookCategory(inDB, pageable);

	}

}
