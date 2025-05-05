package controllers.blog;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import entity.Post;
import entity.Commentaire;
import services.blog.CommentaireService;
import services.blog.PostService;
import services.blog.ReactionService;
import utils.MyDataBase;
import utils.QRCodeGenerator;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import utils.PostCardFactory;
public class PostController {

    @FXML private ComboBox<String> typePostComboBox;

    @FXML private TextField contenuField;
    @FXML private TextField mediaUrlField;
    @FXML private TextField trancheDageField;
    @FXML private TextField nmbLikeField;
    @FXML private DatePicker datePublicationPicker;
    @FXML private VBox postContainer;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> typeFilter;

    @FXML
    private TitledPane postFormPane;

    @FXML
    private Button toggleFormButton;
    private PostService postService;
    private Post postToEdit;

    public static List<Post> recommendedPosts = new ArrayList<>();

    public static void openEditPostWindowStatic(Post post, Runnable onRefresh) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Modifier le Post");

        ButtonType updateButtonType = new ButtonType("✅ Modifier", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("❌ Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, cancelButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // ComboBox for Type
        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("Realism", "Impressionism", "Abstract");
        typeComboBox.setEditable(false);
        typeComboBox.setValue(post.getTypePost());

        TextField contenuField = new TextField(post.getContenu());
        TextField mediaUrlField = new TextField(post.getMediaUrl());
        DatePicker datePicker = new DatePicker(post.getDatePublication().toLocalDate());
        TextField trancheDageField = new TextField(post.getTrancheDage());
        TextField likesField = new TextField(String.valueOf(post.getNmbLike()));

        grid.add(new Label("Type:"), 0, 0);
        grid.add(typeComboBox, 1, 0);
        grid.add(new Label("Contenu:"), 0, 1);
        grid.add(contenuField, 1, 1);
        grid.add(new Label("Image URL:"), 0, 2);
        grid.add(mediaUrlField, 1, 2);
        grid.add(new Label("Date de publication:"), 0, 3);
        grid.add(datePicker, 1, 3);
        grid.add(new Label("Tranche d'âge:"), 0, 4);
        grid.add(trancheDageField, 1, 4);
        grid.add(new Label("Likes:"), 0, 5);
        grid.add(likesField, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                try {
                    post.setTypePost(typeComboBox.getValue());
                    post.setContenu(contenuField.getText());
                    post.setMediaUrl(mediaUrlField.getText());
                    post.setDatePublication(datePicker.getValue().atStartOfDay());
                    post.setTrancheDage(trancheDageField.getText());
                    post.setNmbLike(Integer.parseInt(likesField.getText()));

                    PostService postService = new PostService(MyDataBase.getInstance().getMyConnection());
                    postService.updatePost(post);

                    if (onRefresh != null) {
                        onRefresh.run();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return null;
        });

        dialog.showAndWait();
    }







    public void initialize() throws SQLException {
        postFormPane.setVisible(false);
        postFormPane.setManaged(false);
        this.postService = new PostService(MyDataBase.getInstance().getMyConnection());

        // Set up search + filter listeners
        searchField.textProperty().addListener((observable, oldValue, newValue) -> refreshCards());
        typeFilter.valueProperty().addListener((observable, oldVal, newVal) -> refreshCards());

        // Populate filter dropdown
        List<String> allTypes = postService.getAllPosts().stream()
                .map(Post::getTypePost)
                .distinct()
                .collect(Collectors.toList());
        allTypes.add(0, "Tous"); // Add "All" option at the top

        typeFilter.getItems().addAll(allTypes);
        typeFilter.getSelectionModel().selectFirst(); // Select "Tous" by default
        typePostComboBox.getItems().addAll("Realism", "Impressionism", "Abstract");
        typePostComboBox.setEditable(false);
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


    private boolean containsBadWords(String text) {
        try {
            String encodedText = java.net.URLEncoder.encode(text, "UTF-8");
            String url = "https://www.purgomalum.com/service/containsprofanity?text=" + encodedText;

            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) new java.net.URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream()));
            String result = in.readLine();
            in.close();

            return Boolean.parseBoolean(result);
        } catch (Exception e) {
            e.printStackTrace();
            return false; // fail-safe: allow if check fails
        }
    }






