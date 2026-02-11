package fr.framelab.Controller;

import fr.framelab.DTO.TokenDTO;
import fr.framelab.Enum.Screen;
import fr.framelab.Main;
import fr.framelab.Model.User;
import fr.framelab.Service.RecupUser;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class ConnexionController {
    @FXML private TextField emailField;
    @FXML private TextField passwordField;
    @FXML private Button loginButton;
    @FXML private Label feedbackLogin;

    private final RecupUser recupuser = new RecupUser();

    @FXML public void initialize() {
        loginButton.setOnAction(e -> {
            try {
                connexion();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @FXML private void connexion() throws Exception {
        String email = emailField.getText();
        String password = passwordField.getText();

        feedbackLogin.setText("Connexion en cours");

        Task<TokenDTO> task = new Task<>(){
            @Override
            protected TokenDTO call() throws Exception {
                return recupuser.getUser(email, password);
            }
        };

        task.setOnSucceeded(event -> {
            TokenDTO token = task.getValue();
            if(token != null){
                try {
                    Main.navigateTo(Screen.PROJECTS);
                } catch (Exception e){

                }
            } else {
                feedbackLogin.setText("Identifiants invalides");
            }
        });

        task.setOnFailed(event -> {
            feedbackLogin.setText("Erreur : impossible de contacter le serveur");
        });

        new Thread(task).start();
    }

}
