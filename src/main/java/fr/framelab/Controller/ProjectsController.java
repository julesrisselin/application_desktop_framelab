package fr.framelab.Controller;

import fr.framelab.DTO.ChallengeDTO;
import fr.framelab.DTO.ChallengeDataDTO;
import fr.framelab.DTO.TokenDTO;
import fr.framelab.Enum.Screen;
import fr.framelab.Main;
import fr.framelab.Service.CurrentChallenge;
import fr.framelab.Service.RecupUser;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ProjectsController {
    @FXML private Button downloadButton;
    @FXML private ImageView challengeImage;
    @FXML private Label titleChallenge;
        @FXML private Label descriptionChallenge;
        @FXML private Label dateStartChallenge;
        @FXML private Label dateEndChallenge;

        private final CurrentChallenge currentChallenge = new CurrentChallenge();

        @FXML public void initialize() {
            try {
                showChallenge();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        @FXML private void showChallenge() throws Exception {

            Task<ChallengeDTO> task = new Task<>(){
                @Override
                protected ChallengeDTO call() throws Exception {
                    return currentChallenge.getChallenge();
                }
            };

            task.setOnSucceeded(event -> {
                ChallengeDTO currentChallenge = task.getValue();
                if(currentChallenge != null){
                    try {
                        ChallengeDataDTO currentChallengeData = currentChallenge.getData();
                        Image img = new Image(currentChallengeData.getPicture(),200,200,true, true);
                        challengeImage.setImage(img);
                        titleChallenge.setText(currentChallengeData.getTitle_theme());
                        descriptionChallenge.setText(currentChallengeData.getDescription_theme());
                        dateStartChallenge.setText(currentChallengeData.getDate_start());
                        dateEndChallenge.setText(currentChallengeData.getDate_end());

                    } catch (Exception e){

                    }
                }
            });

            new Thread(task).start();
        }

    }

