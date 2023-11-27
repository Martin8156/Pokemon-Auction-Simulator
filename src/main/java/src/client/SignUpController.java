package src.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import src.sharedClass.LoginInfo;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    private Scene personalScene;
    private Stage mainView;
    private LoginScreenController loginScreenController;
    private Client client;

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button signUpButton;
    @FXML
    private Button cancelButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        signUpButton.setOnAction(event -> handleSignUpButton(event));
        cancelButton.setOnAction(event -> handleCancelButton(event));
    }

    private void handleCancelButton(ActionEvent event) {
        loginScreenController.returnToThisScene();
    }

    private void handleSignUpButton(ActionEvent event) {
        if (usernameField.getText().isEmpty() == false && passwordField.getText().isEmpty() == false) {
            // need to send request to server
            LoginInfo userAttempt = new LoginInfo(usernameField.getText(), passwordField.getText());
            client.addToSignUpQueue(userAttempt); // Add the bid info to the shared queue
            // Setting up main UI now
            try {
                // wait for server resposne
                boolean signUpResponse = client.getSignUpResponseQueue().take();
                if (signUpResponse) {
                    showAlert("Sign Up Successful", "Your account has been created successfully.");
                    loginScreenController.returnToThisScene();

                } else {
                    showAlert("Sign Up Failed", "Username already exists and/or Invalid Input");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    /**
     * TODO: Setters
     *
     */
    public void setPersonalScene(Scene signInScene) {
        personalScene = signInScene;
    }

    public void setStage(Stage mainView) {
        mainView = mainView;
    }

    public void setClient(Client client) {
        this.client = client;
    }
    public void setPreviousController(LoginScreenController loginScreenController) {
        this.loginScreenController = loginScreenController;
    }
}
