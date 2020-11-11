package com.Bookworm.model;
import java.util.ArrayList;

public class Bookshelf {
	private int id;
	private String description;
	private String name;
	private ArrayList<Book> books;

	public Bookshelf(String name, String description, ArrayList<Book> books) {
		super();
		this.name = name;
		this.description = description;
		this.books = books;
	}
	public Bookshelf(String name, String description) {
		super();
		this.name = name;
		this.description = description;
		this.books = null;
	}

	public Bookshelf() {}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) { this.description = description; }

	public void setBooks(ArrayList<Book> books) {
		this.books = books;
	}

	public ArrayList<Book> getBooks() {
		return books;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
