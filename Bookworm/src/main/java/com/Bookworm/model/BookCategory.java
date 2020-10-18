package com.Bookworm.model
import java.util.List;

public class BookCategory extends GeneralList {
	private String name;
	private List<Book> books;
	
	public BookCategory(String name, List<Book> books) {
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

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}
	
}
