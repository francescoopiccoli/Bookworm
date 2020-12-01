package com.Bookworm.controller;
/*
 * Copyright (c) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
//
import com.Bookworm.model.Book;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.books.Books;
import com.google.api.services.books.BooksRequestInitializer;
import com.google.api.services.books.Books.Volumes.List;
import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;

import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.util.LinkedList;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GoogleBooksClient {
  private static final String APPLICATION_NAME = "bookworm";

  /** Value of the "API key" shown under "Simple API Access". */
  static final String API_KEY = "AIzaSyCrhM2GtOjpf-exVbgJnQdKEDRG494tEG0";

  private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance();
  private static final NumberFormat PERCENT_FORMATTER = NumberFormat.getPercentInstance();
  public static LinkedList<Book> foundBooks;
  private static final Logger LOGGER = Logger.getLogger(GoogleBooksClient.class.getName());



  public static Books connectToAPI(JsonFactory jsonFactory) {
    try {
      return new Books.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, null)
              .setApplicationName(APPLICATION_NAME)
              .setGoogleClientRequestInitializer(new BooksRequestInitializer(API_KEY))
              .build();
    } catch (GeneralSecurityException e) {
      LOGGER.log( Level.SEVERE, "Error instantiating google api client", e);
      //System.out.println("Error instantiating google api client");
      //e.printStackTrace();
    } catch (IOException e) {
      LOGGER.log( Level.SEVERE, "Error instantiating google api client", e);
      //System.out.println("Error instantiating google api client");
      //e.printStackTrace();
    }
    return null;
  }


  //method called by searchBooks to find all books given the  query written in a formal way
  public static LinkedList<Book> getFoundBooks(JsonFactory jsonFactory, String query){

    final Books books = connectToAPI(jsonFactory);

    foundBooks = new LinkedList<>();

    // Set up Books client.

    // Set query string and filter only Google eBooks.
    System.out.println("Query: [" + query + "]");
    List volumesList = null;
    try {
      volumesList = books.volumes().list(query);
    } catch (IOException e) {
      LOGGER.log( Level.SEVERE, e.toString(), e);
      //e.printStackTrace();
    }
    // volumesList.setFilter("ebooks");
    volumesList.setMaxResults((long) 40);
    //volumesList.setOrderBy("newest");
    // Execute the query.
    Volumes volumes = null;
    try {
      volumes = volumesList.execute();
    } catch (UnknownHostException e) {
      LOGGER.log( Level.SEVERE, "An error occurred, check your internet connection!", e);
    } catch (IOException e) {
      LOGGER.log( Level.SEVERE, e.toString(), e);
      //e.printStackTrace();
    }

    try {
      if (volumes.getTotalItems() == 0 || volumes.getItems() == null) {
        System.out.println("No matches found.");
        return null;
      }

    for (Volume volume : volumes.getItems()) {
      Volume.VolumeInfo volumeInfo = volume.getVolumeInfo();
      Volume.SaleInfo saleInfo = volume.getSaleInfo();

      Book currentBook = new Book();

      if (volumeInfo.getDescription() != null && volumeInfo.getDescription().length() > 0) {
        currentBook.setDescription(volumeInfo.getDescription());
      }

      currentBook.setName(volumeInfo.getTitle());

      java.util.List<String> authors = volumeInfo.getAuthors();
      String author = "";
      if (authors != null && !authors.isEmpty()) {
        for (int i = 0; i < authors.size(); ++i) {
          author += authors.get(i);

          if (i < authors.size() - 1) {
            author += ", ";
          }
        }
      }
      currentBook.setAuthor(author);

      if (volumeInfo.getAverageRating() != null && volumeInfo.getAverageRating() >= 0) {
        currentBook.setRating((volumeInfo.getAverageRating().intValue()));
      }

      if(volumeInfo.getImageLinks() != null){
        //to get larger images just change the zoom parameter in the url, &zoom=3 or 0 or 2
        currentBook.setImageURL((String)volumeInfo.getImageLinks().get("thumbnail"));
      }

      foundBooks.add(currentBook);
    }
    return foundBooks;
    } catch (NullPointerException e) {
      LOGGER.log( Level.SEVERE, e.toString(), e);
      return null;
    }
  }

  //method called in method RefreshThread in DiscoverView to retrieve a list of "Book" objects given the user-formulated query
  public static LinkedList<Book> searchBooks(String query) {
    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    try {
      // Verify command line parameters.
      //if (args.length == 0) {
       // System.err.println("Usage: BooksSample [--author|--isbn|--title] \"<query>\"");
      //  System.exit(1);
     // }
      // Parse command line parameters into a query.
      // Query format: "[<author|isbn|intitle>:]<query>"
      String prefix = null;
      String[] parameters = query.split(" ");
      String formalQuery = "";
      for (String arg : parameters) {
        if ("--author".equals(arg)) {
          prefix = "inauthor:";
        } else if ("--isbn".equals(arg)) {
          prefix = "isbn:";
        } else if ("--title".equals(arg)) {
          prefix = "intitle:";
        } else if (arg.startsWith("--")) {
          System.err.println("Unknown argument: " + arg);
          System.exit(1);
        } else {
          formalQuery = formalQuery + " " + arg;
        }
      }
      if (prefix != null) {
        formalQuery = prefix + formalQuery;
      }
      LinkedList<Book> foundBooks = getFoundBooks(jsonFactory, formalQuery);
      return foundBooks;
    } catch (Throwable t) {
      LOGGER.log( Level.SEVERE, t.toString(), t);
      //t.printStackTrace();
    }
    System.exit(0);
    return null;
   }

}
