package src.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import src.sharedClass.AuctionItem;

import java.io.FileInputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UIController implements Initializable {
    @FXML
    private Label paneTitle;
    @FXML
    private ChoiceBox<String> myChoiceBox;
    @FXML
    private GridPane displayPane;

    private String[] dropDownMenu = {"Available Pokemon", "Closed Bids"};
    private Client client;

    /**
     * Setter
     * @param client
     */
    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        myChoiceBox.getItems().addAll(dropDownMenu);
        myChoiceBox.setOnAction(this::selectPane);
    }
    public void selectPane(ActionEvent event) {
        String currMenu = myChoiceBox.getValue();
        paneTitle.setText(currMenu);
    }

    public void updateGridPane(List<AuctionItem> receivedPokemonList) {
        // Update the GridPane based on the received data
        Platform.runLater(() -> {
            for (int i = 0; i < receivedPokemonList.size(); i++) {
                AuctionItem pokemon = receivedPokemonList.get(i);
                AnchorPane anchorPane = createAnchorPaneForPokemon(pokemon); // create anchorpane that will be added to gridpane
                int row = i / 2; // Calculate the row based on the index
                int col = i % 2; // Calculate the column based on the index
                displayPane.add(anchorPane, col, row); // gridpane size is fixed for now
            }
        });
    }

    // hard coded elements are just the picture added and the placement of the picture in pink circle
    private AnchorPane createAnchorPaneForPokemon(AuctionItem pokemon) {
        AnchorPane anchorPane = new AnchorPane();

        Label nameLabel = new Label("Pokemon Name: " + pokemon.getName());
        Circle circle = new Circle(55, Color.LIGHTPINK);
        Label bidLabel = new Label("Current Bid: " + pokemon.getMinBid());
        Button bidButton = new Button("Bid");

        try {
            Image image;
            ImageView imageView = new ImageView();

            // hardcoded switch block
            switch (pokemon.getName()) {
                case "Gengar":
                    image = new Image(new FileInputStream("/Users/brian/IdeaProjects/fa-23-final-project-Martin8156/src/src/sharedClass/Gengar.png"));
                    imageView = new ImageView(image);
                    imageView.setX(50);
                    imageView.setY(25);
                    imageView.setFitHeight(100);
                    imageView.setFitWidth(100);
                    imageView.setPreserveRatio(true);
                    break;
                case "Lucario":
                    image = new Image(new FileInputStream("/Users/brian/IdeaProjects/fa-23-final-project-Martin8156/src/src/sharedClass/Lucario.png"));
                    imageView = new ImageView(image);
                    imageView.setX(50);
                    imageView.setY(25);
                    imageView.setFitHeight(100);
                    imageView.setFitWidth(100);
                    imageView.setPreserveRatio(true);
                    break;
                case "Mewtwo":
                    image = new Image(new FileInputStream("/Users/brian/IdeaProjects/fa-23-final-project-Martin8156/src/src/sharedClass/Mewtwo.png"));
                    imageView = new ImageView(image);
                    imageView.setX(50);
                    imageView.setY(25);
                    imageView.setFitHeight(100);
                    imageView.setFitWidth(100);
                    imageView.setPreserveRatio(true);
                    break;
                case "Shaymin":
                    image = new Image(new FileInputStream("/Users/brian/IdeaProjects/fa-23-final-project-Martin8156/src/src/sharedClass/Shaymin.png"));
                    imageView = new ImageView(image);
                    imageView.setX(50);
                    imageView.setY(25);
                    imageView.setFitHeight(100);
                    imageView.setFitWidth(100);
                    imageView.setPreserveRatio(true);
                    break;
                case "Darkrai":
                    image = new Image(new FileInputStream("/Users/brian/IdeaProjects/fa-23-final-project-Martin8156/src/src/sharedClass/Darkrai.png"));
                    imageView = new ImageView(image);
                    imageView.setX(50);
                    imageView.setY(25);
                    imageView.setFitHeight(100);
                    imageView.setFitWidth(100);
                    imageView.setPreserveRatio(true);
                    break;
                case "Cyndaquil":
                    image = new Image(new FileInputStream("/Users/brian/IdeaProjects/fa-23-final-project-Martin8156/src/src/sharedClass/Cyndaquil.png"));
                    imageView = new ImageView(image);
                    imageView.setX(50);
                    imageView.setY(25);
                    imageView.setFitHeight(115);
                    imageView.setFitWidth(115);
                    imageView.setPreserveRatio(true);
                    break;
                default:
            }


            anchorPane.getChildren().addAll(nameLabel, circle, bidLabel, bidButton, imageView);

            anchorPane.setMaxSize(585, 383);

            AnchorPane.setTopAnchor(nameLabel, 25.0);
            AnchorPane.setLeftAnchor(nameLabel, 135.0);

            AnchorPane.setTopAnchor(circle, 3.0);
            AnchorPane.setLeftAnchor(circle, 5.0);

            // hardcoded switch block
            switch (pokemon.getName()) {
                case "Gengar":
                    AnchorPane.setTopAnchor(imageView, 9.0);
                    AnchorPane.setLeftAnchor(imageView, 14.0);
                    break;
                case "Lucario":
                    AnchorPane.setTopAnchor(imageView, 5.0);
                    AnchorPane.setLeftAnchor(imageView, 12.0);
                    break;
                case "Mewtwo":
                    AnchorPane.setTopAnchor(imageView, 9.0);
                    AnchorPane.setLeftAnchor(imageView, 20.0);
                    break;
                case "Shaymin":
                    AnchorPane.setTopAnchor(imageView, 8.0);
                    AnchorPane.setLeftAnchor(imageView, 12.0);
                    break;
                case "Darkrai":
                    AnchorPane.setTopAnchor(imageView, 9.0);
                    AnchorPane.setLeftAnchor(imageView, 12.0);
                    break;
                case "Cyndaquil":
                    AnchorPane.setTopAnchor(imageView, 2.0);
                    AnchorPane.setLeftAnchor(imageView, 4.5);
                    break;
                default:
            }

            AnchorPane.setTopAnchor(bidLabel, 45.0);
            AnchorPane.setLeftAnchor(bidLabel, 135.0);

            AnchorPane.setTopAnchor(bidButton, 65.0);
            AnchorPane.setLeftAnchor(bidButton, 135.0);



        }
        catch(Exception e){
            e.printStackTrace();
        }
        return anchorPane;


    }


}
