package fr.framelab.Controller;

import fr.framelab.DAO.DatabaseManager;
import fr.framelab.DAO.ProjetDAO;
import fr.framelab.Enum.Screen;
import fr.framelab.Main;
import fr.framelab.Model.Project;
import fr.framelab.Service.RecupUser;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class EditorController {
    @FXML
    private StackPane challengeContainer;
    private Image image;
    private int rotation;
    private double lum;
    private double sat;
    private int id_challenge;
    private Project currentProject;
    private Canvas canvas;
    private WritableImage layerImage;
    private Boolean newProject;
    @FXML
    private Slider sliderBrightness;
    @FXML
    private Slider sliderSaturation;

    @FXML
    public void initialize() {
        try {
            this.rotation = 0;
            this.lum = 0.0;
            this.sat = 0.0;

            this.sliderBrightness.setMax(1);
            this.sliderBrightness.setMin(-1);
            this.sliderBrightness.setBlockIncrement(0.01);
            sliderBrightness.valueProperty().addListener((ObservableValue<? extends Number> num, Number oldVal, Number newVal) -> {
                this.lum = (double) newVal;
                display();
            });

            this.sliderSaturation.setMin(0.0);
            this.sliderSaturation.setMax(2.0);
            this.sliderSaturation.setValue(1.0);
            this.sliderSaturation.setBlockIncrement(0.01);
            sliderSaturation.valueProperty().addListener((ObservableValue<? extends Number> num, Number oldVal, Number newVal) -> {
                this.sat = (double) newVal;
                display();
            });


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

    public void setupProjet(Project currentProject, Boolean newProject) {
        try {
            this.newProject = newProject;
            this.currentProject = currentProject;
            this.id_challenge = currentProject.getId_challenge();
            this.rotation = currentProject.getRotate();
            String path;
            if (newProject == true) {
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
        this.canvas = new Canvas(this.image.getWidth(), this.image.getHeight());
        double x = 900/this.image.getWidth();
        double y = 900/this.image.getHeight();
        double z = Math.min(x,y);
        this.challengeContainer.setScaleX(z);
        this.challengeContainer.setScaleY(z);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        challengeContainer.getChildren().add(canvas);
        String path;
        if (newProject == true) {
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
        int h = (int) this.layerImage.getHeight();
        int w = (int) this.layerImage.getWidth();
        WritableImage dest = new WritableImage(w, h);

        PixelReader reader = this.layerImage.getPixelReader();
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
        int h = (int) this.layerImage.getHeight();
        int w = (int) this.layerImage.getWidth();
        WritableImage dest = new WritableImage(w, h);

        PixelReader reader = this.layerImage.getPixelReader();
        PixelWriter writer = dest.getPixelWriter();
        for (int y = 0; y < h; y++) {
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
        int h = (int) canvas.getHeight();
        int w = (int) canvas.getWidth();

        int centreX = w / 2;
        int centreY = h / 2;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.TRANSPARENT);
        gc.fillRect(0, 0, w, h);
        gc.save();
        gc.translate(centreX, centreY);
        gc.rotate(this.rotation);
        gc.translate(-centreX, -centreY);
        this.drawCanvasImage(this.layerImage);
        gc.restore();
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
        Main.navigateTo(Screen.PROJECTS);
    }

    public void brightnessFilter(WritableImage drawImage) {
            int h = (int) drawImage.getHeight();
            int w = (int) drawImage.getWidth();

            PixelReader reader = drawImage.getPixelReader();
            PixelWriter writer = drawImage.getPixelWriter();
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    Color sourceColor = reader.getColor(x, y);
                    double red   = Math.clamp(sourceColor.getRed()   + this.lum, 0.0, 1.0);
                    double green = Math.clamp(sourceColor.getGreen() + this.lum, 0.0, 1.0);
                    double blue  = Math.clamp(sourceColor.getBlue()  + this.lum, 0.0, 1.0);
                    Color color = Color.color(red, green, blue);
                    writer.setColor(x, y, color);
                }
            }
    }

    public void saturationFilter(WritableImage drawImage) {
            int h = (int) drawImage.getHeight();
            int w = (int) drawImage.getWidth();

            PixelReader reader = drawImage.getPixelReader();
            PixelWriter writer = drawImage.getPixelWriter();
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    Color sourceColor = reader.getColor(x, y);
                    double grey = 0.299 * sourceColor.getRed() + 0.587 * sourceColor.getGreen() + 0.114 * sourceColor.getBlue();
                    double red   = Math.clamp(grey + this.sat *(sourceColor.getRed() - grey), 0 ,1);
                    double green   = Math.clamp(grey + this.sat *(sourceColor.getGreen() - grey), 0 ,1);
                    double blue   = Math.clamp(grey + this.sat *(sourceColor.getBlue() - grey), 0 ,1);
                    Color color = Color.color(red, green, blue);
                    writer.setColor(x, y, color);
                }
            }
    }

    public void display(){
        WritableImage drawImage = ImageTools.copyImg(this.layerImage);
        brightnessFilter(drawImage);
        saturationFilter(drawImage);
        this.drawCanvasImage(drawImage);
    }

    public void applyLum(){
        brightnessFilter(this.layerImage);
        this.sliderBrightness.setValue(0);
        this.drawCanvasImage(layerImage);
    }

    public void applySat(){
        saturationFilter(this.layerImage);
        this.sliderSaturation.setValue(1);
        this.drawCanvasImage(layerImage);
    }

    public void sendPart() throws Exception {
        try {
            String path = "projets/" + "Projet#" + currentProject.getId() + ".png";
            File file = new File(path);
            ImageTools.saveImg(this.layerImage, path);
            ProjetDAO saveProject = new ProjetDAO(DatabaseManager.getConnection());
            saveProject.updateProjet(currentProject);
            sendController controller = (sendController) Main.navigateTo(Screen.SEND);
            controller.sendPart(currentProject);
        } catch (Exception e) {
            System.out.println("ERREUR EditorController.sendPart : " + e.getMessage());
            e.printStackTrace();
        }

    }

    @FXML
    private void logOut() throws IOException {
        RecupUser newCookies = new RecupUser();
        Main.navigateTo(Screen.LOGIN);
    }

}
