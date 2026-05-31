package fr.framelab.Controller;

import fr.framelab.Model.Project;
import javafx.fxml.FXML;
import javafx.scene.image.Image;

import java.io.File;

public class sendController {
    int id_challenge;
    String pathImg;


    @FXML
    public void initialize() {

    }


    public void setupPart(Project currentProject) {
        try {
            this.id_challenge = currentProject.getId_challenge();
            this.pathImg = currentProject.getPicture();


        } catch (Exception e) {

        }

    }
}
