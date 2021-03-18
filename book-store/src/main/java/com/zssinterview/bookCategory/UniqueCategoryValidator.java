package com.zssinterview.bookCategory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

public class UniqueCategoryValidator implements ConstraintValidator<UniqueCategoryTitle, String> {

	@Autowired
	BookCategoryRepository bookCategoryRepository;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		BookCategory inDB = bookCategoryRepository.findByCategoryTitle(value);
		if (inDB == null) {
			return true;
		}
		return false;
	}

}
