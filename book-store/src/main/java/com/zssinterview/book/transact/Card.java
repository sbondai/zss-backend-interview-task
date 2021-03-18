package com.zssinterview.book.transact;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Card {

	private String id;
	private String expiry;

	public Card(String id, String expiry) {
		super();
		this.id = id;
		this.expiry = expiry;
	}

}
