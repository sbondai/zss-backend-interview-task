package com.zssinterview.book;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = UniqueTitleValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueBookTitle {

	String message() default "{zssinterview.constraints.title.UniqueTitle.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
