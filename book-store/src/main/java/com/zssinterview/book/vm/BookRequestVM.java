package com.zssinterview.book.vm;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.zssinterview.book.BookCategoryMustExistInDB;
import com.zssinterview.book.UniqueBookTitle;

import lombok.Data;

@Data
public class BookRequestVM {

	@Id
	@GeneratedValue
	private long id;

	@NotNull
	@Size(min = 3, max = 255)
	@UniqueBookTitle
	private String title;

	@NotNull
	@Size(min = 8, max = 5000)
	@Column(length = 5000)
	private String description;

	@DecimalMin(value = "0.0", inclusive = false)
	@Digits(integer = 3, fraction = 2)
	private double price;

	@NotNull
	@BookCategoryMustExistInDB
	private String bookCategory;

}
