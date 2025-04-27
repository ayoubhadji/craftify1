package controllers.user;

import DAO.UserDAO;
import entity.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.collections.FXCollections;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SignUpController {

    @FXML
    private TextField nomField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField codeField;
    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    private ComboBox<String> sexeComboBox;
    @FXML
    private DatePicker dateNaissancePicker;
    @FXML
    private TextField telField;
    @FXML
    private TextField addressField;

    private UserDAO userDAO = new UserDAO();
    private final UserController userController = new UserController();

    // Initialize method to set default values in ComboBoxes
    public void initialize() {
        roleComboBox.setItems(FXCollections.observableArrayList("ADMIN", "USER" , "ARTISAN"));
        sexeComboBox.setItems(FXCollections.observableArrayList("Homme", "Femme"));
    }

    // Handle the Sign-Up process
    public void handleSignUp() {
        String nom = nomField.getText().trim();
        String email = emailField.getText().trim();
        String code = codeField.getText().trim();
        String role = roleComboBox.getValue();  // comboBox maintenant
        String sexe = sexeComboBox.getValue();  // comboBox maintenant
        LocalDate dateNaissance = dateNaissancePicker.getValue();
        String tel = telField.getText().trim();
        String address = addressField.getText().trim();
        String fiscal = "";
        String token = "";

        // Check if any field is empty
        if (nom.isEmpty() || email.isEmpty() || code.isEmpty() || role == null ||
                sexe == null || dateNaissance == null || tel.isEmpty() || address.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Champs manquants", "Tous les champs doivent être remplis.");
            return;
        }

        if (dateNaissance.isAfter(LocalDate.now())) {
            showAlert(Alert.AlertType.ERROR, "Date de naissance invalide", "La date de naissance doit être dans le passé.");
            return;
        }

        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showAlert(Alert.AlertType.ERROR, "Email invalide", "Veuillez entrer un email valide.");
            return;
        }

        if (!tel.matches("\\d{8,15}")) {
            showAlert(Alert.AlertType.ERROR, "Téléphone invalide", "Le téléphone doit contenir uniquement des chiffres (min. 8).");
            return;
        }

        // ✅ Validation du sexe
        if (!(sexe.equals("Homme") || sexe.equals("Femme"))) {
            showAlert(Alert.AlertType.ERROR, "Sexe invalide", "Le sexe doit être 'Homme' ou 'Femme'.");
            return;
        }

        // ✅ Validation du rôle
        if (!(role.equals("ADMIN") || role.equals("USER") || role.equals("ARTISAN"))) {
            showAlert(Alert.AlertType.ERROR, "Rôle invalide", "Le rôle doit être 'Admin', 'User' ou 'Artisan'.");
            return;
        }

        // Remettre les valeurs formatées proprement (avec majuscules par ex.)
        sexe = sexe.substring(0, 1).toUpperCase() + sexe.substring(1);
        role = role.substring(0, 1).toUpperCase() + role.substring(1);

        // Création de l'utilisateur
        User user = new User(
                nom,
                email,
                code,
                role,
                sexe,
                dateNaissance.atStartOfDay(),  // Utilise la vraie date de naissance
                LocalDateTime.now(),
                tel,
                address,
                fiscal,
                token
        );
        // Insert user into the database
        boolean success = userController.userDAO.insert(user);

        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sign Up Success");
            alert.setHeaderText("Account Created");
            alert.setContentText("You have successfully created an account!");
            alert.showAndWait();

            // Redirect to login page
            goToLoginPage();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sign Up Error");
            alert.setHeaderText("Account Creation Failed");
            alert.setContentText("There was an error while creating your account. Please try again.");
            alert.showAndWait();
        }
    }
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Redirect to the login page
    @FXML
    private void goToLoginPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/user/login.fxml"));
            AnchorPane root = loader.load();

            Stage stage = (Stage) nomField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page de connexion.");
        }
    }
}
