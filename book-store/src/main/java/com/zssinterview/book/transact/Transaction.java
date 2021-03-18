package com.zssinterview.book.transact;

import java.util.Map;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Transaction {

	private String type;

	private String extendedType;

	private double amount;

	private String created;

	private String reference;

	private String narration;

	private Map<String, Object> additionalData;

	private Card card;

}
