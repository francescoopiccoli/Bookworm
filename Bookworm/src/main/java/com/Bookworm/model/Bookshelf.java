package com.Bookworm.model;
import java.util.ArrayList;
import java.util.List;

public class Bookshelf extends GeneralList {
	private String description;
	
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
		ArrayList<Tag> tags = new ArrayList<Tag>();
		tags.add(new Tag("tag", null));
		ArrayList<Book> istanceList = new ArrayList<Book>();
		istanceList.add(new Book("istance", "La spada nella roccia", new BookCategory("Horror", null), tags, new Author("Artù", null), 5));
		istanceList.add(new Book("istance", "La spada nella roccia", new BookCategory("Horror", null), tags, new Author("Artù", null), 5));
		istanceList.add(new Book("istance", "La spada nella roccia", new BookCategory("Horror", null), tags, new Author("Artù", null), 5));
		return null;
	}
}
