package com.zssinterview.book;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = BookCategoryMustExistValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BookCategoryMustExistInDB {

	String message() default "{zssinterview.constraints.category.BookCatInDB.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
