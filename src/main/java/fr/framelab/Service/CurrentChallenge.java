package fr.framelab.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.framelab.DTO.ChallengeDTO;
import fr.framelab.DTO.TokenDTO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CurrentChallenge {

    private final HttpClient client;
    private final ObjectMapper mapper;

    public CurrentChallenge () {
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    public ChallengeDTO getChallenge() throws Exception {
        try {
            // Construction de la requête
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://framelab.jules-risselin.fr/api/challenges/current"))
                    .GET()
                    .build();
            // Envoi et réception
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
            // Vérification du statut
            if (response.statusCode() == 200) {
                // Parsing du JSON en objet Java
                try {
                    ChallengeDTO currentChallenge  = mapper.readValue(response.body(), ChallengeDTO.class);// Parsing du JSON en objet Java
                    return currentChallenge;
                }
                catch (Exception e){
                }
            }
            // Si le statut n'est pas 200, on lève une exception
            throw new Exception("Erreur API: " + response.statusCode());
        }
        catch (IOException | InterruptedException e) {
            // Toutes les erreurs réseau/parsing sont encapsulées
            throw new Exception("Échec récupération du challenge en cours", e);
        }
        }
    }

