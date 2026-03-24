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
    private Image image;
    private int rotation;
    private int challengeId;
    private Canvas canvas;

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

    public void setupChallenge(int challengeId) throws Exception {
        System.out.println("Là2");
        try {
            this.challengeId = challengeId;
            String path = "challenge/Challenge#" + this.challengeId + ".png";
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

        String path = "challenge/Challenge#" + this.challengeId + ".png";
        File file = new File(path);
        Image img = new Image(file.toURI().toString());
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
    }

    public void rotateImage(){
        this.rotation = (this.rotation + 90)%360;
        WritableImage source = ImageTools.copyImg(this.image);
        int h = (int) canvas.getHeight();
        int w = (int) canvas.getWidth();

        int centreX = w/2;
        int centreY = h/2;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, w, h);
        gc.save();
        gc.translate(centreX, centreY);
        gc.rotate(this.rotation);
        gc.translate(-centreX, -centreY);
        this.drawCanvasImage(source);
        gc.restore();
    }



    public void drawCanvasImage(Image img) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(img, 0, 0);
    }

    public void saveProject(){

    }


}
