package com.zssinterview;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.zssinterview.book.Book;
import com.zssinterview.book.BookRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class BookRepositoryTest {

	@Autowired
	TestEntityManager testEntityManager;

	@Autowired
	BookRepository bookRepository;

	@Test
	public void findByTitle_whenBookDoesNotExist_returnNull() {
		Book inDB = bookRepository.findByTitle("book-not-inDB");
		assertThat(inDB).isNull();
	}

}
