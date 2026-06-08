package fr.framelab.DTO;

import fr.framelab.Service.UrlManager;

public class ChallengeDataDTO {
    private int id;
    private String title_theme;
    private String description_theme;
    private String picture;
    private String date_start;
    private String date_end;


    public ChallengeDataDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle_theme() {
        return title_theme;
    }

    public void setTitle_theme(String title_theme) {
        this.title_theme = title_theme;
    }

    public String getDescription_theme() {
        return description_theme;
    }

    public void setDescription_theme(String description_theme) {
        this.description_theme = description_theme;
    }

    public String getPicture() {

        return picture;
    }

    public String getFullPicture(){
        return UrlManager.getURL() + picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDate_start() {
        return date_start;
    }

    public void setDate_start(String date_start) {
        this.date_start = date_start;
    }

    public String getDate_end() {
        return date_end;
    }

    public void setDate_end(String date_end) {
        this.date_end = date_end;
    }
}

