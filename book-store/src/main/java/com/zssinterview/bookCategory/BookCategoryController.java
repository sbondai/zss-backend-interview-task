package com.zssinterview.bookCategory;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zssinterview.book.vm.BookResponseVM;

@RestController
@RequestMapping("/api/v1")
public class BookCategoryController {

	@Autowired
	BookCategoryService bookCategoryService;

	@PostMapping("/categories")
	void createCategory(@Valid @RequestBody BookCategory bookCategory) {
		bookCategoryService.save(bookCategory);
	}

	@GetMapping("/categories/{category}")
	Page<BookResponseVM> getBooksOfCategory(@PathVariable String category, Pageable pageable) {
		return bookCategoryService.getBooksOfCategory(category, pageable).map(BookResponseVM::new);
	}

}
