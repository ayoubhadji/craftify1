package controllers.blog;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import entity.Commentaire;
import services.blog.CommentaireService;
import utils.MyDataBase;

import java.util.List;

public class CommentaireController {
    @FXML private TableView<Commentaire> commentaireTable;
    @FXML private TextField postIdField, userIdField, contenuField, nmbLikeField;
    @FXML private DatePicker dateCommentairePicker;

    private CommentaireService commentaireService;

    public void initialize() {
        this.commentaireService = new CommentaireService(MyDataBase.getInstance().getMyConnection());
        refreshTable();
    }

    public void refreshTable() {
        try {
            List<Commentaire> commentaires = commentaireService.getAllCommentaires();
            commentaireTable.setItems(FXCollections.observableArrayList(commentaires));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addCommentaire() {
        try {
            Commentaire c = new Commentaire(0,
                    Integer.parseInt(postIdField.getText()),
                    Integer.parseInt(userIdField.getText()),
                    contenuField.getText(),
                    dateCommentairePicker.getValue().atStartOfDay(),
                    Integer.parseInt(nmbLikeField.getText()));
            commentaireService.addCommentaire(c);
            refreshTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateCommentaire() {
        Commentaire selected = commentaireTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        try {
            selected.setContenu(contenuField.getText());
            selected.setDateCommentaire(dateCommentairePicker.getValue().atStartOfDay());
            selected.setNmbLike(Integer.parseInt(nmbLikeField.getText()));
            commentaireService.updateCommentaire(selected);
            refreshTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteCommentaire() {
        Commentaire selected = commentaireTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                commentaireService.deleteCommentaire(selected.getId());
                refreshTable();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onRowClick(MouseEvent event) {
        Commentaire selected = commentaireTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            postIdField.setText(String.valueOf(selected.getPostId()));
            userIdField.setText(String.valueOf(selected.getUserId()));
            contenuField.setText(selected.getContenu());
            nmbLikeField.setText(String.valueOf(selected.getNmbLike()));
            dateCommentairePicker.setValue(selected.getDateCommentaire().toLocalDate());
        }
    }
}
