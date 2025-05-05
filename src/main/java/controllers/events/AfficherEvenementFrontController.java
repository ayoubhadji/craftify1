package controllers.events;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import entity.Evenement;
import javafx.stage.Stage;
import services.events.ServiceEvenement;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherEvenementFrontController implements Initializable {
    @FXML
    private GridPane eventGrid;
    
    @FXML
    private TextField searchField;

    private final ServiceEvenement serviceEvenement = new ServiceEvenement();
    private List<Evenement> evenements;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadEvents();
        setupSearch();
    }

    private void loadEvents() {
        evenements = serviceEvenement.afficher();
        displayEvents(evenements);
    }

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            String searchText = newValue.toLowerCase();
            List<Evenement> filteredEvents = evenements.stream()
                .filter(e -> e.getNom().toLowerCase().contains(searchText) ||
                           e.getDescription().toLowerCase().contains(searchText) ||
                           e.getLieu().toLowerCase().contains(searchText))
                .toList();
            displayEvents(filteredEvents);
        });
    }

    private void displayEvents(List<Evenement> eventsToDisplay) {
        eventGrid.getChildren().clear();
        int column = 0;
        int row = 0;

        for (Evenement evenement : eventsToDisplay) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/events/yasmine/EvenementCardFront.fxml"));
                Node eventCard = loader.load();
                EvenementCardFrontController controller = loader.getController();
                controller.setEvenement(evenement);

                eventGrid.add(eventCard, column, row);
                column++;
                if (column == 3) {
                    column = 0;
                    row++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void goToProfile(ActionEvent event) {
        loadScene(event, "/org.example/user/profile.fxml", "Mon Profil");
    }
    public void logout(ActionEvent event) {
        loadScene(event, "/org.example/user/login.fxml", "Connexion");
    }
    public void goHome(ActionEvent event) {
        loadScene(event, "/org.example/home.fxml", "Accueil");
    }
    public void goprudect(ActionEvent event) { loadScene(event, "/org.example/produit/AfficherProduitFront.fxml", "produit");
    }
    public void gopost(ActionEvent event) { loadScene(event, "/org.example/blog/Post.fxml", "blog");
    }
    public void gotoevents(ActionEvent event) { loadScene(event, "/org.example/events/yasmine/AfficherEvenementFront.fxml", "events");

    }
    public void gotofoire(ActionEvent event) { loadScene(event, "/org.example/foire/ViewFoire2.fxml", "foire");
    }


    private void loadScene(ActionEvent event, String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}