package com.zssinterview.book;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.zssinterview.bookCategory.BookCategory;

import lombok.Data;

@Data
@Entity
public class Book {

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

	@ManyToOne(cascade = CascadeType.MERGE)
	private BookCategory bookCategory;
}
