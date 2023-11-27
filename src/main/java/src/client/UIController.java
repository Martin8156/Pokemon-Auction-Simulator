package src.client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import src.sharedClass.AuctionItem;
import src.sharedClass.BidInfo;

import java.io.FileInputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.Duration;
import java.time.LocalTime;

public class UIController implements Initializable {
    @FXML
    private Label paneTitle;
    @FXML
    private ChoiceBox<String> myChoiceBox;
    @FXML
    private GridPane displayPane;

    private String[] dropDownMenu = {"Available Pokemon", "Closed Bids", "Logout"};
    private LoginScreenController loginScreenController;
    private Client client;
    private Stage mainView;
    private Scene personalScene;
    private boolean guest;


    /**
     * Setter
     * @param client
     */
    public void setClient(Client client) {
        this.client = client;
    }
    public void setStage(Stage mainView) {
        this.mainView = mainView;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        myChoiceBox.getItems().addAll(dropDownMenu);
        myChoiceBox.setOnAction(this::selectPane);
    }
    public void selectPane(ActionEvent event) {
        String currMenu = myChoiceBox.getValue();
        if (currMenu.equals("Logout")){
            loginScreenController.returnToThisScene();
            loginScreenController.clearUsernameField();
            loginScreenController.clearPasswordField();
        }
        if (currMenu.equals("Available Pokemon")) {
            updateGridPane(client.getReceivedPokemonList());

        }
        if (currMenu.equals("Closed Bids")) {
            updateGridPaneForClosedBids(client.getReceivedClosedBids());
        }

        paneTitle.setText(currMenu);


    }


