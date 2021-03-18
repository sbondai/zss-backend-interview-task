package com.zssinterview;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.zssinterview.book.Book;
import com.zssinterview.book.BookRepository;
import com.zssinterview.book.BookService;
import com.zssinterview.book.vm.BookRequestVM;
import com.zssinterview.bookCategory.BookCategoryRepository;
import com.zssinterview.bookCategory.BookCategoryService;
import com.zssinterview.error.ApiError;
import com.zssinterview.shared.GenericResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BookControllerTest {

	private static final String API_V1_BOOKS = "/api/v1/books";

	@Autowired
	TestRestTemplate testRestTemplate;

	@Autowired
	BookRepository bookRepository;

	@Autowired
	BookService bookService;

	@Autowired
	BookCategoryService bookCategoryService;

	@Autowired
	BookCategoryRepository bookCategoryRepository;

	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;

	@Before
	public void deleteAll() {
		bookRepository.deleteAll();
		bookCategoryRepository.deleteAll();

	}

	@Test
	public void postBook_whenBookIsValid_receiveOk() {
		bookCategoryRepository.save(TestUtil.createValidBookCategory());
		BookRequestVM book = TestUtil.createValidBookRequest();
		ResponseEntity<Object> response = postBook(book, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

	}

	@Test
	public void postBook_whenBookIsValid_saveBookToDatabase() {
		bookCategoryRepository.save(TestUtil.createValidBookCategory());
		BookRequestVM book = TestUtil.createValidBookRequest();
		postBook(book, Object.class);
		assertThat(bookRepository.count()).isEqualTo(1);
	}

	@Test
	public void postBook_whenBookIsValid_receiveSuccessMessage() {
		BookRequestVM book = TestUtil.createValidBookRequest();
		ResponseEntity<GenericResponse> response = postBook(book, GenericResponse.class);
		assertThat(response.getBody().getMessage()).isNotNull();

	}

	@Test
	public void postBook_whenBookHasNoTitle_receiveBadRequest() {
		BookRequestVM book = TestUtil.createValidBookRequest();
		book.setTitle(null);
		ResponseEntity<Object> response = postBook(book, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	public void postBook_whenBookHasNoDescription_receiveBadRequest() {
		BookRequestVM book = TestUtil.createValidBookRequest();
		book.setDescription(null);
		ResponseEntity<Object> response = postBook(book, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	public void postBook_whenBookHasTitleWithLessThanRequired_receiveBadRequest() {
		BookRequestVM book = TestUtil.createValidBookRequest();
		book.setTitle("SQ");
		ResponseEntity<Object> response = postBook(book, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	public void postBook_whenBookHasDescriptionWithLessThanRequired_receiveBadRequest() {
		BookRequestVM book = TestUtil.createValidBookRequest();
		book.setDescription("sq book");
		ResponseEntity<Object> response = postBook(book, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	public void postBook_whenBookTitleExceedsCharacterLimit_receiveBadRequest() {
		BookRequestVM book = TestUtil.createValidBookRequest();
		String bookTitle256 = IntStream.rangeClosed(1, 256).mapToObj(i -> "a").collect(Collectors.joining());
		book.setTitle(bookTitle256);
		ResponseEntity<Object> response = postBook(book, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	public void postBook_whenBookDescriptionIs5000Characters_receiveOk() {
		bookCategoryRepository.save(TestUtil.createValidBookCategory());
		BookRequestVM book = TestUtil.createValidBookRequest();
		String longDescription = IntStream.rangeClosed(1, 5000).mapToObj(i -> "x").collect(Collectors.joining());
		book.setDescription(longDescription);
		ResponseEntity<Object> response = postBook(book, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	public void postBook_whenBookDescriptionIsMoreThan5000Characters_receiveOk() {
		BookRequestVM book = TestUtil.createValidBookRequest();
		String longDescription = IntStream.rangeClosed(1, 5001).mapToObj(i -> "x").collect(Collectors.joining());
		book.setDescription(longDescription);
		ResponseEntity<Object> response = postBook(book, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	public void postBook_whenUserIsInvalid_receiveApiError() {
		BookRequestVM book = new BookRequestVM();
		ResponseEntity<ApiError> response = postBook(book, ApiError.class);
		assertThat(response.getBody().getUrl()).isEqualTo(API_V1_BOOKS);
	}

	@Test
	public void postBook_whenUserIsInvalid_receiveApiErrorWithValidationErrors() {
		BookRequestVM book = new BookRequestVM();
		ResponseEntity<ApiError> response = postBook(book, ApiError.class);
		assertThat(response.getBody().getValidationErrors().size()).isEqualTo(4);
	}

	@Test
	public void postBook_whenSameBookTitleExistsInDB_receiveBadRequest() {
		bookService.save(TestUtil.createValidBookRequest());

		BookRequestVM book = TestUtil.createValidBookRequest();
		ResponseEntity<Object> response = postBook(book, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	public void postBook_whenSameBookTitleExistsInDB_receiveMessageOfDulicateBookTitle() {

		bookService.save(TestUtil.createValidBookRequest());

		BookRequestVM book = TestUtil.createValidBookRequest();

		ResponseEntity<ApiError> response = postBook(book, ApiError.class);
		Map<String, String> validationErrors = response.getBody().getValidationErrors();
		assertThat(validationErrors.get("title")).isEqualTo("This Book exists");
	}

	@Test
	public void getBooks_whenThereAreNoBooksInDB_receiveOK() {
		ResponseEntity<Object> response = testRestTemplate.getForEntity(API_V1_BOOKS, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	public void getBooks_whenThereAreNoBooksInDB_receivePageWithZeroItems() {
		ResponseEntity<TestPage<Object>> response = getBooks(new ParameterizedTypeReference<TestPage<Object>>() {
		});
		assertThat(response.getBody().getTotalElements()).isEqualTo(0);
	}

	@Test
	public void getBooks_whenThereIsABook_receivePageWithBook() {
		// bookCategoryRepository.save(TestUtil.createValidBookCategory());
		bookService.save(TestUtil.createValidBookRequest());

		ResponseEntity<TestPage<Object>> response = getBooks(new ParameterizedTypeReference<TestPage<Object>>() {
		});
		assertThat(response.getBody().getTotalElements()).isEqualTo(1);
	}

	@Test
	public void getBooks_whenPageIsRequestedFor3BooksPerPageWhenDBHas20Books_receive3Books() {
		IntStream.rangeClosed(1, 20).mapToObj(i -> "sample-book-" + i).map(

				TestUtil::createValidBook)

				.forEach(bookService::save);

		String path = API_V1_BOOKS + "?page=0&size=3";

		ResponseEntity<TestPage<Object>> response = getBooks(path, new ParameterizedTypeReference<TestPage<Object>>() {
		});

		assertThat(response.getBody().getContent().size()).isEqualTo(3);
	}

	@Test
	public void getBooks_whenThereAreNoBooksInDB_receivePageSize10() {
		ResponseEntity<TestPage<Object>> response = getBooks(new ParameterizedTypeReference<TestPage<Object>>() {
		});
		assertThat(response.getBody().getSize()).isEqualTo(10);
	}

	@Test
	public void getBooks_whenPageSizeIsGreaterThan100_receivePageSizeAs100() {
		String path = API_V1_BOOKS + "?size=500";
		ResponseEntity<TestPage<Object>> response = getBooks(path, new ParameterizedTypeReference<TestPage<Object>>() {
		});
		assertThat(response.getBody().getSize()).isEqualTo(100);
	}

	@Test
	public void getBooks_whenPageSizeIsNegative_receivePageSizeAs10() {
		String path = API_V1_BOOKS + "?size=-5";
		ResponseEntity<TestPage<Object>> response = getBooks(path, new ParameterizedTypeReference<TestPage<Object>>() {
		});
		assertThat(response.getBody().getSize()).isEqualTo(10);
	}

	@Test
	public void getBooks_whenPageSizeIsNegative_receiveFirstPage() {
		String path = API_V1_BOOKS + "?page=-5";
		ResponseEntity<TestPage<Object>> response = getBooks(path, new ParameterizedTypeReference<TestPage<Object>>() {
		});
		assertThat(response.getBody().getNumber()).isEqualTo(0);
	}

	@Test
	public void getBookByTitle_whenBookExists_receiveOK() {
		String title = "sample book title";

		bookService.save(TestUtil.createValidBook(title));

		ResponseEntity<Object> response = getBook(title, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

	}

	@Test
	public void getBookByTitle_whenBookDoesNotExists_receiveNotFound() {
		ResponseEntity<Object> response = getBook("unknown-book", Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

	}

	@Test
	public void getBookByTitle_whenBookDoesNotExists_receiveApiError() {
		ResponseEntity<ApiError> response = getBook("unknown-book", ApiError.class);
		assertThat(response.getBody().getMessage().contains("unknown-book")).isTrue();

	}

	@Test
	public void postBook_whenBookIsValid_BookSavedWithCategoryInfo() {

		bookCategoryRepository.save(TestUtil.createValidBookCategory());

		BookRequestVM book = TestUtil.createValidBookRequest();

		postBook(book, Object.class);

		Book inDB = bookRepository.findAll().get(0);

		assertThat(inDB.getBookCategory().getCategoryTitle()).isEqualTo("Fiction");
	}

	public <T> ResponseEntity<T> getBook(String title, Class<T> responseType) {
		String path = API_V1_BOOKS + "/" + title;
		return testRestTemplate.exchange(path, HttpMethod.GET, null, responseType);
	}

	public <T> ResponseEntity<T> getBooks(String path, ParameterizedTypeReference<T> responseType) {
		return testRestTemplate.exchange(path, HttpMethod.GET, null, responseType);
	}

	public <T> ResponseEntity<T> getBooks(ParameterizedTypeReference<T> responseType) {
		return testRestTemplate.exchange(API_V1_BOOKS, HttpMethod.GET, null, responseType);
	}

	public <T> ResponseEntity<T> postBook(Object request, Class<T> response) {
		return testRestTemplate.postForEntity(API_V1_BOOKS, request, response);
	}

}
