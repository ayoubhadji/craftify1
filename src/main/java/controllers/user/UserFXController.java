package controllers.user;

import entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.LocalDate;

public class UserFXController {

    @FXML private TextField nomField;
    @FXML private TextField emailField;
    @FXML private TextField codeField;
    @FXML private TextField telField;
    @FXML private TextField addressField;
    @FXML private TextField fiscalField;
    @FXML private TextField tokenField;
    @FXML private ListView<String> userList;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private ComboBox<String> sexeComboBox;
    @FXML private DatePicker dateNaissancePicker;

    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Integer> idCol;
    @FXML private TableColumn<User, String> nomCol;
    @FXML private TableColumn<User, String> emailCol;
    @FXML private TableColumn<User, String> codeCol;
    @FXML private TableColumn<User, String> roleCol;
    @FXML private TableColumn<User, String> sexeCol;
    @FXML private TableColumn<User, LocalDateTime> naissanceCol;
    @FXML private TableColumn<User, LocalDateTime> joinCol;
    @FXML private TableColumn<User, String> telCol;
    @FXML private TableColumn<User, String> addressCol;
    @FXML
    private Pagination pagination;


    @FXML
    private PieChart genderPieChart;

    @FXML private TextField searchField;

    private final UserController userController = new UserController();
    private static final int ROWS_PER_PAGE = 5; // 🔥 5 users per page
    private List<User> allUsers; // list of ALL users fetched once

    @FXML
    public void initialize() {
        allUsers = userController.getAllUsers();

        roleComboBox.getItems().addAll("ADMIN", "USER", "ARTISAN");
        sexeComboBox.getItems().addAll("Homme", "Femme");

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        sexeCol.setCellValueFactory(new PropertyValueFactory<>("sexe"));
        naissanceCol.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        joinCol.setCellValueFactory(new PropertyValueFactory<>("dateJoin"));
        telCol.setCellValueFactory(new PropertyValueFactory<>("tel"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        // ⬇️ Activer le tri sur certaines colonnes
        idCol.setSortable(true);
        nomCol.setSortable(true);
        emailCol.setSortable(true);

        pagination.setPageFactory(this::createPage); // 👈 pagination magic
        updateGenderChart();
    }
    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        if (fromIndex >= allUsers.size()) {
            // 🔥 Protection si l'index déborde
            userTable.setItems(FXCollections.observableArrayList());
            return userTable;
        }

        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, allUsers.size());
        List<User> pageData = allUsers.subList(fromIndex, toIndex);
        userTable.setItems(FXCollections.observableArrayList(pageData));
        return userTable;
    }



    @FXML
    public void ajouterUser() {
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

        // ⚠️ Validation de base
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

        boolean success = userController.userDAO.insert(user);
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur ajouté avec succès.");
            afficherUtilisateurs();
            clearFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'ajout de l'utilisateur.");
        }
    }

    private void clearFields() {
        nomField.clear();
        emailField.clear();
        codeField.clear();
        telField.clear();
        addressField.clear();
        roleComboBox.setValue(null);
        sexeComboBox.setValue(null);
        dateNaissancePicker.setValue(null);
    }

    private void afficherUtilisateurs() {
        allUsers = userController.getAllUsers();
        pagination.setPageCount((int) Math.ceil(allUsers.size() * 1.0 / ROWS_PER_PAGE));
        pagination.setCurrentPageIndex(0); // 🔥 Remettre à la 1ère page
        pagination.setPageFactory(this::createPage);
    }


    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    //

    @FXML
    public void updateSelectedUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert(Alert.AlertType.WARNING, "Aucun utilisateur sélectionné", "Veuillez sélectionner un utilisateur à modifier.");
            return;
        }

        String nom = nomField.getText().trim();
        String email = emailField.getText().trim();
        String code = codeField.getText().trim();
        String role = roleComboBox.getValue();
        String sexe = sexeComboBox.getValue();
        LocalDate dateNaissance = dateNaissancePicker.getValue();
        String tel = telField.getText().trim();
        String address = addressField.getText().trim();

        if (nom.isEmpty() || email.isEmpty() || code.isEmpty() || role == null || sexe == null || dateNaissance == null || tel.isEmpty() || address.isEmpty()) {
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

        sexe = sexe.substring(0, 1).toUpperCase() + sexe.substring(1);
        role = role.toUpperCase();

        selectedUser.setNom(nom);
        selectedUser.setEmail(email);
        selectedUser.setCode(code);
        selectedUser.setRole(role);
        selectedUser.setSexe(sexe);
        selectedUser.setDateNaissance(dateNaissance.atStartOfDay());
        selectedUser.setTel(tel);
        selectedUser.setAddress(address);

        boolean success = userController.userDAO.update(selectedUser);
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur mis à jour avec succès.");
            afficherUtilisateurs();
            clearFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la mise à jour de l'utilisateur.");
        }
    }


    @FXML
    private void showSelectedUser() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            nomField.setText(selected.getNom());
            emailField.setText(selected.getEmail());
            codeField.setText(selected.getCode());
            roleComboBox.setValue(selected.getRole());
            sexeComboBox.setValue(selected.getSexe());
            dateNaissancePicker.setValue(selected.getDateNaissance().toLocalDate());
            telField.setText(selected.getTel());
            addressField.setText(selected.getAddress());
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucun utilisateur sélectionné", "Veuillez sélectionner un utilisateur à afficher.");
        }
    }

    private void updateGenderChart() {
        List<User> users = userController.getAllUsers();
        long hommeCount = users.stream().filter(u -> u.getSexe().equalsIgnoreCase("Homme")).count();
        long femmeCount = users.stream().filter(u -> u.getSexe().equalsIgnoreCase("Femme")).count();

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Hommes", hommeCount),
                new PieChart.Data("Femmes", femmeCount)
        );

        genderPieChart.setData(pieChartData);
        genderPieChart.setTitle("Répartition Hommes / Femmes");

        // 🔥 Affiche les valeurs sur le graphique
        pieChartData.forEach(data ->
                data.nameProperty().set(data.getName() + " (" + (int) data.getPieValue() + ")")
        );
    }

    @FXML
    private void deleteSelectedUser() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            boolean confirmed = showConfirmation("Confirmer la suppression", "Êtes-vous sûr de vouloir supprimer cet utilisateur ?");
            if (confirmed) {
                boolean deleted = userController.userDAO.delete(selected.getId());
                if (deleted) {
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur supprimé avec succès.");
                    afficherUtilisateurs();
                    clearFields();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la suppression.");
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucun utilisateur sélectionné", "Veuillez sélectionner un utilisateur à supprimer.");
        }
    }

    private boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();

    }
    @FXML
    private void filterUsers() {
        String keyword = searchField.getText().toLowerCase().trim();
        List<User> allUsers = userController.getAllUsers();

        List<User> filtered = allUsers.stream()
                .filter(u -> u.getNom().toLowerCase().contains(keyword) || u.getEmail().toLowerCase().contains(keyword))
                .toList();

        userTable.setItems(FXCollections.observableArrayList(filtered));
    }


    @FXML
    private void retourDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
