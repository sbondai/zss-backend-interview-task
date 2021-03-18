package com.zssinterview.bookCategory;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.zssinterview.book.Book;

import lombok.Data;

@Entity
@Data
public class BookCategory {

	@Id
	@GeneratedValue
	private long id;

	@NotNull
	@Size(min = 3, max = 255)
	@UniqueCategoryTitle
	private String categoryTitle;

	@OneToMany(mappedBy = "bookCategory")
	private List<Book> books;

}
