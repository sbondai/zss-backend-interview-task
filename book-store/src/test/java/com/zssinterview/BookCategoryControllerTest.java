package com.zssinterview;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.After;
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

import com.zssinterview.book.BookRepository;
import com.zssinterview.book.BookService;
import com.zssinterview.book.vm.BookResponseVM;
import com.zssinterview.bookCategory.BookCategory;
import com.zssinterview.bookCategory.BookCategoryRepository;
import com.zssinterview.bookCategory.BookCategoryService;
import com.zssinterview.error.ApiError;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BookCategoryControllerTest {

	private static final String API_1_BOOKCATEGORY = "/api/v1/categories";

	@Autowired
	TestRestTemplate testRestTemplate;

	@Autowired
	BookService bookService;

	@Autowired
	BookRepository bookRepository;

	@Autowired
	BookCategoryRepository bookCategoryRepository;

	@Autowired
	BookCategoryService bookCategoryService;

	@Before
	public void deleteAll() {
		bookCategoryRepository.deleteAll();
		bookRepository.deleteAll();
	}

	@Test
	public void postCategory_whenCategoryIsValid_receiveOK() {

		BookCategory bookCategory = TestUtil.createValidBookCategory();
		ResponseEntity<Object> response = postBookCategory(bookCategory, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

	}

	@Test
	public void postCategory_whenCategoryIsValid_saveCategoryToDatabase() {

		BookCategory bookCategory = TestUtil.createValidBookCategory();
		postBookCategory(bookCategory, Object.class);
		assertThat(bookCategoryRepository.count()).isEqualTo(1);

	}

	@Test
	public void postCategory_whenCategoryTitleIsNull_receiveBadRequest() {

		BookCategory bookCategory = new BookCategory();
		ResponseEntity<Object> response = postBookCategory(bookCategory, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

	}

	@Test
	public void postCategory_whenCategoryLessThan3Character_receiveBadRequest() {

		BookCategory bookCategory = new BookCategory();
		bookCategory.setCategoryTitle("12");
		ResponseEntity<Object> response = postBookCategory(bookCategory, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

	}

	@Test
	public void postCategory_whenCategoryIsMoreThan256Characters_receiveBadRequest() {
		BookCategory bookCategory = new BookCategory();
		String bookCategoryTitle256 = IntStream.rangeClosed(1, 256).mapToObj(i -> "a").collect(Collectors.joining());
		bookCategory.setCategoryTitle(bookCategoryTitle256);
		ResponseEntity<Object> response = postBookCategory(bookCategory, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	public void postCategory_whenCategoryIsNull_receiveApiErrorWithValidationErrors() {
		BookCategory bookCategory = new BookCategory();
		ResponseEntity<ApiError> response = postBookCategory(bookCategory, ApiError.class);
		Map<String, String> validationErrors = response.getBody().getValidationErrors();
		assertThat(validationErrors.get("categoryTitle")).isNotNull();
	}

	@Test
	public void postBook_whenSameCategoryInDB_receiveBadRequest() {

		bookCategoryRepository.save(TestUtil.createValidBookCategory());

		BookCategory bookCategory = TestUtil.createValidBookCategory();
		postBookCategory(bookCategory, Object.class);

		ResponseEntity<Object> response = postBookCategory(bookCategory, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	public void postBook_whenSameCategoryInDB_receiveApiError() {

		bookCategoryRepository.save(TestUtil.createValidBookCategory());

		BookCategory bookCategory = TestUtil.createValidBookCategory();
		postBookCategory(bookCategory, Object.class);

		ResponseEntity<ApiError> response = postBookCategory(bookCategory, ApiError.class);
		Map<String, String> validationErrors = response.getBody().getValidationErrors();
		assertThat(validationErrors.get("categoryTitle")).isEqualTo("This Category exists");
	}

	@Test
	public void getBooksInCategory_wheCategoryExists_receiveOK() {

		bookCategoryService.save(TestUtil.createValidBookCategory());

		ResponseEntity<Object> response = getBooksOfCategory("Fiction", new ParameterizedTypeReference<Object>() {
		});

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	public void getBooksInCategory_wheCategoryDoesNotExists_receiveNotFound() {

		ResponseEntity<Object> response = getBooksOfCategory("unknown-category",
				new ParameterizedTypeReference<Object>() {
				});

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	public void getBooksInCategory_wheCategoryExists_receivePageWithZeroBooks() {

		bookCategoryService.save(TestUtil.createValidBookCategory());

		ResponseEntity<TestPage<Object>> response = getBooksOfCategory("Fiction",
				new ParameterizedTypeReference<TestPage<Object>>() {
				});

		assertThat(response.getBody().getTotalElements()).isEqualTo(0);
	}

	@Test
	public void getBooksInCategory_wheCategoryExists_receivePageWithBookVM() {

		bookCategoryRepository.save(TestUtil.createValidBookCategory());
		bookService.save(TestUtil.createValidBook("Java in Action"));

		ResponseEntity<TestPage<BookResponseVM>> response = getBooksOfCategory("Fiction",
				new ParameterizedTypeReference<TestPage<BookResponseVM>>() {
				});

		BookResponseVM storedBook = response.getBody().getContent().get(0);

		assertThat(storedBook.getBookCat()).isEqualTo("Fiction");
	}

	@Test
	public void getBooksInCategory_wheCategoryExistsWithMultipleBooks_receivePageWithSameBookCount() {
		bookCategoryRepository.save(TestUtil.createValidBookCategory());
		bookService.save(TestUtil.createValidBook("Java in Action"));

		// bookRepository.save(TestUtil.createValidBook());

		ResponseEntity<TestPage<BookResponseVM>> response = getBooksOfCategory("Fiction",
				new ParameterizedTypeReference<TestPage<BookResponseVM>>() {
				});

		assertThat(response.getBody().getTotalElements()).isEqualTo(1);
	}

	public <T> ResponseEntity<T> getBooksOfCategory(String category, ParameterizedTypeReference<T> responseType) {
		String path = "/api/v1/categories/" + category;
		return testRestTemplate.exchange(path, HttpMethod.GET, null, responseType);
	}

	private <T> ResponseEntity<T> postBookCategory(BookCategory bookCategory, Class<T> responseType) {
		return testRestTemplate.postForEntity(API_1_BOOKCATEGORY, bookCategory, responseType);
	}

	@After
	public void runAfter() {
		bookRepository.deleteAll();
	}

}
