package fr.framelab.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.framelab.DTO.LoginDTO;
import fr.framelab.DTO.TokenDTO;
import fr.framelab.Model.User;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

public class RecupUser {

    private final HttpClient client;
    private final ObjectMapper mapper;
    private final CookieManager cookieManager;

    public RecupUser() {
        this.cookieManager = new CookieManager();
        this.cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        this.client = HttpClient.newBuilder()
                .cookieHandler(cookieManager)
                .build();
        this.mapper = new ObjectMapper();
    }

    public TokenDTO getUser(String email, String password) throws Exception {
        try {
            String jsonBody = mapper.writeValueAsString(
                    new LoginDTO(email, password)
            );

            // Construction de la requête
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:3000/api/auth/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            // Envoi et réception
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
            // Vérification du statut
            if (response.statusCode() == 200) {
                try {
                    TokenDTO token  = mapper.readValue(response.body(), TokenDTO.class);// Parsing du JSON en objet Java
                    return token;
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }

            // Si le statut n'est pas 200, on lève une exception
            throw new Exception("Erreur API: " + response.statusCode());
        } catch (IOException | InterruptedException e) {

            throw new Exception("Échec de connexion", e);
        }
    }
}
