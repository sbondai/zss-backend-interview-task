package com.zssinterview.book;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.zssinterview.book.transact.Card;
import com.zssinterview.book.transact.Transaction;
import com.zssinterview.book.vm.BookRequestVM;
import com.zssinterview.book.vm.TransactRequest;
import com.zssinterview.bookCategory.BookCategoryRepository;
import com.zssinterview.error.NotFoundException;

@Service
public class BookService {

	private static final String API_ZSS_TOKEN = "a4cd881f-7b98-4ffd-93ce-f19ff8d34769";

	BookRepository bookRepository;

	BookCategoryRepository bookCategoryRepository;

	public BookService(BookRepository bookRepository, BookCategoryRepository bookCategoryRepository) {
		super();
		this.bookRepository = bookRepository;
		this.bookCategoryRepository = bookCategoryRepository;
	}

	public Book save(BookRequestVM bookRequest) {
		Book book = new Book();
		BeanUtils.copyProperties(bookRequest, book);
		book.setBookCategory(bookCategoryRepository.findByCategoryTitle(bookRequest.getBookCategory()));
		return bookRepository.save(book);
	}

	public Page<Book> getBooks(Pageable pageable) {
		return bookRepository.findAll(pageable);

	}

	public Book getByTitle(String title) {
		Book bookInDB = bookRepository.findByTitle(title);
		if (bookInDB == null) {
			throw new NotFoundException(title + " not found");
		}
		return bookInDB;
	}

	public String bookTransact(TransactRequest transactReq) throws Exception {
		Book validBook = getByTitle(transactReq.getBookTitle());

		RestTemplate anotherRestTemplate = new RestTemplate();

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		Transaction transact = new Transaction();
		transact.setType("PURCHASE");
		transact.setExtendedType("NONE");
		transact.setAmount(validBook.getPrice());
		transact.setCreated(timestamp.toInstant().toString());
		transact.setReference("1a1d7ef9-03e9-4224-bd9d-4b00a4bbd1cc");
		transact.setNarration("Test Narration");
		transact.setCard(new Card(transactReq.getCardNumber(), transactReq.getExpiryDate()));
		transact.setAdditionalData(new HashMap<String, Object>() {
			/**
			* 
			*/
			private static final long serialVersionUID = 1L;

			{
				put("SampleKey", "This is a sample value");

			}
		});

		HttpHeaders headers = new HttpHeaders();
		// set `content-type` header
		headers.setContentType(MediaType.APPLICATION_JSON);
		// set `accept` header
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Bearer " + API_ZSS_TOKEN);

		HttpEntity<Transaction> entity = new HttpEntity<>(transact, headers);

		ResponseEntity<Object> response = anotherRestTemplate
				.exchange("https://secure.v.co.zw/interview/api/transaction", HttpMethod.POST, entity, Object.class);
		return response.getBody().toString();
	}

}
