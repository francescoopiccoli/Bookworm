package com.Bookworm.model;
import java.util.ArrayList;
import java.util.List;

public class Book {
		
		private String description, name;
		private String category;
		private ArrayList<Tag> tags;
		private String author;
		private int rate;
		
		
		public Book(String name, String description, String category, ArrayList<Tag> tags, String author, int rate) {
			super();
			this.description = description;
			this.name = name;
			this.category = category;
			this.tags = tags;
			this.author = author;
			this.rate = rate;
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
		public int getRate() {
			return rate;
		}
		public void setRate(int rate) {
			this.rate = rate;
		}	

}
