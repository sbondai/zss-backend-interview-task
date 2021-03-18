package com.zssinterview.book;

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

import com.zssinterview.book.vm.BookRequestVM;
import com.zssinterview.book.vm.BookVM;
import com.zssinterview.book.vm.TransactRequest;
import com.zssinterview.shared.GenericResponse;

@RestController
@RequestMapping("/api/v1")
public class BookController {

	@Autowired
	BookService bookService;

	@PostMapping("/books")
	GenericResponse createBook(@Valid @RequestBody BookRequestVM book) {

		bookService.save(book);
		return new GenericResponse("Book saved");

	}

	@GetMapping("/books")
	Page<BookVM> getUsers(Pageable page) {
		return bookService.getBooks(page).map(BookVM::new);
	}

	@GetMapping("/books/{title}")
	BookVM getBook(@PathVariable String title) {
		Book book = bookService.getByTitle(title);
		return new BookVM(book);
	}

	@PostMapping("/books/transact")
	GenericResponse bookTransact(@Valid @RequestBody TransactRequest transactReq) throws Exception {
		String message = bookService.bookTransact(transactReq);
		return new GenericResponse(message);
	}

}
