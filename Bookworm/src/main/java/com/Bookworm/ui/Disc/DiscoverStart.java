package com.Bookworm.ui.Disc;


import com.Bookworm.ui.Disc.Discover;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class DiscoverStart extends Application {
    Stage window;
    Scene scene;
    BorderPane layout;
    ScrollPane scrollPane;
    @Override
    public void start(Stage primaryStage) throws Exception{

        window = primaryStage;
        //Since discover is first scene show that first and set it as initial scene; layout is the current discover scene.
        primaryStage.setTitle("Bookworm - Discover");
        layout = new BorderPane();
        //Create an instance of Discover to fill the borderpane with its functions
        Discover disc = new Discover();
        layout.setTop(disc.createTopDisc());
        layout.setCenter(disc.getCenterDisc());
        scene = new Scene(layout, 1800,750);
        //Sets css to the discover scene
        scene.getStylesheets().add("/com/Bookworm/ui/Disc/style.css");
        window.setScene(scene);
        //Other characteristics for window of the application.
        window.setResizable(false);
        window.show();
        primaryStage.show();



    }




    public static void main(String[] args) {
        launch(args);
    }
}
