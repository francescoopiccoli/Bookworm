package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BookInfo extends Application {

    Stage window;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //    Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        window = primaryStage;
        window.setTitle("Book Info");

        //book title
        TextArea displayTitle = new TextArea("book title"); //Book.getName()
        displayTitle.setPrefSize(100, 30);
        displayTitle.setEditable(false);
        displayTitle.setMouseTransparent(true);
        displayTitle.setFocusTraversable(false);


        //Authors in combo box
        String authors = Author.getName();
        ComboBox combo_box =
                new ComboBox(FXCollections
                        .observableArrayList(authors));

        combo_box.setOnAction(e -> combo_box.getValue());
        combo_box.setEditable(false);
        //TO-DO implement listener for selection change

        //book cover
        Image image = new Image("sample/300.png");
        ImageView iv = new ImageView(image);

        //tags
        Button tag1 = new Button("tag1");
        Button tag2 = new Button("tag2");
        HBox buttons = new HBox();
        buttons.getChildren().addAll(tag1,tag2);
        buttons.setSpacing(10);


        //short description
        TextArea description = new TextArea("Short Description");
        description.setText("description");
        description.setPrefColumnCount(15);
        description.setPrefHeight(100);
        description.setPrefWidth(90);
        description.setEditable(false);
        description.setMouseTransparent(true);
        description.setFocusTraversable(false);

        //Review Area

        TextArea review = new TextArea("review");
        review.setText("review");
        review.setPrefColumnCount(15);
        review.setPrefHeight(100);
        review.setPrefWidth(90);
        review.setEditable(true);

        Button add = new Button("Add review");

        //positions
        Pane root = new Pane();
        displayTitle.setLayoutX(130);
        displayTitle.setLayoutY(10);
        combo_box.setLayoutX(250);
        combo_box.setLayoutY(10);
        iv.setX(10);
        iv.setY(10);
        iv.setFitHeight(80);
        iv.setFitWidth(80);
        description.setLayoutX(10);
        description.setLayoutY(140);
        buttons.setLayoutX(10);
        buttons.setLayoutY(100);
        review.setLayoutX(180);
        review.setLayoutY(140);
        add.setLayoutY(250);
        add.setLayoutX(197);

        //display elements

        root.getChildren().addAll(displayTitle, combo_box, iv,description,buttons,review,add);
        Scene scene = new Scene(root, 300, 300);
        window.setScene(scene);
        window.show();
    }
}
