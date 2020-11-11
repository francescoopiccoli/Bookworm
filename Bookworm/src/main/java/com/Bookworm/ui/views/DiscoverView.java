package com.Bookworm.ui.views;

import com.Bookworm.controller.GoogleBooksClient;
import com.Bookworm.model.Book;
import com.Bookworm.ui.widgets.BookWidget;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import java.util.LinkedList;
import java.util.List;

// !!! to fix  input with return null (no results found)

public class DiscoverView extends BorderPane {

    BorderPane layout;
    ScrollPane scrollPane;
    // sicuro che sia static?
    public static List<Book> bookList = new LinkedList<>();
    private boolean loadingStatus;
    private TextField searchWidget;
    private Label searchPlaceholder;

    public static List<Book> getbookList() {
        return bookList;
    }

    public static void setbookList(List<Book> bookList) {
        DiscoverView.bookList = bookList;
    }

    private void setLoadingStatus(boolean loadingStatus) {
        this.loadingStatus = loadingStatus;
        // ugly af - better have some kind of loading widget
        searchWidget.setVisible(!loadingStatus);
        searchPlaceholder.setVisible(loadingStatus);
    }
    private boolean getLoadingStatus() {
        return loadingStatus;
    }



    public DiscoverView() {
        //Create an instance of Discover to fill the borderpane with its functions
        setTop(createTopDisc());
        setCenter(getCenterDisc());

    }

    public Node getCenterDisc() {
        //Temporary image to replace covers
        //Create vertical box will align all elements one under the other
        VBox vb = new VBox();

        vb.getStyleClass().add("vbrect");
        vb.setSpacing(50);
        //Create horizontal box will align all elements one next to the other
        HBox hb = new HBox();

        //Button for all single books to be created
        //Button rect;


        //Add a scroll pane so scrolling is made possible
        ScrollPane scrollPane;


        Label label  = new Label("Discover new books!");
        label.getStyleClass().add("discoverLabel");
        vb.getChildren().add(label);
        //will create (currently 3) rows with at most 5 books on each row
       // System.out.println(bookList);
        int counter = 0;
        if(!bookList.isEmpty()){
            hb = new HBox();
            HBox.setMargin(hb, new Insets(10));
            for (Book b : bookList){
                //This will be replaced with the function giving us the cover of the book and also setting the reaction to clicking the "button"
                if(counter == 4) {
                    counter = 0;
                    vb.getChildren().add(hb);
                    hb = new HBox();
                    HBox.setMargin(hb, new Insets(10));
                    BookWidget bookWidget = new BookWidget(b);

                    Book finalBook = b;
                    bookWidget.setOnMouseClicked(event -> {
                        BookInfoView.spawnWindow(finalBook, null);
                    });
                    hb.getChildren().add(bookWidget);
                    counter++;
                } else {
                    BookWidget bookWidget = new BookWidget(b);

                    Book finalBook = b;
                    bookWidget.setOnMouseClicked(event -> {
                        BookInfoView.spawnWindow(finalBook, null);
                    });
                    hb.getChildren().add(bookWidget);
                    counter++;
                }



            }}
            vb.getChildren().add(hb);


        scrollPane = new ScrollPane(vb);
        scrollPane.getStyleClass().add("scrollpane");

        return  scrollPane;



    }

    public Node createTopDisc() {
        VBox vb = new VBox();
        HBox hb = new HBox();
        searchWidget = new TextField();
        searchWidget.setOnAction(event -> {
            RefreshThread thread = new RefreshThread(this, searchWidget.getText());
            thread.start();
        });

        searchPlaceholder = new Label("Search in progress...");
        searchPlaceholder.setVisible(false);

        vb.getChildren().addAll(searchWidget,searchPlaceholder,hb);
        vb.setSpacing(5.5);
        return  vb;
    }
    public void refresh() {
        setCenter(getCenterDisc());
    }

    private class RefreshThread extends Thread {
        private DiscoverView d;
        private String query;
        public RefreshThread(DiscoverView d, String query) {
            this.d = d;
            this.query = query;
        }
        public void run() {
            if(d.getLoadingStatus())
                return; // don't mess with multiple searches at once
            setLoading(true);

            List<Book> bookList = GoogleBooksClient.searchBooks(query);
            if(bookList != null && !bookList.isEmpty()){
                d.setbookList(bookList);
                Node centerDisc = d.getCenterDisc();
                Platform.runLater(() -> d.setCenter(centerDisc));
            }
            setLoading(false);
        }

        private void setLoading(boolean b) {
            Platform.runLater(() -> d.setLoadingStatus(b));
        }
    }


}
