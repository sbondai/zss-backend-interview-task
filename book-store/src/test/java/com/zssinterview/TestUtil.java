package com.zssinterview;

import com.zssinterview.book.Book;
import com.zssinterview.book.vm.BookRequestVM;
import com.zssinterview.bookCategory.BookCategory;

public class TestUtil {

	public static Book createValidBook() {
		Book book = new Book();

		book.setTitle("Learn Microservices with Spring Boot");
		book.setDescription("A Practical Approach to RESTful Services Using an Event-Driven Architecture "
				+ "Cloud-Native Patterns, and Containerization");
		book.setPrice(10.21);
		book.setBookCategory(TestUtil.createValidBookCategory());

		return book;
	}

	public static BookRequestVM createValidBookRequest() {
		BookRequestVM book = new BookRequestVM();

		book.setTitle("Learn Microservices with Spring Boot");
		book.setDescription("A Practical Approach to RESTful Services Using an Event-Driven Architecture "
				+ "Cloud-Native Patterns, and Containerization");
		book.setPrice(10.21);
		book.setBookCategory("Fiction");
		return book;
	}

	public static BookRequestVM createValidBook(String title) {
		BookRequestVM book = TestUtil.createValidBookRequest();
		book.setTitle(title);
		return book;

	}

	public static Book createValidBookDiffTitle(String title) {
		Book book = TestUtil.createValidBook();
		book.setTitle(title);
		return book;

	}

	public static BookCategory createValidBookCategory() {
		BookCategory bookCategory = new BookCategory();
		bookCategory.setCategoryTitle("Fiction");
		return bookCategory;

	}
}
