package fr.framelab.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;

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