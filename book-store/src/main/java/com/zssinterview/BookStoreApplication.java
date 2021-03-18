package com.zssinterview;

import java.util.stream.IntStream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import com.zssinterview.bookCategory.BookCategory;
import com.zssinterview.bookCategory.BookCategoryService;

@SpringBootApplication
public class BookStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookStoreApplication.class, args);
	}

	@Bean
	@Profile("!test")
	CommandLineRunner run(BookCategoryService bookCategoryService) {
		return args -> {
			IntStream.rangeClosed(1, 2).mapToObj(i -> {
				BookCategory bookCategory = new BookCategory();
				bookCategory.setCategoryTitle("Fiction" + i);
				return bookCategory;
			}).forEach(bookCategoryService::save);

		};
	}

}
