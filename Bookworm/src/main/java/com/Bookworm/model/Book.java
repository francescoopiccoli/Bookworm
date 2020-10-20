package com.Bookworm.model;
import java.util.List;

public class Book {
		
		private String description, name;
		private BookCategory category;
		private List<Tag> tags;
		private Author author;
		private int rate;
		
		
		public Book(String description, String name, BookCategory category, List<Tag> tags, Author author, int rate) {
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
		public BookCategory getCategory() {
			return category;
		}
		public void setCategory(BookCategory category) {
			this.category = category;
		}
		public List<Tag> getTags() {
			return tags;
		}
		public void setTags(List<Tag> tags) {
			this.tags = tags;
		}
		public Author getAuthor() {
			return author;
		}
		public void setAuthor(Author author) {
			this.author = author;
		}
		public int getRate() {
			return rate;
		}
		public void setRate(int rate) {
			this.rate = rate;
		}	

}
