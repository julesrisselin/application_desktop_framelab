package fr.framelab.Controller;

import fr.framelab.DAO.DatabaseManager;
import fr.framelab.DAO.ProjetDAO;
import fr.framelab.DTO.ChallengeDTO;
import fr.framelab.DTO.ChallengeDataDTO;
import fr.framelab.Enum.Screen;
import fr.framelab.Main;
import fr.framelab.Model.Project;
import fr.framelab.Service.ChallengeDemoManager;
import fr.framelab.Service.CurrentChallenge;
import fr.framelab.Service.RecupUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

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
    private TableView<Project> listProjects;
    @FXML
    private TableColumn<Project, String> NameProjets;
    @FXML
    private TableColumn<Project, String> LastEditProjets;
    private ObservableList<Project> projects;


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
            try {
                ChallengeDataDTO currentChallengeData = currentChallenge.getData();

                if (currentChallengeData == null) {
                    currentChallengeData = ChallengeDemoManager.getDemoChallenge().getData();
                } else {
                    currentChallengeData = currentChallenge.getData();
                }

                ChallengeDataDTO finalChall = currentChallengeData;

                String path = "challenge/" + "Challenge#" + finalChall.getId() + ".png";
                File file = new File(path);
                Image img;
                if (!file.isFile()) {
                    img = new Image(finalChall.getFullPicture());
                    ImageTools.saveImg(img, path);
                } else {
                    img = new Image(file.toURI().toString());
                }
                challengeImage.setImage(img);
                titleChallenge.setText(finalChall.getTitle_theme());
                descriptionChallenge.setText(finalChall.getDescription_theme());
                dateStartChallenge.setText(finalChall.getDate_start());
                dateEndChallenge.setText(finalChall.getDate_end());

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
                        Project newProject = new Project(this.nameProject, path, LocalDate.now().toString(), LocalDate.now().toString(), finalChall.getId());
                        ProjetDAO firstSave = new ProjetDAO(DatabaseManager.getConnection());
                        firstSave.createProjet(newProject);
                        EditorController controller = (EditorController) Main.navigateTo(Screen.EDITOR);
                        controller.setupProjet(newProject, true);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                });

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        task.setOnFailed(event -> {
            ChallengeDataDTO finalChall = ChallengeDemoManager.getDemoChallenge().getData();

            String path = "challenge/" + "Challenge#" + finalChall.getId() + ".png";
            File file = new File(path);

            File defaultFile = new File("challenge/pardefaut.png");

            Image img;

            if (!file.isFile()) {
                img = new Image(defaultFile.toURI().toString());
                try {
                    ImageTools.saveImg(img, path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                img = new Image(file.toURI().toString());
            }
            challengeImage.setImage(img);
            challengeImage.setImage(img);
            titleChallenge.setText(finalChall.getTitle_theme());
            descriptionChallenge.setText(finalChall.getDescription_theme());
            dateStartChallenge.setText(finalChall.getDate_start());
            dateEndChallenge.setText(finalChall.getDate_end());

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
                    Project newProject = new Project(this.nameProject, path, LocalDate.now().toString(), LocalDate.now().toString(), finalChall.getId());
                    ProjetDAO firstSave = new ProjetDAO(DatabaseManager.getConnection());
                    firstSave.createProjet(newProject);
                    EditorController controller = (EditorController) Main.navigateTo(Screen.EDITOR);
                    controller.setupProjet(newProject, true);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
        });


        new Thread(task).start();

    }

    private void loadProjects() {
        try {
            ProjetDAO recupProjects = new ProjetDAO(DatabaseManager.getConnection());
            ArrayList<Project> data = recupProjects.readAllProjects();
            projects = FXCollections.observableArrayList(data);
            this.listProjects.setItems(projects);

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Erreur lors du chargement des projets : " + e.getMessage());
            alert.show();
        }
    }

    @FXML
    private void openProject() throws Exception {
        Project selected = listProjects.getSelectionModel().getSelectedItem();
        EditorController controller = (EditorController) Main.navigateTo(Screen.EDITOR);
        controller.setupProjet(selected, false);
    }

    @FXML
    private void suppProject() throws Exception {
        Project selected = listProjects.getSelectionModel().getSelectedItem();
        ProjetDAO suppProject = new ProjetDAO(DatabaseManager.getConnection());
        suppProject.deleteProjet(selected.getId());
        projects.remove(selected);
    }

    @FXML
    private void logOut() throws IOException {
        RecupUser newCookies = new RecupUser();
        Main.navigateTo(Screen.LOGIN);
    }

}

