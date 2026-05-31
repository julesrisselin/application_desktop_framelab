package fr.framelab.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mizosoft.methanol.MultipartBodyPublisher;
import fr.framelab.DTO.ChallengeDTO;
import fr.framelab.DTO.PartDTO;

import javax.naming.PartialResultException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;

public class sendPart {

    private final HttpClient client;
    private final ObjectMapper mapper;

    public sendPart () {
        this.client = HttpClientManager.getClient();
        this.mapper = new ObjectMapper();
    }

    public PartDTO subPart(int id_challenge, String pathImg) throws Exception {
        try {
            String token = SessionManager.getToken();
            int userId = SessionManager.getUserId();

            MultipartBodyPublisher multipartBody = MultipartBodyPublisher.newBuilder()
                    .textPart("id_challenge", String.valueOf(id_challenge))
                    .textPart("user_id", String.valueOf(userId))
                    .filePart("image", Path.of(pathImg)) // Methanol détecte le Content-Type auto
                    .build();

            // Construction de la requête
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://framelab.jules-risselin.fr/api/participations"))
                    .header("Content-Type", "application/json")
                    .POST(multipartBody)
                    .build();
            // Envoi et réception
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
            // Vérification du statut
            if (response.statusCode() == 200|| response.statusCode() == 201) {
                return mapper.readValue(response.body(), PartDTO.class);
            }
            // Si le statut n'est pas 200, on lève une exception
            throw new Exception("Erreur API: " + response.statusCode());
        }
        catch (IOException | InterruptedException e) {
            // Toutes les erreurs réseau/parsing sont encapsulées
            throw new Exception("Échec de l'envoi de la participation", e);
        }
    }
}