    public void updateGridPane(List<AuctionItem> receivedPokemonList) {
        // Update the GridPane based on the received data
        Platform.runLater(() -> {
            // Clear the existing content in the displayPane
            displayPane.getChildren().clear();

            for (int i = 0; i < receivedPokemonList.size(); i++) {
                AuctionItem pokemon = receivedPokemonList.get(i);
                AnchorPane anchorPane = createAnchorPaneForPokemon(pokemon); // create anchorpane that will be added to gridpane
                int row = i / 2; // Calculate the row based on the index
                int col = i % 2; // Calculate the column based on the index
                displayPane.add(anchorPane, col, row); // gridpane size and length is fixed for now (6 pokemon displayed only)
            }
        });
    }
    // hard coded elements are just the picture added and the placement of the picture in pink circle
    private AnchorPane createAnchorPaneForPokemon(AuctionItem pokemon) {
        AnchorPane anchorPane = new AnchorPane();

        Label nameLabel = new Label("Pokemon Name: " + pokemon.getName());
        Circle circle = new Circle(55, Color.LIGHTPINK);
        Label bidLabel = new Label("Current Bid: " + pokemon.getMinBid());
        Label maxBid = new Label("Buy Now Price: " + pokemon.getBuyNowPrice());
        Button bidButton = new Button("View");

        try {
            Image image;
            ImageView imageView = new ImageView();

            // hardcoded switch block
            switch (pokemon.getName()) {
                case "Gengar":
                    image = new Image(new FileInputStream("/Users/brian/IdeaProjects/fa-23-final-project-Martin8156/src/main/resources/Gengar.png"));
                    imageView = new ImageView(image);
                    imageView.setX(50);
                    imageView.setY(25);
                    imageView.setFitHeight(100);
                    imageView.setFitWidth(100);
                    imageView.setPreserveRatio(true);
                    break;
                case "Lucario":
                    image = new Image(new FileInputStream("/Users/brian/IdeaProjects/fa-23-final-project-Martin8156/src/main/resources/Lucario.png"));
                    imageView = new ImageView(image);
                    imageView.setX(50);
                    imageView.setY(25);
                    imageView.setFitHeight(100);
                    imageView.setFitWidth(100);
                    imageView.setPreserveRatio(true);
                    break;
                case "Mewtwo":
                    image = new Image(new FileInputStream("/Users/brian/IdeaProjects/fa-23-final-project-Martin8156/src/main/resources/Mewtwo.png"));
                    imageView = new ImageView(image);
                    imageView.setX(50);
                    imageView.setY(25);
                    imageView.setFitHeight(100);
                    imageView.setFitWidth(100);
                    imageView.setPreserveRatio(true);
                    break;
                case "Shaymin":
                    image = new Image(new FileInputStream("/Users/brian/IdeaProjects/fa-23-final-project-Martin8156/src/main/resources/Shaymin.png"));
                    imageView = new ImageView(image);
                    imageView.setX(50);
                    imageView.setY(25);
                    imageView.setFitHeight(100);
                    imageView.setFitWidth(100);
                    imageView.setPreserveRatio(true);
                    break;
                case "Darkrai":
                    image = new Image(new FileInputStream("/Users/brian/IdeaProjects/fa-23-final-project-Martin8156/src/main/resources/Darkrai.png"));
                    imageView = new ImageView(image);
                    imageView.setX(50);
                    imageView.setY(25);
                    imageView.setFitHeight(100);
                    imageView.setFitWidth(100);
                    imageView.setPreserveRatio(true);
                    break;
                case "Cyndaquil":
                    image = new Image(new FileInputStream("/Users/brian/IdeaProjects/fa-23-final-project-Martin8156/src/main/resources/Cyndaquil.png"));
                    imageView = new ImageView(image);
                    imageView.setX(50);
                    imageView.setY(25);
                    imageView.setFitHeight(115);
                    imageView.setFitWidth(115);
                    imageView.setPreserveRatio(true);
                    break;
                default:
            }

            bidButton.setOnAction(event -> handleBidButton(pokemon));
            //anchorPane.getChildren().addAll(nameLabel, circle, bidLabel, bidButton, imageView, maxBid);
            anchorPane.setMaxSize(585, 383);


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


            AnchorPane.setTopAnchor(circle, 3.0);
            AnchorPane.setLeftAnchor(circle, 5.0);

            AnchorPane.setTopAnchor(nameLabel, 15.0);
            AnchorPane.setLeftAnchor(nameLabel, 135.0);

            AnchorPane.setTopAnchor(maxBid, 35.0);
            AnchorPane.setLeftAnchor(maxBid, 135.0);

            AnchorPane.setTopAnchor(bidLabel, 55.0);
            AnchorPane.setLeftAnchor(bidLabel, 135.0);

            AnchorPane.setTopAnchor(bidButton, 75.0);
            AnchorPane.setLeftAnchor(bidButton, 135.0);

            // trying to do timer
            Label countdownTest = new Label(String.valueOf(pokemon.getTimeRemaining() + " mins left"));
            AnchorPane.setTopAnchor(countdownTest, 2.5);
            AnchorPane.setLeftAnchor(countdownTest, 27.0);
            anchorPane.getChildren().addAll(nameLabel, circle, bidLabel, bidButton, imageView, maxBid, countdownTest);


        }
        catch(Exception e){
            e.printStackTrace();
        }
        return anchorPane;


    }

