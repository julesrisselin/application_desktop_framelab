//package fr.framelab.Service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import fr.framelab.DTO.ChallengeDTO;
//
//import java.io.IOException;
//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//
//public class sendPart {
//
//    private final HttpClient client;
//    private final ObjectMapper mapper;
//
//    public sendPart () {
//        this.client = HttpClientManager.getClient();
//        this.mapper = new ObjectMapper();
//    }
//
//    public void subPart() throws Exception {
//        try {
//            // Construction de la requête
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(URI.create("http://localhost:3000/api/participations"))
//                    .POST()
//                    .build();
//            // Envoi et réception
//            HttpResponse<String> response = client.send(request,
//                    HttpResponse.BodyHandlers.ofString());
//            // Vérification du statut
//            if (response.statusCode() == 200) {
//                // Parsing du JSON en objet Java
//                try {
//
//                }
//                catch (Exception e){
//                }
//            }
//            // Si le statut n'est pas 200, on lève une exception
//            throw new Exception("Erreur API: " + response.statusCode());
//        }
//        catch (IOException | InterruptedException e) {
//            // Toutes les erreurs réseau/parsing sont encapsulées
//            throw new Exception("Échec récupération du challenge en cours", e);
//        }
//    }
//}
