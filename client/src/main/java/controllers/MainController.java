package controllers;

import fx.MainApplication;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import model.MainModel;
import fx.EmailListCell;
import javafx.scene.control.Label;
import shared.Email;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.io.IOException;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController extends BindableController implements Initializable {

    enum State {
        INBOX,
        SENT,
        SPECIAL,
        TRASH
    }

    @FXML private Button starButton;
    @FXML private Button replyButton;

    @FXML private Label topBarTitle;
    @FXML private ImageView avatarImageHolder;
    @FXML private ListView<Email> emailListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Setup Circle avatar
        Circle circle = new Circle(64);
        circle.setCenterX(64);
        circle.setCenterY(64);
        avatarImageHolder.setClip(circle);

        // Email list view init
        emailListView.setCellFactory(new EmailListCell.EmailListCellFactory());
        emailListView.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2) {
                MainApplication.switchToShowMail(emailListView.getSelectionModel().getSelectedItem());
            }
        });

    }

    @FXML
    private void deleteButtonClick() {
        Email selected = emailListView.getSelectionModel().getSelectedItem();
        model.delete(selected);
    }

    @FXML
    private void replyButtonClick() {
        Email email = emailListView.getSelectionModel().getSelectedItem();
        MainApplication.switchToCompose(email);
    }

    @FXML
    private void composeButtonClick() {
        MainApplication.switchToCompose();
    }

    @FXML
    private void starButtonClick() {
        Email email = emailListView.getSelectionModel().getSelectedItem();
        model.star(email);
    }

    /** +========+ SIDE NAVIGATION BAR BUTTONS HANDLERS +========+ **/
    @FXML
    private void inboxButtonClick() {
        setState(State.INBOX);
    }

    @FXML
    private void sentButtonClick() {
        setState(State.SENT);
    }

    @FXML
    private void specialButtonClick() {
        setState(State.SPECIAL);
    }

    @FXML
    private void trashButtonClick() {
        setState(State.TRASH);
    }

    @Override
    public void bindModel(MainModel model) {
        super.bindModel(model);
        model.bind(emailListView);              // Bind the ListView widget to the model
    }

    private void setState(State state) {
        switch (state) {
            case INBOX:
                model.filter(Email.Type.RECEIVED);
                replyButton.setVisible(true);
                starButton.setVisible(true);
                topBarTitle.setText("Inbox");
            break;

            case SENT:
                model.filter(Email.Type.SENT);
                replyButton.setVisible(false);
                starButton.setVisible(false);
                topBarTitle.setText("Sent");
            break;

            case SPECIAL:
                model.filter(Email.Type.SPECIAL);
                replyButton.setVisible(true);
                starButton.setVisible(false);
                topBarTitle.setText("Special");
            break;

            case TRASH:
                replyButton.setVisible(false);
                starButton.setVisible(false);
                model.filter(Email.Type.TRASH);
                topBarTitle.setText("Trash");
            break;

            default:
                throw new IllegalArgumentException();
        }
    }

}

