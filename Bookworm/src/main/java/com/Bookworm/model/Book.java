package com.Bookworm.model;
import java.io.Serializable;
import java.util.ArrayList;

public class Book implements Serializable {

	private String description;
	private String name;
	private String imageURL;
	private String review;
	private String category;
	private ArrayList<Tag> tags;
	private String author;
	private int rating;

	public Book(String description, String name, String category, ArrayList<Tag> tags, String author, int rating, String review, String imageURL) {
		super();
		this.description = description;
		this.name = name;
		this.category = category;
		this.tags = tags;
		this.author = author;
		this.rating = rating;
		this.imageURL = imageURL;
		this.review  = review;
	}

	public Book() {

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
	public ArrayList<Tag> getTags() {
		return tags;
	}
	public void setTags(ArrayList<Tag> tags) {
		this.tags = tags;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getReview() { return review; }

	public void setReview(String review) { this.review = review;}


}
