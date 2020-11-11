package com.Bookworm.model;
import java.io.Serializable;
import java.util.ArrayList;

public class Book implements Serializable {

	private String description;
	private String name;
	private String imageURL;
	private String review;
	private ArrayList<Tag> tags;
	private String author;
	private int rating;
	private int id;

	public Book(String name, String author, String description) {
		super();
		this.description = description;
		this.name = name;
		this.tags = null;
		this.author = author;
		this.rating = 0;
		this.imageURL = "";
		this.review  = "";
		this.id = -1;
	}

	public Book() {}

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


    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
}
