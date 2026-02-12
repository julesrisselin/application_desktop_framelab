package fr.framelab.Controller;

import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import javafx.embed.swing.SwingFXUtils;
import java.io.IOException;

public class ImageTools {


    public static void saveImg(Image img, String path) throws IOException {
        BufferedImage bImg = SwingFXUtils.fromFXImage(img, null);
        ImageIO.write(bImg,"png", new File(path));
    }

    public static void downloadImage(String URLImg) throws IOException{
        Image img = new Image(URLImg);
        saveImg(img, "projects/"+  (URLImg.split("/")[4]).split("\\.")[0]  + ".png");
    }
}