    public void updateGridPaneForClosedBids(List<AuctionItem> receivedClosedBids) {
        // Update the GridPane based on the received data
        Platform.runLater(() -> {
            // Clear the existing content in the displayPane
            displayPane.getChildren().clear();
            if (receivedClosedBids == null) {return;}
            for (int i = 0; i < receivedClosedBids.size(); i++) {
                AuctionItem pokemon = receivedClosedBids.get(i);
                AnchorPane anchorPane = createAnchorPaneForClosedBids(pokemon); // create anchorpane that will be added to gridpane
                int row = i / 2; // Calculate the row based on the index
                int col = i % 2; // Calculate the column based on the index
                displayPane.add(anchorPane, col, row); // gridpane size and length is fixed for now (6 pokemon displayed only)
            }
        });
    }
    private AnchorPane createAnchorPaneForClosedBids(AuctionItem pokemon) {
        AnchorPane anchorPane = new AnchorPane();

        Label nameLabel = new Label("Pokemon Name: " + pokemon.getName());
        Circle circle = new Circle(55, Color.LIGHTPINK);
        Button bidButton = new Button("View");

        try {
            Image image;
            ImageView imageView = new ImageView();

            // hardcoded switch block
            switch (pokemon.getName()) {
                case "Gengar":
                    image = new Image(new FileInputStream("/Users/brian/IdeaProjects/fa-23-final-project-Martin8156/src/main/resources/Gengar.png"));
                    imageView = new ImageView(image);
                    imageView.setX(50);
                    imageView.setY(25);
                    imageView.setFitHeight(100);
                    imageView.setFitWidth(100);
                    imageView.setPreserveRatio(true);
                    break;
                case "Lucario":
                    image = new Image(new FileInputStream("/Users/brian/IdeaProjects/fa-23-final-project-Martin8156/src/main/resources/Lucario.png"));
                    imageView = new ImageView(image);
                    imageView.setX(50);
                    imageView.setY(25);
                    imageView.setFitHeight(100);
                    imageView.setFitWidth(100);
                    imageView.setPreserveRatio(true);
                    break;
                case "Mewtwo":
                    image = new Image(new FileInputStream("/Users/brian/IdeaProjects/fa-23-final-project-Martin8156/src/main/resources/Mewtwo.png"));
                    imageView = new ImageView(image);
                    imageView.setX(50);
                    imageView.setY(25);
                    imageView.setFitHeight(100);
                    imageView.setFitWidth(100);
                    imageView.setPreserveRatio(true);
                    break;
                case "Shaymin":
                    image = new Image(new FileInputStream("/Users/brian/IdeaProjects/fa-23-final-project-Martin8156/src/main/resources/Shaymin.png"));
                    imageView = new ImageView(image);
                    imageView.setX(50);
                    imageView.setY(25);
                    imageView.setFitHeight(100);
                    imageView.setFitWidth(100);
                    imageView.setPreserveRatio(true);
                    break;
                case "Darkrai":
                    image = new Image(new FileInputStream("/Users/brian/IdeaProjects/fa-23-final-project-Martin8156/src/main/resources/Darkrai.png"));
                    imageView = new ImageView(image);
                    imageView.setX(50);
                    imageView.setY(25);
                    imageView.setFitHeight(100);
                    imageView.setFitWidth(100);
                    imageView.setPreserveRatio(true);
                    break;
                case "Cyndaquil":
                    image = new Image(new FileInputStream("/Users/brian/IdeaProjects/fa-23-final-project-Martin8156/src/main/resources/Cyndaquil.png"));
                    imageView = new ImageView(image);
                    imageView.setX(50);
                    imageView.setY(25);
                    imageView.setFitHeight(115);
                    imageView.setFitWidth(115);
                    imageView.setPreserveRatio(true);
                    break;
                default:
            }

            bidButton.setOnAction(event -> handleViewButtonForClosedBid(pokemon));
            anchorPane.getChildren().addAll(nameLabel, circle, bidButton, imageView);
            anchorPane.setMaxSize(585, 383);


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


            AnchorPane.setTopAnchor(circle, 3.0);
            AnchorPane.setLeftAnchor(circle, 5.0);

            AnchorPane.setTopAnchor(nameLabel, 15.0);
            AnchorPane.setLeftAnchor(nameLabel, 135.0);

            AnchorPane.setTopAnchor(bidButton, 55.0);
            AnchorPane.setLeftAnchor(bidButton, 135.0);



        }
        catch(Exception e){
            e.printStackTrace();
        }
        return anchorPane;


    }
    private void handleViewButtonForClosedBid(AuctionItem pokemon) {
        // UI elements for new bid window
        Label nameLabel = new Label("Pokemon Name: " + pokemon.getName());
        Label description = new Label("Description: " + pokemon.getDescription());
        Button backButton = new Button("Back");

        // Create a ListView to display bid history
        ListView<String> bidHistoryListView = new ListView<>();
        //String[] test = {"Martin has placed a bid on Gengar for 500", "Anika has placed a bid on Gengar for 600"};
        //bidHistoryListView.getItems().addAll(test);
        String[] history = pokemon.getBidHistoryStringArray();
        System.out.println(history);
        bidHistoryListView.getItems().addAll(history);

        Label soldPrice;
        if(bidHistoryListView.getItems().isEmpty()) {
            soldPrice = new Label(pokemon.getName() + " was not sold");
        }
        else {
             soldPrice = new Label(pokemon.getName() + " was sold for " + pokemon.getMinBid());
        }


        VBox vbox;
        vbox = new VBox(nameLabel, description, soldPrice, bidHistoryListView, backButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);

        backButton.setOnAction(event -> mainView.setScene(personalScene)); // return to biddingScene

        Scene bidScene = new Scene(vbox, 600, 400);
        mainView.setScene(bidScene);  // Set the scene for the bidding
    }


