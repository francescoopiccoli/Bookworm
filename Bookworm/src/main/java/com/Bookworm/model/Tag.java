package com.Bookworm.model;

public class Tag extends GeneralList {
	private String name;

	public Tag(String name) {
		super();
		this.name = name;
		this.books = books;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
