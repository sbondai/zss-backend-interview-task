package com.zssinterview.book.vm;

import com.zssinterview.book.Book;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookResponseVM {

	private long id;

	private String title;

	private String description;

	private double price;

	private String bookCat;

	public BookResponseVM(Book book) {
		this.setId(book.getId());
		this.setTitle(book.getTitle());
		this.setDescription(book.getDescription());
		this.setPrice(book.getPrice());
		this.setBookCat(book.getBookCategory().getCategoryTitle());

	}

}
