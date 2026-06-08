package fr.framelab.DTO;

public class PartDTO {
    private int user_id;
    private int id_challenge;
    private String pathImg;
    private String message;
    private boolean success;

    public PartDTO() {
    }

    public int getUser_id() {
        return user_id;
    }

    public int getId_challenge() {
        return id_challenge;
    }

    public String getPathImg() {
        return pathImg;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
