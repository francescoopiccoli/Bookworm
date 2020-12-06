package com.Bookworm.ui.widgets;

import com.Bookworm.App;
import com.Bookworm.controller.DatabaseManager;
import com.Bookworm.model.Book;
import com.Bookworm.model.Bookshelf;
import com.Bookworm.ui.views.BookInfoView;
import com.Bookworm.ui.views.BookListView;
import com.Bookworm.ui.views.BookshelfView;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

//NOT USED
public class BookshelfWidget extends ScrollPane{
    private String currentFilter = "";
    private List<Bookshelf> bookshelves;
    private List<ImageView> covers = new ArrayList<>();
    private BookshelfView parent;
    private Image image;
    private static final Logger LOGGER = Logger.getLogger(BookshelfWidget.class.getName());

    public static final String BOOKSHELF_IMAGE_URI = "/Images/bookshelfPlaceholder.png";

    public BookshelfWidget(List<Bookshelf>bookshelves, BookshelfView parent){
        super();
        this.bookshelves = bookshelves;
        this.parent = parent;
        for(Bookshelf b : bookshelves){
            try {
                List<Book> list = DatabaseManager.getInstance().getBookShelfBooks(b.getId());

                if(list.isEmpty()){
                    image = new Image(getClass().getResource(BOOKSHELF_IMAGE_URI).toExternalForm());
                } else {
                    image = new Image(list.get(0).getImageURL());
                }

                ImageView img = new ImageView(image);
                img.setFitHeight(100);
                img.setFitWidth(70);
                covers.add(img);
            } catch (SQLException | ClassNotFoundException throwables) {
                LOGGER.log( Level.SEVERE, throwables.toString(), throwables);
                //throwables.printStackTrace();
            } //e.printStackTrace();

        }
        setFitToHeight(true);
        setFitToWidth(true);
        widthProperty().addListener((e) -> updateList(null));
        updateList();

    }
    public void updateList() throws NullPointerException {
        updateList("");
    }

    public  void updateList(String filter) throws NullPointerException {
        if (filter != null)
            currentFilter = filter;

        if (bookshelves == null || bookshelves.size() == 0) {

            image = new Image(getClass().getResource(BookListWidget.PLACEHOLDER_IMAGE_URI).toExternalForm());
            ImageView imageView = new ImageView(this.image);
            imageView.setFitHeight(250);
            imageView.setFitWidth(150);
            imageView = new ImageView(this.image);


            VBox centerVBox = new VBox();
            centerVBox.getStyleClass().add("inner");
            HBox hBox = new HBox();
            centerVBox.getChildren().addAll(hBox, imageView);
            centerVBox.setSpacing(50);
            centerVBox.setAlignment(Pos.BASELINE_CENTER);
            setContent(centerVBox);
            try {
                image = new Image(getClass().getResource(BookListWidget.PLACEHOLDER_IMAGE_URI).toExternalForm());
                imageView.setImage(image);
            } catch (IllegalArgumentException | NullPointerException e) {
                image = new Image(getClass().getResource(BookListWidget.PLACEHOLDER_IMAGE_URI).toExternalForm());
            }

        } else {
            VBox vb = new VBox();
            vb.getStyleClass().add("inner");
            vb.setAlignment(Pos.CENTER);

            int width = (int) getWidth();
            int numColumns = getMaxColumns(width);

            int i = 0;
            HBox hb = new HBox();

            for(int j = 0; j<bookshelves.size();j++) {
                if (i >= numColumns) {
                    i = 0;
                }
                if (i == 0) {
                    hb = new HBox();
                    hb.setSpacing(20);
                    hb.setAlignment(Pos.CENTER);
                    ImageView imgv = new ImageView(image);
                    imgv.setFitWidth(180);
                    imgv.setFitHeight(200);


                    vb.getChildren().add(hb);
                }

                // filtering
                if (filter != null
                        && !bookshelves.get(j).getName().toLowerCase().contains(filter.toLowerCase()))
                    continue;

                    Button button = new Button(bookshelves.get(j).getName(), covers.get(j));
                    button.getStyleClass().add("bookshelfWidget");
                    //To FIx
                    button.setPrefWidth(250);
                    button.setAlignment(Pos.CENTER_LEFT);
                    String bookshelfName = bookshelves.get(j).getName();
                    String bookshelfDesc = bookshelves.get(j).getDescription();


                    int finalJ = j;
                    button.setOnMouseClicked((event -> {
                        parent.setTop(parent.createTop2(bookshelfDesc, bookshelves.get(finalJ)));
                        parent.setCenter(parent.createCenter3(bookshelfName, bookshelfDesc));
                    }));

                    HBox.setMargin(button, new Insets(20));
                    hb.getChildren().add(button);

                i++;
            }

            // fill with whitespace
            while (i != 0 && i < numColumns) {
                BookWidget b = new BookWidget(null);
                b.setVisible(false);
                HBox.setMargin(b, new Insets(20));
                hb.getChildren().add(b);
                i++;
            }

            setContent(vb);
        }
    }

    private int getMaxColumns(int width) {
        return width / 230; // width + margin
    }
}
