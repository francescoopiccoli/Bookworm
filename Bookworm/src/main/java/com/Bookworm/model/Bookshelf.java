package com.Bookworm.model;
import java.util.ArrayList;

public class Bookshelf {
	private String description;
	private String name;
	private ArrayList<Book> books;
	private int id;

	public Bookshelf(String name, String description, ArrayList<Book> books) {
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


	public void setBooks(ArrayList<Book> books) {
		this.books = books;
	}


	public ArrayList<Book> getBooks() {
		return books;
	}

	public void addBook() {
		// TODO Auto-generated method stub

	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