    public void refreshCards() {
        try {
            postContainer.getChildren().clear();

            String search = searchField.getText() != null ? searchField.getText().toLowerCase() : "";
            String selectedType = typeFilter.getValue();

            List<Post> allPosts = postService.getAllPosts();
            ReactionService reactionService = new ReactionService(MyDataBase.getInstance().getMyConnection());
            int currentUserId = 19;

            List<Post> filteredPosts = allPosts.stream()
                    .filter(p -> p.getContenu().toLowerCase().contains(search))
                    .filter(p -> selectedType == null || selectedType.equals("Tous") || p.getTypePost().equalsIgnoreCase(selectedType))
                    .collect(Collectors.toList());

            // 🔷 Show regular posts
            for (Post post : filteredPosts) {
                VBox card = PostCardFactory.createCard(post, currentUserId, false, this::refreshCards);

                postContainer.getChildren().add(card);
            }

            // ✨ Show recommended posts
            if (!recommendedPosts.isEmpty()) {
                Label recLabel = new Label("✨ Posts recommandés pour vous :");
                recLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #3F51B5;");
                postContainer.getChildren().add(recLabel);

                for (Post recPost : recommendedPosts) {
                    if (filteredPosts.stream().noneMatch(p -> p.getIdPost() == recPost.getIdPost())) {
                        VBox recCard = createCardForPost(recPost, currentUserId, true);
                        postContainer.getChildren().add(recCard);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

 VBox createCardForPost(Post post, int currentUserId, boolean isRecommended) {
        VBox card = new VBox(15);
        card.setAlignment(Pos.CENTER);
        card.setStyle(
                (isRecommended
                        ? "-fx-background-color: #E3F2FD; -fx-border-color: #42A5F5;"
                        : "-fx-background-color: white; -fx-border-color: #ddd;") +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 20;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 3);"
        );
        card.setPrefWidth(600);

        // Optional label on top if recommended
        if (isRecommended) {
            Label badge = new Label("✨ Recommandé");
            badge.setStyle("-fx-background-color: #42A5F5; -fx-text-fill: white; -fx-padding: 5 10; -fx-background-radius: 5;");
            card.getChildren().add(badge);
        }

        // Image
        if (post.getMediaUrl() != null && !post.getMediaUrl().isEmpty()) {
            File imageFile = new File(post.getMediaUrl());
            if (imageFile.exists()) {
                try {
                    Image image = new Image(imageFile.toURI().toString(), 400, 250, true, true);
                    ImageView imageView = new ImageView(image);
                    HBox imageBox = new HBox(imageView);
                    imageBox.setAlignment(Pos.CENTER);
                    imageBox.setStyle("-fx-padding: 10 0 10 0;");
                    card.getChildren().add(imageBox);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // QR Code
        String qrContent = "Type: " + post.getTypePost() +
                "\nContenu: " + post.getContenu() +
                "\nDate: " + post.getDatePublication().toLocalDate();
        Image qrImage = QRCodeGenerator.generateQRCodeImage(qrContent, 100, 100);
        if (qrImage != null) {
            ImageView qrImageView = new ImageView(qrImage);
            qrImageView.setFitHeight(100);
            qrImageView.setFitWidth(100);
            HBox qrBox = new HBox(qrImageView);
            qrBox.setAlignment(Pos.CENTER);
            qrBox.setStyle("-fx-padding: 10 0 10 0;");
            card.getChildren().add(qrBox);
        }

        try {
            ReactionService reactionService = new ReactionService(MyDataBase.getInstance().getMyConnection());

            int likeCount = reactionService.countReactions(post.getIdPost(), "like");
            int dislikeCount = reactionService.countReactions(post.getIdPost(), "dislike");

            Label likeLabel = new Label("👍 " + likeCount);
            likeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: green; -fx-font-weight: bold;");

            Label dislikeLabel = new Label("👎 " + dislikeCount);
            dislikeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: red; -fx-font-weight: bold;");

            Button likeButton = new Button("👍 J’aime");
            likeButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
            likeButton.setOnAction(e -> {
                try {
                    reactionService.react(post.getIdPost(), currentUserId, "like");

                    // 💡 Generate Recommendations
                    recommendedPosts = postService.getAllPosts().stream()
                            .filter(p -> p.getIdPost() != post.getIdPost())
                            .filter(p -> p.getTypePost().equalsIgnoreCase(post.getTypePost()))
                            .collect(Collectors.toList());

                    refreshCards();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            Button dislikeButton = new Button("👎 Je n’aime pas");
            dislikeButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");
            dislikeButton.setOnAction(e -> {
                try {
                    reactionService.react(post.getIdPost(), currentUserId, "dislike");
                    refreshCards();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            HBox reactionsBox = new HBox(15, likeLabel, dislikeLabel, likeButton, dislikeButton);
            reactionsBox.setAlignment(Pos.CENTER);
            reactionsBox.setStyle("-fx-padding: 10 0 10 0;");
            card.getChildren().add(reactionsBox);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Label typeLabel = new Label("Type: " + post.getTypePost());
        typeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label contenuLabel = new Label("Contenu: " + post.getContenu());
        contenuLabel.setWrapText(true);
        contenuLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #444;");

        Label dateLabel = new Label("📅 " + post.getDatePublication().toLocalDate());
        Label trancheLabel = new Label("👤 Tranche d'âge: " + post.getTrancheDage());

        VBox infoBox = new VBox(5, typeLabel, contenuLabel, dateLabel, trancheLabel);
        infoBox.setAlignment(Pos.CENTER);
        card.getChildren().add(infoBox);

        // Comments
        VBox commentSection = new VBox(5);
        commentSection.setAlignment(Pos.CENTER_LEFT);
        Label commentTitle = new Label("Commentaires:");
        commentTitle.setStyle("-fx-font-style: italic;");
        commentSection.getChildren().add(commentTitle);

        try {
            List<Commentaire> commentaires = postService.getCommentairesByPostId(post.getIdPost());
            for (Commentaire com : commentaires) {
                HBox commentRow = new HBox(10);
                commentRow.setAlignment(Pos.CENTER);

                Label commentLabel = new Label("- " + com.getContenu());
                commentLabel.setWrapText(true);
                commentLabel.setMaxWidth(400);

                Button editComment = new Button("✏");
                editComment.setOnAction(e -> {
                    TextInputDialog dialog = new TextInputDialog(com.getContenu());
                    dialog.setTitle("Modifier le commentaire");
                    dialog.setHeaderText(null);
                    dialog.setContentText("Nouveau contenu:");
                    dialog.showAndWait().ifPresent(newContent -> {
                        if (!newContent.trim().isEmpty()) {
                            try {
                                com.setContenu(newContent);
                                com.setDateCommentaire(LocalDateTime.now());
                                new CommentaireService(MyDataBase.getInstance().getMyConnection()).updateCommentaire(com);
                                refreshCards();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                });

                Button deleteComment = new Button("🗑");
                deleteComment.setOnAction(e -> {
                    try {
                        new CommentaireService(MyDataBase.getInstance().getMyConnection()).deleteCommentaire(com.getId());
                        refreshCards();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                commentRow.getChildren().addAll(commentLabel, editComment, deleteComment);
                commentSection.getChildren().add(commentRow);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Comment button
        Button commentBtn = new Button("💬 Ajouter un commentaire");
        commentBtn.setStyle("-fx-background-color: #00897B; -fx-text-fill: white; -fx-font-weight: bold;");
        commentBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Ajouter un commentaire");
            dialog.setHeaderText(null);
            dialog.setContentText("Commentaire:");
            dialog.showAndWait().ifPresent(commentText -> {
                if (!commentText.trim().isEmpty()) {
                    if (containsBadWords(commentText)) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Commentaire inapproprié");
                        alert.setHeaderText(null);
                        alert.setContentText("⚠️ Votre commentaire contient des mots inappropriés !");
                        alert.showAndWait();
                        return;
                    }

                    try {
                        Commentaire newComment = new Commentaire(
                                0,
                                post.getIdPost(),
                                currentUserId,
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

        card.getChildren().addAll(new HBox(commentBtn), commentSection);
        return card;
    }





    @FXML
    public void addPost() {
        try {
            if (typePostComboBox.getValue() == null ||
                    contenuField.getText().isEmpty() ||
                    mediaUrlField.getText().isEmpty() ||
                    datePublicationPicker.getValue() == null ||
                    trancheDageField.getText().isEmpty() ||
                    nmbLikeField.getText().isEmpty()) {
                showError("Tous les champs doivent être remplis.");
                return;
            }

            int likes;
            try {
                likes = Integer.parseInt(nmbLikeField.getText());
            } catch (NumberFormatException e) {
                showError("Le nombre de likes doit être un entier.");
                return;
            }

            Post post = new Post(
                    0,
                    19, // static user ID
                    typePostComboBox.getValue(), // 🔥 use ComboBox
                    contenuField.getText(),
                    mediaUrlField.getText(),
                    datePublicationPicker.getValue().atStartOfDay(),
                    trancheDageField.getText(),
                    likes
            );

            postService.addPost(post);
            refreshCards();
            clearFields(); // ✅ Reset form after adding
            cancelEditPost(); // ✅ Hide form after adding
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur lors de l'ajout du post !");
        }
    }



    private void clearFields() {
        typePostComboBox.getSelectionModel().clearSelection();
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




  

    @FXML
    private void goToRecommendedPosts() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/RecommendedView.fxml"));
            Stage stage = (Stage) searchField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
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
