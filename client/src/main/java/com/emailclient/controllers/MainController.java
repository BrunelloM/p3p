package com.emailclient.controllers;

import com.emailclient.model.MainModel;
import com.emailclient.ui.EmailListCell;
import com.emailclient.ui.SideBarButton;
import shared.Email;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.io.IOException;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private BorderPane borderPane;
    @FXML private ImageView avatarImageHolder;
    @FXML private ListView<Email> emailListView;
    @FXML private SideBarButton inboxButton;
    private MainModel model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Setup Circle avatar
        Circle circle = new Circle(64);
        circle.setCenterX(64);
        circle.setCenterY(64);
        avatarImageHolder.setClip(circle);

        model = new MainModel();

        // Email list view init
        emailListView.setCellFactory(new EmailListCell.EmailListCellFactory());

        // Default selected side nav button
        model.bindInboxModel(emailListView);
        model.refreshInbox();
        inboxButton.setSelected(true);
    }

    @FXML
    private void deleteButtonClick() {
        Email selected = emailListView.getSelectionModel().getSelectedItem();
        emailListView.getItems().remove(selected);
    }

    @FXML
    private void replyButtonClick(ActionEvent event) {

    }

    @FXML
    private void replyAllButtonClick() {

    }

    @FXML
    private void composeButtonClick(ActionEvent event) throws IOException {
        Parent root = null;
        root = FXMLLoader.load(getClass().getResource("../compose_email.fxml"));
        ((Node) event.getSource()).getScene().setRoot(root);
    }

    /** +========+ SIDE NAVIGATION BAR BUTTONS HANDLERS +========+ **/
    @FXML
    private void inboxButtonClick() {
        model.bindInboxModel(emailListView);
    }

    @FXML
    private void sentButtonClick() {
        model.bindSentModel(emailListView);
    }

    @FXML
    private void specialButtonClick() {
        model.bindSpecialModel(emailListView);
    }

    @FXML
    private void trashButtonClick() {
        model.bindTrashModel(emailListView);
    }

}

