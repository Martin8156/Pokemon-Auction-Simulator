package src.client;

import src.sharedClass.AuctionItem;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AuctionClient extends Application {

    private StringProperty itemDataProperty = new SimpleStringProperty();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Auction Client");

        // UI components
        Label itemLabel = new Label("Item Data:");
        Label itemDataLabel = new Label();

        // Bind the label's text property to the itemDataProperty
        itemDataLabel.textProperty().bind(itemDataProperty);

        VBox vBox = new VBox(itemLabel, itemDataLabel);
        Scene scene = new Scene(vBox, 300, 200);

        primaryStage.setScene(scene);
        primaryStage.show();

        // create display here
        // Display the initial data in the UI
    }

    // Method to update the UI with new item data received from the server
    public void updateItemData(String newData) {
        // Run on the JavaFX application thread to update UI components
        Platform.runLater(() -> {
            itemDataProperty.set(newData);
        });
    }
}
