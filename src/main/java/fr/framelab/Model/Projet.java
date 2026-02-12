package fr.framelab.Model;

public class Projet {
    protected int id;
    protected String name;
    protected String picture;
    protected String date_start;
    protected String date_last_edit;
    protected int id_challenge;

    public Projet(int id, String name, String picture, String date_start, String date_last_edit, int id_challenge) {
        this.id = id;
        this.name = name;
        this.picture = picture;
        this.date_start = date_start;
        this.date_last_edit = date_last_edit;
        this.id_challenge = id_challenge;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
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

    public String getDate_last_edit() {
        return date_last_edit;
    }

    public void setDate_last_edit(String date_last_edit) {
        this.date_last_edit = date_last_edit;
    }

    public int getId_challenge() {
        return id_challenge;
    }

    public void setId_challenge(int id_challenge) {
        this.id_challenge = id_challenge;
    }
}
