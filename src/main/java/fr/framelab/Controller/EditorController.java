package fr.framelab.Controller;

import fr.framelab.Enum.Screen;
import fr.framelab.Main;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.File;

public class EditorController {
    @FXML
    private VBox challengeContainer;
    @FXML
    private ImageView challengeImage;
    private int challengeId;
    private Canvas canvas;

    @FXML
    public void initialize() {
        try {

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

    public void setupChallenge(int challengeId) throws Exception {
        try {
            this.challengeId = challengeId;
            String path = "challenge/Challenge#" + this.challengeId + ".png";
            File file = new File(path);
            Image img = new Image(file.toURI().toString(), 200, 200, true, true);
            createCanvas();
        } catch (Exception e) {

        }

    }

    public void createCanvas() {
        this.canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        challengeContainer.getChildren().add(canvas);

        String path = "challenge/Challenge#" + this.challengeId + ".png";
        File file = new File(path);
        Image img = new Image(file.toURI().toString(), 200, 200, true, true);
        WritableImage wImg = ImageTools.convertImage(img);
        gc.drawImage(wImg, 0, 0);
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
    }

    public void NégativeFilter() {
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
    }



    public void drawCanvasImage(Image img) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(img, 0, 0);
    }


}
