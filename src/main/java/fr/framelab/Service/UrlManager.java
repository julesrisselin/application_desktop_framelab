package fr.framelab.Service;

public class UrlManager {
    private static String url;
    private static boolean prod;

    public UrlManager (){

    }

    public static String getURL(){
        prod = false;
        if(prod){
            url = "http://framelab.jules-risselin.fr/";
        } else {
            url = "http://localhost:3000/";
        }
        return url;
    }
}
