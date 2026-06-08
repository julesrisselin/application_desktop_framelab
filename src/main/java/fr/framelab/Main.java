package fr.framelab;

import fr.framelab.Enum.Screen;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {

        primaryStage = stage;

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/View/connexion.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        stage.setTitle("Framelab");
        stage.setScene(scene);
        stage.show();
    }

    public static Object navigateTo(Screen screen) throws IOException {
        String path = "/View/connexion.fxml";
        String title = "Connexion";
        switch (screen) {
            case Screen.LOGIN:
                path = "/View/connexion.fxml";
                title = "Connexion";
                break;
            case Screen.PROJECTS:
                path = "/View/projects.fxml";
                title = "Mes projets";
                break;
            case Screen.EDITOR:
                path = "/View/editor.fxml";
                title = "Édition";
                break;
            case Screen.SEND:
                path = "/View/submission.fxml";
                title = "Envoi";
                break;
        }
        FXMLLoader projet = new FXMLLoader(Main.class.getResource(path));
        Parent root1 = projet.load();

        Scene scene1 = new Scene(root1);
        primaryStage.setScene(scene1);
        primaryStage.setTitle(title);
        primaryStage.show();

    return projet.getController();
    }


    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }


}