package com.nijoat.frontend.controller.leaderboard;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LeaderboardController {
    @FXML
    private ListView<String> leaderboardListView;

    public void initialize() {
        System.out.println("Leaderboard näkyy");
        refreshLeaderboard();
    }

    private void refreshLeaderboard() {
        System.out.println("Leaderboard refresh");
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/leaderboard/top"))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String responseBody = response.body();

                // Muunna JSON-muotoinen vastaus JsonNode-objektiksi
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode leaderboardJson = objectMapper.readTree(responseBody);

                // Luo ObservableList leaderboard-tiedoille
                ObservableList<String> leaderboardEntries = FXCollections.observableArrayList();

                // Käy läpi JSON-vastauksen ja lisää käyttäjien nimet ja pisteet listaan
                for (JsonNode userNode : leaderboardJson) {
                    String username = userNode.get("username").asText();
                    int points = userNode.get("points").asInt();
                    leaderboardEntries.add(username + ": " + points + " points");
                }

                // Aseta leaderboardEntries ListView-komponenttiin
                leaderboardListView.setItems(leaderboardEntries);
            } else {
                // Virheenkäsittely, jos pyyntö epäonnistui
                displayErrorAlert("HTTP Error " + response.statusCode(), "Failed to fetch leaderboard data");
            }
        } catch (Exception e) {
            // Virheenkäsittely, jos tapahtui poikkeus
            displayErrorAlert("Error", "An error occurred while fetching leaderboard data");
            e.printStackTrace();
        }
    }

    private void displayErrorAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}