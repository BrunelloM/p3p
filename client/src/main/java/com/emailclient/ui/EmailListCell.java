package com.emailclient.ui;

import com.emailclient.beans.Email;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.io.IOException;

public class EmailListCell extends ListCell<Email> {

    @FXML private Label nameLabel;
    @FXML private Label dateLabel;
    @FXML private Label subjectLabel;
    @FXML private Label descriptionLabel;

    public EmailListCell() {
        loadFXML();
    }

    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../email_cell.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateItem(Email item, boolean empty) {
        super.updateItem(item, empty);

        if(empty) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
        else {
            nameLabel.setText(item.getName());
            subjectLabel.setText(item.getSubject());
            descriptionLabel.setText(item.getContent());
            dateLabel.setText(item.getDate());

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }


    public static class EmailListCellFactory implements Callback<ListView<Email>, ListCell<Email>> {
        @Override
        public ListCell<Email> call(ListView<Email> param) {
            return new EmailListCell();
        }
    }

}