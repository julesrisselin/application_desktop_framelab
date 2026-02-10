package fr.framelab.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CurrentChallenge {

    private final HttpClient client;
    private final ObjectMapper mapper;
    private final String apiKey;

    public CurrentChallenge(String apiKey) {
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
        this.apiKey = apiKey;
    }

    public CurrentChallenge getRates(String baseCurrency) {
        try {
// Construction de la requête
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:3000/api/challenge/current"))
                    .header("Authorization", apiKey)
                    .GET()
                    .build();
// Envoi et réception
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
// Vérification du statut
            if (response.statusCode() == 200) {
// Parsing du JSON en objet Java
                return mapper.readValue(response.body(), CurrentChallenge.class);
            }
// Si le statut n'est pas 200, on lève une exception
            throw new Exception("Erreur API: " + response.statusCode());
        } catch (IOException | InterruptedException e) {
// Toutes les erreurs réseau/parsing sont encapsulées
            throw new Exception("Échec récupération taux", e);
        }
    }
}
