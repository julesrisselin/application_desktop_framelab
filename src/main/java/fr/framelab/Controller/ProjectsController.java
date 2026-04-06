package fr.framelab.Controller;

import fr.framelab.DAO.DatabaseManager;
import fr.framelab.DAO.ProjetDAO;
import fr.framelab.DTO.ChallengeDTO;
import fr.framelab.DTO.ChallengeDataDTO;
import fr.framelab.Enum.Screen;
import fr.framelab.Main;
import fr.framelab.Model.Projet;
import fr.framelab.Service.CurrentChallenge;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.ErrorManager;

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
    @FXML
    private TableView<Projet> listProjects;
    @FXML
    private TableColumn<Projet, String> NameProjets;
    @FXML
    private TableColumn<Projet, String> LastEditProjets;
    private ObservableList<Projet> projets;


    public String nameProject;

    private final CurrentChallenge currentChallenge = new CurrentChallenge();

    @FXML
    public void initialize() {
        try {
            showChallenge();
            NameProjets.setCellValueFactory(new PropertyValueFactory<>("name"));
            LastEditProjets.setCellValueFactory(new PropertyValueFactory<>("date_last_edit"));
            loadProjects();
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
            if (currentChallenge == null) {
                try {
                    String path = "challenge/pardefaut.png";
                    File file = new File(path);
                    Image img;
                    if (!file.isFile()) {
                        img = new Image("pardefaut.png");
                        ImageTools.saveImg(img, path);
                    } else {
                        img = new Image(file.toURI().toString());
                    }
                    challengeImage.setImage(img);
                    titleChallenge.setText("Test Démo");
                    descriptionChallenge.setText("Ceci est un test pour la version démo");
                    dateStartChallenge.setText("01/01/1900");
                    dateEndChallenge.setText("01/01/2070");

                    NewProjetButton.setOnAction(e -> {
                        try {
                            TextInputDialog dialog = new TextInputDialog("Valeur par défaut");
                            dialog.setTitle("Rotation");
                            dialog.setHeaderText("Entrer un nom de projet");
                            dialog.setContentText("Nom :");

                            Optional<String> result = dialog.showAndWait();


                            if (result.isPresent()) {
                                this.nameProject = result.get();
                            }
                            Projet newProjet = new Projet(this.nameProject, path, LocalDate.now().toString(), LocalDate.now().toString(), 1);
                            ProjetDAO firstSave = new ProjetDAO(DatabaseManager.getConnection());
                            firstSave.createProjet(newProjet);
                            EditorController controller = (EditorController) Main.navigateTo(Screen.EDITOR);
                            controller.setupProjet(newProjet, true);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    });

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            } else {
                try {
                    ChallengeDataDTO currentChallengeData = currentChallenge.getData();
                    String path = "challenge/" + "Challenge#" + currentChallengeData.getId() + ".png";
                    File file = new File(path);
                    Image img;
                    if (!file.isFile()) {
                        img = new Image(currentChallengeData.getFullPicture());
                        ImageTools.saveImg(img, path);
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
                            TextInputDialog dialog = new TextInputDialog("Valeur par défaut");
                            dialog.setTitle("Rotation");
                            dialog.setHeaderText("Entrer un nom de projet");
                            dialog.setContentText("Nom :");

                            Optional<String> result = dialog.showAndWait();


                            if (result.isPresent()) {
                                this.nameProject = result.get();
                            }
                            Projet newProjet = new Projet(this.nameProject, path, LocalDate.now().toString(), LocalDate.now().toString(), currentChallengeData.getId());
                            ProjetDAO firstSave = new ProjetDAO(DatabaseManager.getConnection());
                            firstSave.createProjet(newProjet);
                            EditorController controller = (EditorController) Main.navigateTo(Screen.EDITOR);
                            controller.setupProjet(newProjet, true);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    });

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        task.setOnFailed(event -> {
        });


        new Thread(task).start();

    }

    private void loadProjects() {
        try {
            ProjetDAO recupProjets = new ProjetDAO(DatabaseManager.getConnection());
            ArrayList<Projet> data = recupProjets.readAllProjects();
            projets = FXCollections.observableArrayList(data);
            this.listProjects.setItems(projets);

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Erreur lors du chargement des projets : " + e.getMessage());
            alert.show();
        }
    }

    @FXML
    private void openProject() throws Exception {
        Projet selected = listProjects.getSelectionModel().getSelectedItem();
        EditorController controller = (EditorController) Main.navigateTo(Screen.EDITOR);
        controller.setupProjet(selected, false);
    }

    @FXML
    private void suppProject() throws Exception {
        Projet selected = listProjects.getSelectionModel().getSelectedItem();
        ProjetDAO suppProject = new ProjetDAO(DatabaseManager.getConnection());
        suppProject.deleteProjet(selected.getId());
        projets.remove(selected);
    }

}

