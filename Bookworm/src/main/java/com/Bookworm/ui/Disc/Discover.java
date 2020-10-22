package com.Bookworm.ui.Disc;

import com.Bookworm.APImanager;
import com.Bookworm.model.Book;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;

// !!! to fix  input with return null (no results found)

public class Discover extends BorderPane {

    BorderPane layout;
    ScrollPane scrollPane;

    public static LinkedList<Book> booklist = new LinkedList<Book>();

    public Discover() {
        //Create an instance of Discover to fill the borderpane with its functions
        setTop(createTopDisc());
        setCenter(getCenterDisc());

    }

    public Node getCenterDisc() {
        //Temporary image to replace covers
        Image image = new Image(getClass().getResourceAsStream("/Images/placeholder.png"));
        //Create vertical box will align all elements one under the other
        VBox vb = new VBox();

        vb.getStyleClass().add("vbrect");
        vb.setSpacing(50);
        //Create horizontal box will align all elements one next to the other
        HBox hb = new HBox();

        //Button for all single books to be created
        Button rect = new Button();


        //Add a scroll pane so scrolling is made possible
        ScrollPane scrollPane;


        Label label  = new Label("Discover new books!");
        label.getStyleClass().add("discoverLabel");
        vb.getChildren().add(label);
        //will create (currently 3) rows with at most 5 books on each row
       // System.out.println(booklist);
        int counter = 0;
        if(!booklist.isEmpty()){
            hb = new HBox();
            for (Book b : booklist){
                //This will be replaced with the function giving us the cover of the book and also setting the reaction to clicking the "button"
                if(counter == 4){
                    counter = 0;
                    vb.getChildren().add(hb);
                    hb = new HBox();
                    ImageView imageView = new ImageView(b.getImageURL());
                    rect = new Button(b.getName(),imageView);
                    rect.getStyleClass().add("rect");
                    rect.setOnAction(event -> {bookinfo(b.getName(),b.getAuthor(),b.getDescription());});
                    hb.getChildren().add(rect);
                    counter++;
                }
                else{
                    ImageView imageView = new ImageView(b.getImageURL());
                    rect = new Button(b.getName(),imageView);
                    rect.getStyleClass().add("rect");
                    rect.setOnAction(event -> {bookinfo(b.getName(),b.getAuthor(),b.getDescription());});
                    hb.getChildren().add(rect);
                    counter++;
                }

            }}
            vb.getChildren().add(hb);


        scrollPane = new ScrollPane(vb);
        scrollPane.getStyleClass().add("scrollpane");

        return  scrollPane;



    }

    private void bookinfo(String title, String author, String description) {
        Stage bookPage = new Stage();
        VBox vb = new VBox();
        Text titlebook = new Text(title);
        Text authrobook = new Text(author);
        Text descbook = new Text(description);
        vb.getChildren().addAll(titlebook,authrobook,descbook);
        bookPage.setTitle("Book info: "+title);
        Scene scenebook = new Scene(vb,500,100);
        bookPage.setScene(scenebook);
        bookPage.show();

    }

    public Node createTopDisc() {
        VBox vb = new VBox();
        HBox hb = new HBox();
        //VBox v = new VBox();
        TextField search = new TextField();
        TextField filters = new TextField();

        search.setOnAction(event -> {
                    booklist = APImanager.searchBooks(search.getText());
                    if(!booklist.isEmpty()){
                    refresh();}
        });

        Button apply = new Button("Filter");
        apply.setOnAction(event -> {filter();});
        hb.getChildren().addAll(filters,apply);
        vb.getChildren().addAll(search,hb);
        vb.setSpacing(5.5);
        return  vb;
    }
    private void filter() {
        setCenter(centersearch());
    }

    private Node centersearch() {
        VBox vb = new VBox();
        Text resulttext = new Text("THIS WILL BE YOUR FILTERED RESULTS. IN DEVELOPMENT");
        vb.getChildren().add(resulttext);
        return vb;
    }
    public void refresh() {
        setCenter(getCenterDisc());
    }
}
