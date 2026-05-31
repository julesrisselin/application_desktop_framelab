package fr.framelab.Service;

import fr.framelab.DTO.UserDTO;

public class SessionManager {
    private static String token;
    private static UserDTO currentUser;

    public static void setToken(String token){
        token = token;
    }

    public static String getToken() {
        return token;
    }

    public static void SetCurrentUser(UserDTO user){
        currentUser = user;
    }

    public static UserDTO getCurrentUser() {
        return currentUser;
    }

    public static int getUserId(){
        if(currentUser == null){
            throw new IllegalArgumentException("Pas de user");
        }
        return currentUser.getId();
    }

    public static void clear(){
        token = null;
        currentUser = null;
    }

}
