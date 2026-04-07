package fr.framelab.Controller;

import fr.framelab.DAO.DatabaseManager;
import fr.framelab.DAO.ProjetDAO;
import fr.framelab.Enum.Screen;
import fr.framelab.Main;
import fr.framelab.Model.Project;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class EditorController {
    @FXML
    private VBox challengeContainer;
    @FXML
    private ImageView challengeImage;
    private Image image;
    private int rotation;
    private int id_challenge;
    private Project currentProject;
    private Canvas canvas;
    private WritableImage layerImage;
    private Boolean newProject;

    @FXML
    public void initialize() {
        try {
            this.rotation = 0;

        } catch (Exception e) {

        }
    }

    @FXML
    private void accueil() {
        try {
            Main.navigateTo(Screen.PROJECTS);
        } catch (Exception e) {

        }
    }

    public void setupProjet(Project currentProject, Boolean newProject){
        try {
            this.newProject = newProject;
            this.currentProject = currentProject;
            this.id_challenge = currentProject.getId_challenge();
            this.rotation = currentProject.getRotate();
            String path;
            if(newProject == true) {
                path = "challenge/Challenge#" + this.id_challenge + ".png";
            } else {
                path = "projets/Projet#" + this.currentProject.getId() + ".png";
            }
            File file = new File(path);
            this.image = new Image(file.toURI().toString());
            createCanvas();
        } catch (Exception e) {

        }

    }

    public void createCanvas() {
        this.canvas = new Canvas(900, 900);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        challengeContainer.getChildren().add(canvas);



        String path;
        if (newProject == true){
            path = "challenge/Challenge#" + this.id_challenge + ".png";
        } else {
            path = "projets/Projet#" + currentProject.getId() + ".png";
        }
        File file = new File(path);
        Image img = new Image(file.toURI().toString());
        this.layerImage = ImageTools.convertImage(img);
        gc.drawImage(this.layerImage, 0, 0);

    }

    public void BandWFilter() {
        WritableImage source = this.canvas.snapshot(null, null);
        int h = (int) source.getHeight();
        int w = (int) source.getWidth();
        WritableImage dest = new WritableImage(w, h);

        PixelReader reader = source.getPixelReader();
        PixelWriter writer = dest.getPixelWriter();
        for (int y = 0; y < h; y++) { // Parcourir tous les pixels
            for (int x = 0; x < w; x++) {
                Color sourceColor = reader.getColor(x, y);
                double lum = 0.299 * sourceColor.getRed() + 0.587 * sourceColor.getGreen() + 0.114 * sourceColor.getBlue();
                Color gray = Color.color(lum, lum, lum);

                writer.setColor(x, y, gray);
            }
        }
        this.drawCanvasImage(dest);
        this.layerImage = dest;
    }

    public void NegativeFilter() {
        WritableImage source = this.canvas.snapshot(null, null);
        int h = (int) source.getHeight();
        int w = (int) source.getWidth();
        WritableImage dest = new WritableImage(w, h);

        PixelReader reader = source.getPixelReader();
        PixelWriter writer = dest.getPixelWriter();
        for (int y = 0; y < h; y++) { // Parcourir tous les pixels
            for (int x = 0; x < w; x++) {
                Color sourceColor = reader.getColor(x, y);
                double red = 1 - sourceColor.getRed();
                double green = 1 - sourceColor.getGreen();
                double blue = 1 - sourceColor.getBlue();
                Color color = Color.color(red, green, blue);

                writer.setColor(x, y, color);
            }
        }
        this.drawCanvasImage(dest);
        this.layerImage = dest;
    }

    public void rotateImage() {
        this.rotation = (this.rotation + 90) % 360;
        WritableImage source = ImageTools.copyImg(this.image);
        int h = (int) canvas.getHeight();
        int w = (int) canvas.getWidth();

        int centreX = w / 2;
        int centreY = h / 2;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, w, h);
        gc.save();
        gc.translate(centreX, centreY);
        gc.rotate(this.rotation);
        gc.translate(-centreX, -centreY);
        this.drawCanvasImage(source);
        gc.restore();
        this.layerImage = source;
    }


    public void drawCanvasImage(Image img) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(img, 0, 0);
    }

    public void saveProject() throws SQLException, IOException {
        String path = "projets/" + "Projet#" + currentProject.getId() + ".png";
        File file = new File(path);
        ImageTools.saveImg(this.layerImage, path);
        ProjetDAO saveProject = new ProjetDAO(DatabaseManager.getConnection());
        saveProject.updateProjet(currentProject);
    }


}
