package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import models.Items;
import services.ItemsS;

import java.util.List;

public class items {

    @FXML
    private ListView<Items> view; // La ListView des items

    private ItemsS itemsService;

    public items() {
        itemsService = new ItemsS();
    }

    @FXML
    public void initialize() {
        // Récupérer la liste des items depuis la base de données
        List<Items> itemsList = itemsService.getAll();

        // Ajouter les items à la ListView
        view.getItems().addAll(itemsList);

        // Définir la manière d'afficher les items dans la ListView
        view.setCellFactory(param -> new ListCell<Items>() {
            @Override
            protected void updateItem(Items item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    // Format de l'affichage
                    setText("ID: " + item.getId() + " | " + "URL: " + item.getUrl() + " | " + "Image: " + item.getImage_path());
                }
            }
        });
    }
    private int foireId; // ID de la foire

    // Méthode pour recevoir l'ID de la foire
    public void initData(int foireId) {
        this.foireId = foireId;
        afficherItems(); // Charger les items après avoir reçu l'ID
    }
    private void afficherItems() {
        // Récupérer la liste des items associés à la foire (en utilisant foireId)
        List<Items> itemsList = itemsService.getItemsByFoireId(foireId);

        // Effacer les anciens éléments
        view.getItems().clear();

        // Ajouter les items à la ListView
        view.getItems().addAll(itemsList);
    }
    @FXML
    private void handleModifierAction() {
        // Logique pour modifier un item
    }

}
