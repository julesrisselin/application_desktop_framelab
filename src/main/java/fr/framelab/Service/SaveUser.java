package fr.framelab.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.framelab.DTO.UserDTO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SaveUser {
    private final HttpClient client;
    private final ObjectMapper mapper;

    public SaveUser() {
        this.client = HttpClientManager.getClient();
        this.mapper = new ObjectMapper();

    }

    public UserDTO getUser() throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://framelab.jules-risselin.fr/api/users/me"))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("Erreur API :" + response.statusCode());
        }


        try {
            return mapper.readValue(response.body(), UserDTO.class);
        } catch (Exception e) {
            throw new Exception("Échec de la récupération du user", e);
        }
    }
}
