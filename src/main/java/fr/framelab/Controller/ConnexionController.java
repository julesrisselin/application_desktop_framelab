package fr.framelab.Controller;

import fr.framelab.Enum.Screen;
import fr.framelab.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


public class ConnexionController {
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button loginButton;

    @FXML
    public void initialize() {
        loginButton.setOnAction(e -> connexion());
    }
    @FXML
    private void connexion() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (!email.isEmpty() && !password.isEmpty()) {
            try {
                Main.navigateTo(Screen.PROJECTS);
            } catch (Exception e){

            }
        }
    }

}
