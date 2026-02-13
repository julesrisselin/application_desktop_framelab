package fr.framelab.Controller;

import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

import java.io.IOException;

public class ImageTools {


    public static void saveImg(Image img, String path) throws IOException {
        BufferedImage bImg = SwingFXUtils.fromFXImage(img, null);
        ImageIO.write(bImg,"png", new File(path));
    }

    public static WritableImage convertImage(Image img){
        int width = (int) img.getWidth();
        int height = (int) img.getHeight();
        WritableImage wimg = new WritableImage(width, height);
        wimg.getPixelWriter().setPixels(0,0,width,height,img.getPixelReader(),0,0);
        return wimg;
    }

//    public static void downloadImage(String URLImg) throws IOException{
//        Image img = new Image(URLImg);
//        saveImg(img, "projects/"+  (URLImg.split("/")[4]).split("\\.")[0]  + ".png");
//    }
}