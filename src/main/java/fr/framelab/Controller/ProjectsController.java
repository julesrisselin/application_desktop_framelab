package fr.framelab.Controller;

import fr.framelab.DTO.ChallengeDTO;
import fr.framelab.DTO.ChallengeDataDTO;
import fr.framelab.Enum.Screen;
import fr.framelab.Main;
import fr.framelab.Service.CurrentChallenge;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class ProjectsController {
    @FXML
    private Button NewProjetButton;
    @FXML
    private ImageView challengeImage;
    @FXML
    private Label titleChallenge;
    @FXML
    private Label descriptionChallenge;
    @FXML
    private Label dateStartChallenge;
    @FXML
    private Label dateEndChallenge;

    private final CurrentChallenge currentChallenge = new CurrentChallenge();

    @FXML
    public void initialize() {
        try {
            showChallenge();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @FXML
    private void showChallenge() throws Exception {

        Task<ChallengeDTO> task = new Task<>() {
            @Override
            protected ChallengeDTO call() throws Exception {
                return currentChallenge.getChallenge();
            }
        };

        task.setOnSucceeded(event -> {
            ChallengeDTO currentChallenge = task.getValue();
            if (currentChallenge != null) {
                try {
                    ChallengeDataDTO currentChallengeData = currentChallenge.getData();
                    String path = "challenge/" + "Challenge#" + currentChallengeData.getId() + ".png";
                    File file = new File(path);
                    Image img;
                    if(!file.isFile()){
                        img = new Image(currentChallengeData.getPicture());
                        ImageTools.saveImg(img,path);
                    } else {
                        img = new Image(file.toURI().toString());
                    }
                    challengeImage.setImage(img);
                    titleChallenge.setText(currentChallengeData.getTitle_theme());
                    descriptionChallenge.setText(currentChallengeData.getDescription_theme());
                    dateStartChallenge.setText(currentChallengeData.getDate_start());
                    dateEndChallenge.setText(currentChallengeData.getDate_end());

                    NewProjetButton.setOnAction(e -> {
                        try {
                            EditorController controller = (EditorController) Main.navigateTo(Screen.EDITOR);
                            controller.setupChallenge(currentChallengeData.getId());

                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    });

                } catch (Exception e) {

                }

            }

        });
        task.setOnFailed(event -> {
        });

        new Thread(task).start();

    }

    ;

}

