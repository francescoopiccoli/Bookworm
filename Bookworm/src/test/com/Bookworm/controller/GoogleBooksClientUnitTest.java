package com.Bookworm.controller;

import com.Bookworm.model.Book;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.testing.json.GoogleJsonResponseExceptionFactoryTesting;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.books.Books;
import com.google.api.services.books.model.Volumes;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class GoogleBooksClientUnitTest {

    private static final JsonFactory JSON_FACTORY = new JacksonFactory().getDefaultInstance();
    private static final int HTTP_CODE_NOT_FOUND = 404;
    private static final String REASON_PHRASE_NOT_FOUND = "NOT FOUND";
    static Book book1 = new Book("The Great Gatsby", "Francis Scott Fitzgerald", "");
    Books.Volumes.List volumesList = null;
    private static LinkedList<Book> testSearchBook;
    GoogleBooksClient googleBooksClient = new GoogleBooksClient();
    private static final Logger LOGGER = Logger.getLogger(GoogleBooksClient.class.getName());
    //added random value to the query, need to check database changes
    private static final String query = "The Great Gatsby";

    @Test
    public void connectToAPI() throws IOException, GeneralSecurityException {
        //check json factory object is not null
        assertNotNull(JSON_FACTORY);
        //check connection
        GoogleJsonResponseException exception =
                GoogleJsonResponseExceptionFactoryTesting.newMock(
                        JSON_FACTORY, HTTP_CODE_NOT_FOUND, REASON_PHRASE_NOT_FOUND);
        assertEquals(HTTP_CODE_NOT_FOUND, exception.getStatusCode());
        assertEquals(REASON_PHRASE_NOT_FOUND, exception.getStatusMessage());
        assertNotNull(googleBooksClient.connectToAPI(JSON_FACTORY));

    }

    @Test
    public void getFoundBooks() throws NullPointerException, IOException, Exception {

        try {
            volumesList = googleBooksClient.connectToAPI(JSON_FACTORY)
                    .volumes().list(query);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
        Volumes volumes = null;
        volumes = volumesList.execute();
        assertNotNull(volumes);
        assertNotNull(googleBooksClient.getFoundBooks(JSON_FACTORY, query));

    }

    @Test

    public void searchBooks() throws NullPointerException {
        testSearchBook = googleBooksClient.searchBooks(query);
        assertNotNull(testSearchBook);
    }
}

