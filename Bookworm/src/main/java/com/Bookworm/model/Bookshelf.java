package com.Bookworm.model;
import java.util.List;

public class Bookshelf {
	private String description;
	private String name;
	private List<Book> books;

	public Bookshelf(String name, String description, List<Book> books) {
		super();
		this.name = name;
		this.description = description;
		this.books = books;
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public void setBooks(List<Book> books) {
		this.books = books;
	}


	public List<Book> getBooks() {
		return null;
	}

	public void addBook() {
		// TODO Auto-generated method stub

	}
	public void removeBook() {
		// TODO Auto-generated method stub

	}

}