    private void handleBidButton(AuctionItem pokemon) {
        // UI elements for new bid window
        Label nameLabel = new Label("Pokemon Name: " + pokemon.getName());
        Label bidLabel = new Label("Current Bid: " + pokemon.getMinBid());
        Label maxBid = new Label("Buy Now Price: " + pokemon.getBuyNowPrice() + " NOTE: Bids equal to or above buy now price will be accepted and will immediately close the bid");
        Label description = new Label("Description: " + pokemon.getDescription());
        TextField bidTextField = new TextField();
        Button placeBidButton = new Button("Place Bid");
        Button backButton = new Button("Back");

        // Create a ListView to display bid history
        ListView<String> bidHistoryListView = new ListView<>();
        //String[] test = {"Martin has placed a bid on Gengar for 500", "Anika has placed a bid on Gengar for 600"};
        //bidHistoryListView.getItems().addAll(test);
        String[] history = pokemon.getBidHistoryStringArray();
        System.out.println(history);
        bidHistoryListView.getItems().addAll(history);

        VBox vbox;
        if (guest) {
            vbox = new VBox(nameLabel, description, maxBid, bidLabel, bidHistoryListView, backButton);
        } else {
            vbox = new VBox(nameLabel, description, maxBid, bidLabel, bidTextField, placeBidButton, bidHistoryListView, backButton);
        }
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);

        placeBidButton.setOnAction(event -> {
            //bid placement logic here
            try {
                String bidAmountText = bidTextField.getText();
                double bidAmount = Double.parseDouble(bidAmountText);

                if (bidAmount > pokemon.getMinBid()) { // check if the bid is greater than the current bid
                    // if so send bid to server amd add the bid to the history of the pokemon
                    BidInfo bidInfo = new BidInfo(client.getUsername(), pokemon.getName(), bidAmount);
                    client.addToBidQueue(bidInfo);
                    pokemon.addToBidHistory(bidInfo);
                    mainView.setScene(personalScene);

                } else {
                    showAlert(Alert.AlertType.ERROR, "Invalid Bid", "Bid amount must be greater than the current bid."); // invalid input
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid numeric bid amount."); // no input selected
            }
        });

        backButton.setOnAction(event -> mainView.setScene(personalScene)); // return to biddingScene

        Scene bidScene = new Scene(vbox, 600, 400);
        mainView.setScene(bidScene);  // Set the scene for the bidding window
    }



    private void showAlert(Alert.AlertType alertType, String title, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.showAndWait();
    }


    public void setGuest(boolean guest) {
        this.guest = guest;
    }

    public void setPersonalScene(Scene biddingScene) {
        personalScene = biddingScene;
    }
    public void setLoginScreenController(LoginScreenController loginScreenController) {
        this.loginScreenController = loginScreenController;
    }


//    private void startCountdown(int timeInSeconds, Label timerLabel) {
//        LocalTime end = LocalTime.now().plusSeconds(timeInSeconds);
//        AnimationTimer timer = new AnimationTimer() {
//            @Override
//            public void handle(long l) {
//                Duration remaining = Duration.between(LocalTime.now(), end);
//                if (remaining.getSeconds() > 0) {
//                    timerLabel.setText(format(remaining));
//                } else {
//                    timerLabel.setText("0 minutes left");
//                    stop();
//                }
//            }
//
//            private String format(Duration remaining) {
//                // Display only minutes
//                long minutes = remaining.toMinutes();
//                return String.format("%d minutes left", minutes);
//            }
//        };
//
//        timer.start();
//    }
}
