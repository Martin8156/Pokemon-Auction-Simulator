package src.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import src.sharedClass.LoginInfo;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginScreenController implements Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label promptIfFail;
    @FXML
    private Text signUp;
    @FXML
    private Button guestButton;

    private Scene personalScene;
    private Stage mainView;
    private Client client;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginButton.setOnAction(event -> handleLoginButton(event));
        cancelButton.setOnAction(event -> handleCancelButton(event));
        guestButton.setOnAction(event -> handleGuestButton(event));
        signUp.setOnMouseClicked(event -> handleSignUp(event));
    }

    private void handleSignUp(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SignUpPage.fxml"));
            BorderPane rootSignUp = loader.load();
            Scene signInScene = new Scene(rootSignUp);
            SignUpController signupController = loader.getController();

            signupController.setStage(mainView);
            signupController.setPersonalScene(signInScene);
            signupController.setPreviousController(this);
            signupController.setClient(client);


            mainView.setScene(signInScene);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void handleGuestButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AuctionUI.fxml"));
            AnchorPane biddingGUI = loader.load();

            Scene biddingScene = new Scene(biddingGUI);
            mainView.setTitle("Pokemon Bidding");
            mainView.setScene(biddingScene);
            mainView.show();


            UIController UIcontroller = loader.getController();
            client.setController(UIcontroller);// Get the controller instance
            UIcontroller.setLoginScreenController(this); // pass a reference of old conrtoller to new controllere
            UIcontroller.setClient(client); // Pass a reference of the client to the controller
            UIcontroller.setStage(mainView);
            UIcontroller.setPersonalScene(biddingScene);
            UIcontroller.setGuest(true);

            client.sendToServer("2");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void handleLoginButton(ActionEvent event) {
        if (usernameField.getText().isEmpty() == false && passwordField.getText().isEmpty() == false) {
            //now check if in database before setting next pane
            // need to send request to server
            LoginInfo userAttempt = new LoginInfo(usernameField.getText(), passwordField.getText());
            client.addToLoginQueue(userAttempt); // Add the bid info to the shared queue
            // Setting up main UI now
            try {
                // wait for server resposne
                boolean loginResponse = client.getLoginResponseQueue().take();
                if (loginResponse) {
                    // Login successful
                    client.setUsername(userAttempt.getUsername());
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/AuctionUI.fxml"));
                    AnchorPane biddingGUI = loader.load();
                    Scene biddingScene = new Scene(biddingGUI);
                    mainView.setTitle("Pokemon Bidding");
                    mainView.setScene(biddingScene);
                    mainView.show();

                    UIController UIcontroller = loader.getController();
                    client.setController(UIcontroller);// Get the controller instance from the loader
                    UIcontroller.setLoginScreenController(this); // pass a reference of old conrtoller to new controllere
                    UIcontroller.setClient(client); // Pass a reference of the client to the controller
                    UIcontroller.setStage(mainView);
                    UIcontroller.setPersonalScene(biddingScene);
                    client.sendToServer("2");
                } else {
                    // Login failed
                    // Update UI to show login failure message
                    promptIfFail.setText("Login Failed");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

//            //if not
//            promptIfFail.setText("Login Failed");
        } else {
            promptIfFail.setText("Please type in your username and password");
        }
    }

    private void handleCancelButton(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }


    /**
     * TODO: Setters
     */
    public void setClient(Client client) {
        this.client = client;
    }

    public void setmainView(Stage mainView) {
        this.mainView = mainView;
    }

    public void setPersonalScene(Scene loginScene) {
        personalScene = loginScene;
    }

    /**
     * TODO: use the reference to the controller and call this method to return to this scene
     */
    public void returnToThisScene() {
        mainView.setScene(personalScene);
    }

    public void clearUsernameField() {
        usernameField.clear();
    }

    public void clearPasswordField() {
        passwordField.clear();
    }
}
