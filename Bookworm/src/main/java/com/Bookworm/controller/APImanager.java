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

//  metodo search ritorna lista di libri, anche querygooglebook.
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

import java.security.GeneralSecurityException;
import java.util.LinkedList;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.NumberFormat;

/**
 * A sample application that demonstrates how Google Books Client Library for
 * Java can be used to query Google Books. It accepts queries in the command
 * line, and prints the results to the console.
 *
 * $ java com.google.sample.books.BooksSample [--author|--isbn|--title] "<query>"
 *
 * Please start by reviewing the Google Books API documentation at:
 * http://code.google.com/apis/books/docs/getting_started.html
 */
public class APImanager {

  /**
   * Be sure to specify the name of your application. If the application name is {@code null} or
   * blank, the application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
   */
  private static final String APPLICATION_NAME = "bookworm";

  private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance();
  private static final NumberFormat PERCENT_FORMATTER = NumberFormat.getPercentInstance();
  public static LinkedList<Book> foundBooks;

  public static Books connectToAPI(JsonFactory jsonFactory) {
    try {
      return new Books.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, null)
              .setApplicationName(APPLICATION_NAME)
              .setGoogleClientRequestInitializer(new BooksRequestInitializer(ClientCredentials.API_KEY))
              .build();
    } catch (GeneralSecurityException e) {
      System.out.println("Error instantiating google api client");
      e.printStackTrace();
    } catch (IOException e) {
      System.out.println("Error instantiating google api client");
      e.printStackTrace();
    }
    return null;
  }

  public static LinkedList<Book> getFoundBooks(JsonFactory jsonFactory, String query) throws Exception {

    final Books books = connectToAPI(jsonFactory);

    foundBooks = new LinkedList<>();
    ClientCredentials.errorIfNotSpecified();

    // Set up Books client.

    // Set query string and filter only Google eBooks.
    System.out.println("Query: [" + query + "]");
    List volumesList = books.volumes().list(query);
   // volumesList.setFilter("ebooks");
    volumesList.setMaxResults((long) 40);
    //volumesList.setOrderBy("newest");
    // Execute the query.
    Volumes volumes = volumesList.execute();
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

      currentBook.setTags(null);

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
  }


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
      try {
        LinkedList<Book> foundBooks = getFoundBooks(jsonFactory, formalQuery);
        //for(Book b : foundBooks){
            //System.out.println(b.getImageURL());
        //}
        // Success!
        return foundBooks;
      } catch (IOException e) {
        System.err.println(e.getMessage());
      }
    } catch (Throwable t) {
      t.printStackTrace();
    }
    System.exit(0);
     return null;
   }

  private static void queryGoogleBooks(JsonFactory jsonFactory, String query) throws Exception {

    ClientCredentials.errorIfNotSpecified();

    // Set up Books client.
    final Books books = new Books.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, null)
            .setApplicationName(APPLICATION_NAME)
            .setGoogleClientRequestInitializer(new BooksRequestInitializer(ClientCredentials.API_KEY))
            .build();
    // Set query string and filter only Google eBooks.
    System.out.println("Query: [" + query + "]");
    List volumesList = books.volumes().list(query);
    volumesList.setFilter("ebooks");

    // Execute the query.
    Volumes volumes = volumesList.execute();
    if (volumes.getTotalItems() == 0 || volumes.getItems() == null) {
      System.out.println("No matches found.");
      return;
    }

    // Output results.
    for (Volume volume : volumes.getItems()) {
      Volume.VolumeInfo volumeInfo = volume.getVolumeInfo();
      Volume.SaleInfo saleInfo = volume.getSaleInfo();
      System.out.println("==========");
      // Title.
      System.out.println("Title: " + volumeInfo.getTitle());
      // Author(s).
      java.util.List<String> authors = volumeInfo.getAuthors();
      if (authors != null && !authors.isEmpty()) {
        System.out.print("Author(s): ");
        for (int i = 0; i < authors.size(); ++i) {
          System.out.print(authors.get(i));
          if (i < authors.size() - 1) {
            System.out.print(", ");
          }
        }
        System.out.println();
      }
      // Description (if any).
      if (volumeInfo.getDescription() != null && volumeInfo.getDescription().length() > 0) {
        System.out.println("Description: " + volumeInfo.getDescription());
      }

      if(volumeInfo.getImageLinks() != null){
        //to get larger images just change the zoom parameter in the url, &zoom=3 or 0 or 2
        System.out.println("Image link: " + volumeInfo.getImageLinks().get("thumbnail"));
      }

      // Ratings (if any).
      if (volumeInfo.getRatingsCount() != null && volumeInfo.getRatingsCount() > 0) {
        int fullRating = (int) Math.round(volumeInfo.getAverageRating().doubleValue());
        System.out.print("User Rating: ");
        for (int i = 0; i < fullRating; ++i) {
          System.out.print("*");
        }
        System.out.println(" (" + volumeInfo.getRatingsCount() + " rating(s))");
      }
      // Price (if any).
      if (saleInfo != null && "FOR_SALE".equals(saleInfo.getSaleability())) {
        double save = saleInfo.getListPrice().getAmount() - saleInfo.getRetailPrice().getAmount();
        if (save > 0.0) {
          System.out.print("List: " + CURRENCY_FORMATTER.format(saleInfo.getListPrice().getAmount())
                  + "  ");
        }
        System.out.print("Google eBooks Price: "
                + CURRENCY_FORMATTER.format(saleInfo.getRetailPrice().getAmount()));
        if (save > 0.0) {
          System.out.print("  You Save: " + CURRENCY_FORMATTER.format(save) + " ("
                  + PERCENT_FORMATTER.format(save / saleInfo.getListPrice().getAmount()) + ")");
        }
        System.out.println();
      }
      // Access status.
      String accessViewStatus = volume.getAccessInfo().getAccessViewStatus();
      String message = "Additional information about this book is available from Google eBooks at:";
      if ("FULL_PUBLIC_DOMAIN".equals(accessViewStatus)) {
        message = "This public domain book is available for free from Google eBooks at:";
      } else if ("SAMPLE".equals(accessViewStatus)) {
        message = "A preview of this book is available from Google eBooks at:";
      }
      System.out.println(message);
      // Link to Google eBooks.
      System.out.println(volumeInfo.getInfoLink());
    }
    System.out.println("==========");
    System.out.println(
            volumes.getTotalItems() + " total results at http://books.google.com/ebooks?q="
                    + URLEncoder.encode(query, "UTF-8"));
  }
}
