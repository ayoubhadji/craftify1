package controllers.foire;

import entity.SliderItem;
import services.foire.SliderItemService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddItemController {

    @FXML private TextField foireIdField;
    @FXML private TextField imagePathField;
    @FXML private TextField altTextField;
    @FXML private TextField urlField;
    @FXML private TextField positionField;

    private final SliderItemService sliderItemService = new SliderItemService();

    @FXML
    private void handleSave() {
        try {
            int foireId = Integer.parseInt(foireIdField.getText());
            String imagePath = imagePathField.getText();
            String altText = altTextField.getText();
            String url = urlField.getText();
            int position = Integer.parseInt(positionField.getText());

            if (imagePath.isEmpty() || altText.isEmpty() || url.isEmpty()) {
                showAlert("Tous les champs doivent être remplis.");
                return;
            }

            SliderItem item = new SliderItem();
            item.setFoireId(foireId);
            item.setImagePath(imagePath);
            item.setAltText(altText);
            item.setUrl(url);
            item.setPosition(position);

            sliderItemService.addSliderItem(item);

            Stage stage = (Stage) foireIdField.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            showAlert("Foire ID et Position doivent être des entiers.");
        }
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) foireIdField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }
}
