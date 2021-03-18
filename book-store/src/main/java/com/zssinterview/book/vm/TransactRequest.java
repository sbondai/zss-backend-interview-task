package com.zssinterview.book.vm;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class TransactRequest {

	@NotNull
	private String cardNumber;

	@NotNull
	private String expiryDate;

	@NotNull
	private String bookTitle;

}
