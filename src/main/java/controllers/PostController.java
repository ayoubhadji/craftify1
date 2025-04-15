package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Post;
import models.Commentaire;
import services.CommentaireService;
import services.PostService;
import utils.MyDb;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class PostController {

    @FXML private TextField typePostField;
    @FXML private TextField contenuField;
    @FXML private TextField mediaUrlField;
    @FXML private TextField trancheDageField;
    @FXML private TextField nmbLikeField;
    @FXML private DatePicker datePublicationPicker;
    @FXML private VBox postContainer;
    @FXML
    private TitledPane postFormPane;

    @FXML
    private Button toggleFormButton;
    private PostService postService;
    private Post postToEdit;

    public void initialize() {
        postFormPane.setVisible(false);
        postFormPane.setManaged(false);
        this.postService = new PostService(MyDb.getInstance().getConnection());
        refreshCards();
    }
    @FXML
    private void togglePostForm() {
        boolean isVisible = postFormPane.isVisible();
        postFormPane.setVisible(!isVisible);
        postFormPane.setManaged(!isVisible);

        toggleFormButton.setText(isVisible ? "➕ Ajouter un Post" : "⬆️ Cacher le formulaire");
    }

    @FXML
    private void cancelEditPost() {
        postFormPane.setVisible(false);
        postFormPane.setManaged(false);
        toggleFormButton.setText("➕ Ajouter un Post");
    }


    public void refreshCards() {
        try {
            postContainer.getChildren().clear();
            List<Post> posts = postService.getAllPosts();

            for (Post post : posts) {
                VBox card = new VBox(5);
                card.setStyle("-fx-border-color: #ccc; -fx-background-color: #f9f9f9; -fx-padding: 10; -fx-border-radius: 5; -fx-background-radius: 5;");
                card.setPrefWidth(500);

                Label typeLabel = new Label("Type: " + post.getTypePost());
                typeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

                Label contenuLabel = new Label("Contenu: " + post.getContenu());
                contenuLabel.setWrapText(true);

                Label dateLabel = new Label("Date: " + post.getDatePublication().toLocalDate());
                Label trancheLabel = new Label("Tranche d'âge: " + post.getTrancheDage());
                Label likesLabel = new Label("Likes: " + post.getNmbLike());

                // Show image
                if (post.getMediaUrl() != null && !post.getMediaUrl().isEmpty()) {
                    File imageFile = new File(post.getMediaUrl());
                    if (imageFile.exists()) {
                        try {
                            Image image = new Image(imageFile.toURI().toString(), 200, 200, true, true);
                            ImageView imageView = new ImageView(image);
                            card.getChildren().add(imageView);
                        } catch (Exception e) {
                            System.err.println("Failed to load image: " + e.getMessage());
                        }
                    }
                }

                VBox commentSection = new VBox(5);
                Label commentTitle = new Label("Commentaires:");
                commentTitle.setStyle("-fx-font-style: italic;");
                commentSection.getChildren().add(commentTitle);

                List<Commentaire> commentaires = postService.getCommentairesByPostId(post.getIdPost());
                for (Commentaire com : commentaires) {
                    HBox commentRow = new HBox(10);
                    commentRow.setStyle("-fx-alignment: center-left;");

                    Label commentLabel = new Label("- " + com.getContenu());
                    commentLabel.setWrapText(true);
                    commentLabel.setMaxWidth(300);

                    Button editBtn = new Button("✏️");
                    editBtn.setOnAction(e -> {
                        TextInputDialog dialog = new TextInputDialog(com.getContenu());
                        dialog.setTitle("Modifier le commentaire");
                        dialog.setHeaderText(null);
                        dialog.setContentText("Nouveau contenu:");
                        dialog.showAndWait().ifPresent(newContent -> {
                            if (!newContent.trim().isEmpty()) {
                                try {
                                    com.setContenu(newContent);
                                    com.setDateCommentaire(LocalDateTime.now());
                                    new CommentaireService(MyDb.getInstance().getConnection()).updateCommentaire(com);
                                    refreshCards();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });
                    });

                    Button deleteBtn = new Button("🗑️");
                    deleteBtn.setOnAction(e -> {
                        try {
                            new CommentaireService(MyDb.getInstance().getConnection()).deleteCommentaire(com.getId());
                            refreshCards();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });

                    commentRow.getChildren().addAll(commentLabel, editBtn, deleteBtn);
                    commentSection.getChildren().add(commentRow);
                }

                Button commentBtn = new Button("Ajouter un commentaire");
                commentBtn.setOnAction(e -> {
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Ajouter un commentaire");
                    dialog.setHeaderText(null);
                    dialog.setContentText("Commentaire:");
                    dialog.showAndWait().ifPresent(commentText -> {
                        if (!commentText.trim().isEmpty()) {
                            try {
                                Commentaire newComment = new Commentaire(
                                        0,
                                        post.getIdPost(),
                                        1,
                                        commentText,
                                        LocalDateTime.now(),
                                        0
                                );
                                postService.addComment(newComment);
                                refreshCards();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                });

                HBox actions = new HBox(10);
                Button deleteBtn = new Button("Delete");
                deleteBtn.setOnAction(e -> {
                    try {
                        postService.deletePost(post.getIdPost());
                        refreshCards();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                Button editBtn = new Button("Edit");
                editBtn.setOnAction(e -> openEditPostWindow(post));
                actions.getChildren().addAll(editBtn, deleteBtn);

                card.getChildren().addAll(typeLabel, contenuLabel, dateLabel, trancheLabel, likesLabel, actions, commentBtn, commentSection);
                postContainer.getChildren().add(card);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void addPost() {
        try {
            if (datePublicationPicker.getValue() == null) {
                showError("La date de publication est obligatoire.");
                return;
            }

            int likes;
            try {
                likes = Integer.parseInt(nmbLikeField.getText());
            } catch (NumberFormatException e) {
                showError("Le nombre de likes doit être un nombre.");
                return;
            }

            Post post = new Post(
                    0,
                    1, // static user ID
                    typePostField.getText(),
                    contenuField.getText(),
                    mediaUrlField.getText(),
                    datePublicationPicker.getValue().atStartOfDay(),
                    trancheDageField.getText(),
                    likes
            );

            postService.addPost(post);
            refreshCards();
            clearFields();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        typePostField.clear();
        contenuField.clear();
        mediaUrlField.clear();
        trancheDageField.clear();
        nmbLikeField.clear();
        datePublicationPicker.setValue(null);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de saisie");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void browseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            mediaUrlField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void openEditPostWindow(Post post) {
        try {
            this.postToEdit = post;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditPost.fxml"));
            loader.setController(this);
            Parent root = loader.load();

            // Fill form fields with existing post data
            typePostField.setText(post.getTypePost());
            contenuField.setText(post.getContenu());
            mediaUrlField.setText(post.getMediaUrl());
            datePublicationPicker.setValue(post.getDatePublication().toLocalDate());
            trancheDageField.setText(post.getTrancheDage());
            nmbLikeField.setText(String.valueOf(post.getNmbLike()));

            Stage stage = new Stage();
            stage.setTitle("Edit Post");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updatePost() throws SQLException {
        if (postToEdit != null) {
            if (datePublicationPicker.getValue() == null) {
                showError("La date de publication est obligatoire.");
                return;
            }

            postToEdit.setTypePost(typePostField.getText());
            postToEdit.setContenu(contenuField.getText());
            postToEdit.setMediaUrl(mediaUrlField.getText());
            postToEdit.setDatePublication(datePublicationPicker.getValue().atStartOfDay());
            postToEdit.setTrancheDage(trancheDageField.getText());
            postToEdit.setNmbLike(Integer.parseInt(nmbLikeField.getText()));

            postService.updatePost(postToEdit);
            refreshCards();

            Stage stage = (Stage) typePostField.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    private void cancelEdit() {
        Stage stage = (Stage) typePostField.getScene().getWindow();
        stage.close();
    }
}
