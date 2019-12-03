package controllers;

import fx.MainApplication;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import shared.Email;

import java.net.URL;
import java.util.ResourceBundle;

public class ShowMailController implements Initializable {

    private Email toShow;

    @FXML private Label subjectHolder;
    @FXML private Label recipientsHolder;
    @FXML private Label textHolder;

    @Override
    public void initialize(URL location, ResourceBundle resources) { }

    public void show(Email toShow) {
        this.toShow = toShow;

        StringBuilder recipientsString = new StringBuilder();
        String[] recipients = toShow.getRecipients();
        for(int i = 0; i < recipients.length - 1; i++) {
            recipientsString.append(recipients[i]);
            recipientsString.append(", ");
        }
        recipientsString.append(recipients[recipients.length - 1]);

        recipientsHolder.setText(recipientsString.toString());
        textHolder.setText(toShow.getText());
        subjectHolder.setText(toShow.getSubject());
    }

    @FXML
    private void onReplyButtonClick() {
        MainApplication.switchToCompose(toShow);
        clearState();
    }

    @FXML
    private void onBackButtonClick() {
        MainApplication.switchToMain();
        clearState();
    }

    private void clearState() {
        this.subjectHolder.setText(null);
        this.recipientsHolder.setText(null);
        this.textHolder.setText(null);
    }
}
