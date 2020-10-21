package com.Bookworm.model;
import java.util.List;

public class Book {

	private String description, name, imageURL;
	private String category;
	private List<Tag> tags;
	private String author;
	private int rate;

	public Book(String description, String name, String category, List<Tag> tags, String author, int rate, String imageURL) {
		super();
		this.description = description;
		this.name = name;
		this.category = category;
		this.tags = tags;
		this.author = author;
		this.rate = rate;
		this.imageURL = imageURL;
	}

	public Book(){

	}


	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public List<Tag> getTags() {
		return tags;
	}
	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
}
