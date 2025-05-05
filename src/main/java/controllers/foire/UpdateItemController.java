package controllers.foire;

import entity.SliderItem;
import services.foire.SliderItemService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class UpdateItemController {

    @FXML private TextField foireIdField;
    @FXML private TextField imagePathField;
    @FXML private TextField altTextField;
    @FXML private TextField urlField;
    @FXML private TextField positionField;

    @FXML private Label foireIdError;
    @FXML private Label imagePathError;
    @FXML private Label altTextError;
    @FXML private Label urlError;
    @FXML private Label positionError;

    private final SliderItemService sliderItemService = new SliderItemService();
    private SliderItem sliderItem;

    public void setSliderItem(SliderItem sliderItem) {
        this.sliderItem = sliderItem;
        foireIdField.setText(String.valueOf(sliderItem.getFoireId()));
        imagePathField.setText(sliderItem.getImagePath());
        altTextField.setText(sliderItem.getAltText());
        urlField.setText(sliderItem.getUrl());
        positionField.setText(String.valueOf(sliderItem.getPosition()));
    }

    @FXML
    private void handleUpdate() {
        // Reset errors
        foireIdError.setText("");
        imagePathError.setText("");
        altTextError.setText("");
        urlError.setText("");
        positionError.setText("");

        boolean hasError = false;

        String foireIdStr = foireIdField.getText();
        String imagePath = imagePathField.getText();
        String altText = altTextField.getText();
        String url = urlField.getText();
        String positionStr = positionField.getText();

        int foireId = 0;
        int position = 0;

        if (foireIdStr == null || foireIdStr.trim().isEmpty()) {
            foireIdError.setText("Foire ID requis.");
            hasError = true;
        } else {
            try {
                foireId = Integer.parseInt(foireIdStr);
                if (foireId <= 0) {
                    foireIdError.setText("Foire ID doit être positif.");
                    hasError = true;
                }
            } catch (Exception e) {
                foireIdError.setText("Foire ID doit être un entier.");
                hasError = true;
            }
        }

        if (imagePath == null || imagePath.trim().isEmpty()) {
            imagePathError.setText("Image Path requis.");
            hasError = true;
        }
        if (altText == null || altText.trim().isEmpty()) {
            altTextError.setText("Alt Text requis.");
            hasError = true;
        }
        if (url == null || url.trim().isEmpty()) {
            urlError.setText("URL requise.");
            hasError = true;
        }
        if (positionStr == null || positionStr.trim().isEmpty()) {
            positionError.setText("Position requise.");
            hasError = true;
        } else {
            try {
                position = Integer.parseInt(positionStr);
                if (position < 0) {
                    positionError.setText("Position doit être >= 0.");
                    hasError = true;
                }
            } catch (Exception e) {
                positionError.setText("Position doit être un entier.");
                hasError = true;
            }
        }

        if (hasError) return;

        if (sliderItem == null) {
            throw new IllegalStateException("SliderItem must be set before updating.");
        }

        sliderItem.setFoireId(foireId);
        sliderItem.setImagePath(imagePath);
        sliderItem.setAltText(altText);
        sliderItem.setUrl(url);
        sliderItem.setPosition(position);

        sliderItemService.updateSliderItem(sliderItem);

        Stage stage = (Stage) foireIdField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) foireIdField.getScene().getWindow();
        stage.close();
    }
}
