package controllers.foire;

import entity.SliderItem;
import services.foire.SliderItemService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ViewItemsController {
    @FXML
    private ListView<SliderItem> itemListView;

    private final SliderItemService sliderItemService = new SliderItemService();
    private final ObservableList<SliderItem> items = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        items.setAll(sliderItemService.getAllSliderItems());
        itemListView.setItems(items);
        itemListView.setCellFactory(listView -> new ListCell<>() {
            private final Label title = new Label();
            private final Label desc = new Label();
            private final Label field = new Label();
            private final Button btnModifier = new Button("Modifier");
            private final Button btnSupprimer = new Button("Supprimer");
            private final VBox vbox = new VBox(6, title, desc, field);
            private final HBox hbox = new HBox(10, vbox, btnModifier, btnSupprimer);

            {
                vbox.setPadding(new Insets(10, 18, 10, 18));
                vbox.getStyleClass().add("list-cell");
                title.getStyleClass().add("card-title");
                desc.getStyleClass().add("card-label");
                field.getStyleClass().add("card-value");
                btnModifier.getStyleClass().add("card-btn");
                btnSupprimer.getStyleClass().add("card-btn");
                HBox.setHgrow(vbox, Priority.ALWAYS);

                btnModifier.setOnAction(e -> {
                    SliderItem item = getItem();
                    if (item != null) {
                        handleModifyItem(item);
                    }
                });
                btnSupprimer.setOnAction(e -> {
                    SliderItem item = getItem();
                    if (item != null) {
                        handleDeleteItem(item);
                    }
                });
            }

            @Override
            protected void updateItem(SliderItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    String nom = "";
                    String description = "";
                    String fieldValue = "";
                    try { nom = (String) item.getClass().getField("nom").get(item); } catch (Exception ignored) {}
                    try { description = (String) item.getClass().getField("description").get(item); } catch (Exception ignored) {}
                    try { fieldValue = (String) item.getClass().getField("field").get(item); } catch (Exception ignored) {}

                    if ((nom == null || nom.isEmpty()) && (description == null || description.isEmpty()) && (fieldValue == null || fieldValue.isEmpty())) {
                        title.setText(item.toString());
                        desc.setText("");
                        field.setText("");
                    } else {
                        title.setText(nom != null ? nom : "");
                        desc.setText(description != null ? description : "");
                        field.setText(fieldValue != null ? fieldValue : "");
                    }
                    setGraphic(hbox);
                    setText(null);
                }
            }
        });
    }

    @FXML
    private void handleGoToFoireList(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/foire/viewFoire.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Impossible d'ouvrir la liste des foires.");
        }
    }

    @FXML
    private void handleAddItem(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/foire/addItem.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Ajouter un Item");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
            items.setAll(sliderItemService.getAllSliderItems());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Impossible d'ajouter un item.");
        }
    }

    // Overload for cell button
    private void handleModifyItem(SliderItem selectedItem) {
        if (selectedItem == null) {
            showAlert("Veuillez sélectionner un item à modifier.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/foire/updateItem.fxml"));
            Parent root = loader.load();
            // Pass the selected item to the update controller
            Object controller = loader.getController();
            if (controller instanceof UpdateItemController) {
                ((UpdateItemController) controller).setSliderItem(selectedItem);
            }
            Stage stage = new Stage();
            stage.setTitle("Modifier l'Item");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
            items.setAll(sliderItemService.getAllSliderItems());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Impossible de modifier l'item.");
        }
    }

    // For FXML compatibility (if needed)
    @FXML
    private void handleModifyItem(ActionEvent event) {
        SliderItem selectedItem = itemListView.getSelectionModel().getSelectedItem();
        handleModifyItem(selectedItem);
    }

    @FXML
    private void handleDeleteItem(ActionEvent event) {
        SliderItem selectedItem = itemListView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Veuillez sélectionner un item à supprimer.");
            return;
        }
        handleDeleteItem(selectedItem);
    }

    private void handleDeleteItem(SliderItem item) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer cet item ?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText(null);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                sliderItemService.deleteSliderItem(item.getId());
                items.setAll(sliderItemService.getAllSliderItems());
            }
        });
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }
}
