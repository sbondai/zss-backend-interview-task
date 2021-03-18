package com.zssinterview.book;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.zssinterview.bookCategory.BookCategory;
import com.zssinterview.bookCategory.BookCategoryRepository;

public class BookCategoryMustExistValidator implements ConstraintValidator<BookCategoryMustExistInDB, String> {

	@Autowired
	BookCategoryRepository bookCategoryRepository;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		BookCategory inDB = bookCategoryRepository.findByCategoryTitle(value);
		if (inDB != null) {
			return true;
		}
		return false;
	}

}
