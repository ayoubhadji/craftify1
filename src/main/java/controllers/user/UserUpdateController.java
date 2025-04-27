package controllers.user;

import DAO.UserDAO;
import entity.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.Session;

public class UserUpdateController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField codeField;
    @FXML private TextField sexeField;
    @FXML private TextField dateNaissanceField;
    @FXML private TextField telField;
    @FXML private TextField addressField;

    @FXML private Button saveButton;
    @FXML private Button backButton;
    private final UserDAO userDAO = new UserDAO();

    public void initialize() {
        User user = Session.getCurrentUser();
        if (user != null) {
            nameField.setText(user.getNom());
            emailField.setText(user.getEmail());
            codeField.setText(user.getCode());
            sexeField.setText(user.getSexe());
            dateNaissanceField.setText(user.getDateNaissance().toString());
            telField.setText(user.getTel());
            addressField.setText(user.getAddress());
        }

        saveButton.setOnAction(event -> {
            if (user != null) {
                user.setNom(nameField.getText());
                user.setEmail(emailField.getText());
                user.setCode(codeField.getText());
                user.setSexe(sexeField.getText());
                user.setTel(telField.getText());
                user.setAddress(addressField.getText());


                try {
                    userDAO.update(user);
                    Session.setCurrentUser(user);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Mise à jour");
                    alert.setHeaderText(null);
                    alert.setContentText("Profil mis à jour avec succès !");
                    alert.showAndWait();

                } catch (Exception e) {
                    e.printStackTrace();

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Une erreur est survenue");
                    alert.setContentText("Impossible de mettre à jour le profil.");
                    alert.showAndWait();
                }
            }
        });

        backButton.setOnAction(event -> {
            try {

                Parent root = FXMLLoader.load(getClass().getResource("/org.example/home.fxml"));
                Stage stage = (Stage) backButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Accueil");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
