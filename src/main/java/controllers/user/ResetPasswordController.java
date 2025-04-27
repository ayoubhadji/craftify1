package controllers.user;

import DAO.UserDAO;
import entity.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

//
import java.util.Base64;
import java.nio.charset.StandardCharsets;
//

public class ResetPasswordController {

    private UserDAO userDAO = new UserDAO();

    @FXML
    private PasswordField newPasswordField;
    @FXML
    private TextField tokenField;
    @FXML
    private Label statusLabel;

    @FXML
    private void handlePasswordReset() {
        String newPassword = newPasswordField.getText().trim();
        String token = tokenField.getText().trim();

        if (newPassword.isEmpty() || token.isEmpty()) {
            statusLabel.setText("Veuillez remplir tous les champs.");
            statusLabel.setVisible(true);
            return;
        }

        // Vérifie si le token existe et réinitialise le mot de passe
        User user = userDAO.getByResetToken(token);
        if (user != null) {
            try {
                String decoded = new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8);
                String[] parts = decoded.split(":");

                if (parts.length != 2) {
                    statusLabel.setText("Format de token invalide.");
                    statusLabel.setVisible(true);
                    return;
                }

                long expirationTime = Long.parseLong(parts[1]);
                if (System.currentTimeMillis() > expirationTime) {
                    statusLabel.setText("Token expiré.");
                    statusLabel.setVisible(true);
                    return;
                }

                // Token valide, changer mot de passe
                boolean success = userDAO.resetPasswordByToken(token, newPassword);
                if (success) {
                    statusLabel.setText("Mot de passe réinitialisé avec succès !");
                    statusLabel.setStyle("-fx-text-fill: green;");
                } else {
                    statusLabel.setText("Erreur lors de la réinitialisation.");
                }

            } catch (Exception e) {
                statusLabel.setText("Erreur de décodage du token.");
            }

        } else {
            statusLabel.setText("Token invalide ou expiré.");
        }
        statusLabel.setVisible(true);

    }
}
