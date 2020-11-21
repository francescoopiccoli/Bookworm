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
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
//NOT USED
public class BookshelfWidget extends ScrollPane{
    private String currentFilter = "";
    private List<Bookshelf> bookshelves;
    private List<ImageView> covers = new ArrayList<>();
    private BookshelfView parent;
    private Image image;
    public BookshelfWidget(List<Bookshelf>bookshelves, BookshelfView parent){
        super();
        this.bookshelves = bookshelves;
        this.parent = parent;
        for(Bookshelf b : bookshelves){
            try {
                List<Book> list = DatabaseManager.getInstance().getBookShelfBooks(b.getId());
                if(list.isEmpty()){
                    image = new Image(getClass().getResource(BookListWidget.PLACEHOLDER_IMAGE_URI).toExternalForm());
                    ImageView img = new ImageView(image);
                    img.setFitHeight(200);
                    img.setFitWidth(150);

                    covers.add(img);
                }else {
                    image = new Image(list.get(0).getImageURL());
                    ImageView img = new ImageView(image);
                    img.setFitHeight(200);
                    img.setFitWidth(150);
                    covers.add(img);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
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
            imageView.setFitWidth(500);
            VBox centerVBox = new VBox();
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
            vb.setAlignment(Pos.CENTER);

            int width = (int) getWidth();
            int numColumns = getMaxColumns(width);

            int i = 0;
            HBox hb = new HBox();

            for(int j = 0; j<bookshelves.size();j++) {
                System.out.println(bookshelves.get(j).getName());
                if (i >= numColumns) {
                    i = 0;
                }
                if (i == 0) {
                    hb = new HBox();
                    hb.setAlignment(Pos.CENTER);
                    ImageView imgv = new ImageView(image);
                    imgv.setFitWidth(150);
                    imgv.setFitHeight(200);
                    vb.getChildren().add(hb);
                }

                // filtering
                if (filter != null
                        && !bookshelves.get(j).getName().toLowerCase().contains(filter.toLowerCase()))
                    continue;

                    Button button = new Button(bookshelves.get(j).getName(), covers.get(j));
                    //To FIx
                    button.setPrefWidth(250);
                    String bookshelfName = bookshelves.get(j).getName();
                    String bookshelfDesc = bookshelves.get(j).getDescription();
                    boolean isDefault = false;
                    if(j==0){
                        isDefault = true;
                    }
                if(isDefault==false){
                    button.setOnMouseClicked((event -> {
                        parent.setTop(parent.createTop2(bookshelfDesc));
                        parent.setCenter(parent.createCenter3(bookshelfName));
                    }));}
                else {
                    button.setOnMouseClicked((event -> {
                    parent.setTop(parent.createTop2(bookshelfDesc));
                    parent.setCenter(parent.createCenter2());
                    }));

                }

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
