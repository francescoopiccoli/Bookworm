package com.Bookworm;

import com.Bookworm.ui.Disc.Discover;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application {

	private Region currentView = new Discover();
	private BorderPane mainPane;
	
    @Override
    public void start(Stage stage) {
        var javaVersion = SystemInfo.javaVersion();
        var javafxVersion = SystemInfo.javafxVersion();

        mainPane = new BorderPane();
        mainPane.setTop(_generateTopBar());
        mainPane.setCenter(_generateContent(0));
        mainPane.setBottom(_generateSidebar());
        var scene = new Scene(mainPane, 800, 600);
        scene.getStylesheets().add("/com/Bookworm/ui/Disc/style.css");
        stage.setScene(scene);
        stage.show();
    }
    
    private VBox _generateTopBar() {
		VBox vBox = new VBox();
        vBox.setPadding(new Insets(20));
		vBox.setAlignment(Pos.CENTER);
        Text title = new Text("Bookworm");
        title.setFont(Font.font("Noto Sans Condensed", FontWeight.BOLD, 28));
        vBox.getChildren().add(title);

        return vBox;
    }
    
    private Region _generateContent(int id) {
		return currentView;
    }
	
	private HBox _generateSidebar() {
    	HBox hBox = new HBox();
        hBox.setPadding(new Insets(10));
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(8);

        ToggleGroup toggleGroup = new ToggleGroup();
        
        ToggleButton options[] = new ToggleButton[] {
            new ToggleButton("Home"),
            new ToggleButton("Discover"),
            new ToggleButton("Rankings"),
            new ToggleButton("Lists"),
            new ToggleButton("Profile")};

        String defaultView = "Home";
        for (int i=0; i<options.length; i++) {
        	if(defaultView.equals(options[i].getText()))
        		options[i].setSelected(true);
        		
            hBox.setMargin(options[i], new Insets(0, 0, 0, 8));
            hBox.getChildren().add(options[i]);
            options[i].setOnAction((event) -> {
                currentView = new Label("viewing "+((ToggleButton) event.getSource()).getText());
                mainPane.setCenter(currentView);
            });
            options[i].setToggleGroup(toggleGroup);
        }

        return hBox;
    }
	

    public static void main(String[] args) {
        launch();
    }

}