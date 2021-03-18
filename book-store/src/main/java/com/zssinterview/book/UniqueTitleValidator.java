package com.zssinterview.book;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

public class UniqueTitleValidator implements ConstraintValidator<UniqueBookTitle, String> {

	@Autowired
	BookRepository bookRepository;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		Book inDB = bookRepository.findByTitle(value);
		if (inDB == null) {
			return true;
		}
		return false;
	}

}
