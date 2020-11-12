package com.Bookworm.ui.widgets;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class StarWidget {

    public static final String STAR_EMPTY = "/Images/star-empty.png";
    public static final String STAR_FILLED = "/Images/star-filled.png";
    private static HBox starWidgetBox;
    private static Image emptyStar = new Image(StarWidget.class.getResourceAsStream(STAR_EMPTY));
    private static Image filledStar = new Image(StarWidget.class.getResourceAsStream(STAR_FILLED));


    public static HBox getStarWidget(){
        starWidgetBox = new HBox();
        ImageView emptyS = new ImageView(emptyStar);
        emptyS.setFitWidth(20);
        emptyS.setFitHeight(20);
        Button b;

        for(int i = 0; i < 5; i++){

            b = new Button();
            emptyS = new ImageView(emptyStar);
            emptyS.setFitWidth(20);
            emptyS.setFitHeight(20);
            b.setGraphic(emptyS);
            starWidgetBox.getChildren().add(b);
            starWidgetBox.setHgrow(b, Priority.ALWAYS);

        }
        return starWidgetBox;
    }


}
