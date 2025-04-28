package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Post;
import services.PostService;
import services.ReactionService;
import utils.MyDb;
import utils.PostCardFactory;
import java.util.List;

public class RecommendedPostsController {

    @FXML
    private VBox recommendedContainer;

    @FXML
    private Button backToAllBtn;

    private PostService postService;
    private ReactionService reactionService;

    public void initialize() {
        postService = new PostService(MyDb.getInstance().getConnection());
        reactionService = new ReactionService(MyDb.getInstance().getConnection());

        backToAllBtn.setOnAction(e -> switchToMainView());

        displayRecommended();
    }

    private void displayRecommended() {
        try {
            int currentUserId = 1;

            reactionService = new ReactionService(MyDb.getInstance().getConnection());
            postService = new PostService(MyDb.getInstance().getConnection());

            // Get post IDs the user liked
            List<Integer> likedPostIds = reactionService.getLikedPostIdsByUser(currentUserId);

            // Get full Post objects (handling SQLException in lambda)
            List<Post> likedPosts = likedPostIds.stream()
                    .map(id -> {
                        try {
                            return postService.getPostById(id);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(p -> p != null)
                    .toList();

            List<String> likedTypes = likedPosts.stream()
                    .map(Post::getTypePost)
                    .distinct()
                    .toList();

            // Find other posts with same types that are not already liked
            List<Post> allPosts = postService.getAllPosts();
            List<Post> recommendations = allPosts.stream()
                    .filter(p -> likedTypes.contains(p.getTypePost()))
                    .filter(p -> !likedPostIds.contains(p.getIdPost()))
                    .toList();

            recommendedContainer.getChildren().clear();

            if (recommendations.isEmpty()) {
                Label emptyLabel = new Label("Aucune recommandation pour l'instant.");
                emptyLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: red;");
                recommendedContainer.getChildren().add(emptyLabel);
                return;
            }

            for (Post post : recommendations) {
                VBox card = PostCardFactory.createCard(post, currentUserId, true, this::displayRecommended);
                recommendedContainer.getChildren().add(card);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void switchToMainView() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Post.fxml"));
            Stage stage = (Stage) backToAllBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
