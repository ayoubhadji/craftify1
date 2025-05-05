package utils;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import entity.Commentaire;
import entity.Post;
import services.blog.CommentaireService;
import services.blog.PostService;
import services.blog.ReactionService;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

import static controllers.blog.PostController.openEditPostWindowStatic;

public class PostCardFactory {

    public static VBox createCard(Post post, int currentUserId, boolean isRecommended, Runnable onRefresh) {
        VBox card = new VBox(15);
        card.setAlignment(Pos.CENTER);
        card.setStyle((isRecommended
                ? "-fx-background-color: #E3F2FD; -fx-border-color: #42A5F5;"
                : "-fx-background-color: white; -fx-border-color: #ddd;")
                + "-fx-border-radius: 10;"
                + "-fx-background-radius: 10;"
                + "-fx-padding: 20;"
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 3);"
        );
        card.setPrefWidth(600);

        if (isRecommended) {
            Label badge = new Label("✨ Recommandé");
            badge.setStyle("-fx-background-color: #42A5F5; -fx-text-fill: white; -fx-padding: 5 10; -fx-background-radius: 5;");
            card.getChildren().add(badge);
        }

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
                    reactionService.toggleReaction(post.getIdPost(), currentUserId, "like");
                    onRefresh.run();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            Button dislikeButton = new Button("👎 Je n’aime pas");
            dislikeButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");
            dislikeButton.setOnAction(e -> {
                try {
                    reactionService.toggleReaction(post.getIdPost(), currentUserId, "dislike");
                    onRefresh.run();
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

        // 🛠️ Edit and Delete buttons for Post
        Button editBtn = new Button("✏️ Modifier");
        editBtn.setStyle("-fx-background-color: #1976D2; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6;");
        editBtn.setOnAction(e -> openEditPostWindowStatic(post, onRefresh));


        Button deleteBtn = new Button("🗑️ Supprimer");
        deleteBtn.setStyle("-fx-background-color: #B71C1C; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6;");
        deleteBtn.setOnAction(e -> {
            try {
                new PostService(MyDataBase.getInstance().getMyConnection()).deletePost(post.getIdPost());
                onRefresh.run();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        HBox actionsBox = new HBox(10, editBtn, deleteBtn);
        actionsBox.setAlignment(Pos.CENTER);
        card.getChildren().add(actionsBox);

        // Comment Section
        VBox commentSection = new VBox(5);
        commentSection.setAlignment(Pos.CENTER);

        Label commentTitle = new Label("Commentaires:");
        commentTitle.setStyle("-fx-font-style: italic; -fx-font-size: 15px;");
        HBox titleBox = new HBox(commentTitle);
        titleBox.setAlignment(Pos.CENTER);
        commentSection.getChildren().add(titleBox);

        try {
            List<Commentaire> commentaires = new PostService(MyDataBase.getInstance().getMyConnection()).getCommentairesByPostId(post.getIdPost());
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
                                onRefresh.run();
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
                        onRefresh.run();
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

        Button commentBtn = new Button("💬 Ajouter un commentaire");
        commentBtn.setStyle("-fx-background-color: #00897B; -fx-text-fill: white; -fx-font-weight: bold;");
        commentBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Ajouter un commentaire");
            dialog.setHeaderText(null);
            dialog.setContentText("Commentaire:");
            dialog.showAndWait().ifPresent(commentText -> {
                if (!commentText.trim().isEmpty()) {
                    if (ProfanityFilter.containsBadWords(commentText)) {
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
                        new PostService(MyDataBase.getInstance().getMyConnection()).addComment(newComment);
                        onRefresh.run();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        });

        HBox commentBtnBox = new HBox(commentBtn);
        commentBtnBox.setAlignment(Pos.CENTER);

        VBox commentSectionWrapper = new VBox(10, commentBtnBox, commentSection);
        commentSectionWrapper.setAlignment(Pos.CENTER);

        card.getChildren().add(commentSectionWrapper);

        return card;
    }
}
