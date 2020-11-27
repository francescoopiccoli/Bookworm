package com.Bookworm.controller;

import com.Bookworm.model.Book;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.testing.json.GoogleJsonResponseExceptionFactoryTesting;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GoogleBooksClientUnitTest {

    private static final JsonFactory JSON_FACTORY = new JacksonFactory().getDefaultInstance();
    private static final int HTTP_CODE_NOT_FOUND = 404;
    private static final String REASON_PHRASE_NOT_FOUND = "NOT FOUND";
    private static LinkedList<Book> testFoundBook;
    private static LinkedList<Book> testSearchBook;
    GoogleBooksClient googleBooksClient = new GoogleBooksClient();
    //added random value to the query, need to check database changes
    private static final String query="book";

    @Test
    public void connectToAPI() throws IOException {
        //check json factory object is not null
        assertNotNull(JSON_FACTORY);

        //check connection
        GoogleJsonResponseException exception =
                GoogleJsonResponseExceptionFactoryTesting.newMock(
                        JSON_FACTORY, HTTP_CODE_NOT_FOUND, REASON_PHRASE_NOT_FOUND);
        assertEquals(HTTP_CODE_NOT_FOUND, exception.getStatusCode());
        assertEquals(REASON_PHRASE_NOT_FOUND, exception.getStatusMessage());
    }

    @Test
    public void getFoundBooks() throws NullPointerException, IOException, Exception  {
        testFoundBook = googleBooksClient.getFoundBooks(JSON_FACTORY, query);
        assertNotNull(testFoundBook);
    }

    @Test

    public void searchBooks() throws NullPointerException {
        testSearchBook = googleBooksClient.searchBooks(query);
        assertNotNull(testSearchBook);
    }
}

