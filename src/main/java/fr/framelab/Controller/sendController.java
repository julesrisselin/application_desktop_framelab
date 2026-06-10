package fr.framelab.Controller;

import fr.framelab.DTO.PartDTO;
import fr.framelab.DTO.TokenDTO;
import fr.framelab.Enum.Screen;
import fr.framelab.Main;
import fr.framelab.Model.Project;
import fr.framelab.Service.sendPart;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;

public class sendController {
    private final sendPart sendPart = new sendPart();
    @FXML
    private Label feedbackPart;
    private int id_challenge;
    private String pathImg;


    @FXML
    public void initialize() {

    }


    public void sendPart(Project currentProject) throws Exception {

        this.id_challenge = currentProject.getId_challenge();
        this.pathImg = "projets/" + "Projet" + currentProject.getId() + ".png";

        Task<PartDTO> task = new Task<>() {
            @Override
            protected PartDTO call() throws Exception {
                return sendPart.subPart(id_challenge, pathImg);
            }
        };

        task.setOnSucceeded(event -> {
            feedbackPart.setText("Participation bien envoyé");

        });

        task.setOnFailed(event -> {
            feedbackPart.setText("Erreur : impossible de contacter le serveur");
            Throwable ex = task.getException();
            System.out.println("ERREUR sendPart task : " + ex.getMessage());
            ex.printStackTrace();
            feedbackPart.setText("Erreur : " + ex.getMessage());
        });

        new Thread(task).start();
    }
}

