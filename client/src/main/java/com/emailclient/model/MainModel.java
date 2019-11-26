package com.emailclient.model;

import com.emailclient.utils.ApplicationContext;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import shared.Email;
import shared.Request;
import java.util.List;
import shared.RequestType;
import java.util.Objects;

public class MainModel {

    private ObservableList<Email> inboxModel;
    private ObservableList<Email> sentModel;
    private ObservableList<Email> specialModel;
    private ObservableList<Email> trashModel;

    public MainModel() {
        inboxModel = FXCollections.observableArrayList();
        sentModel = FXCollections.observableArrayList();
        specialModel = FXCollections.observableArrayList();
        trashModel = FXCollections.observableArrayList();
    }

    public void bindSentModel(ListView<Email> listView) {
        Objects.requireNonNull(listView);
        refreshSent();
        if(listView.getItems() != sentModel) {
            listView.setItems(sentModel);
        }
    }

    public void bindInboxModel(ListView<Email> listView) {
        refreshInbox();
        Objects.requireNonNull(listView);
        if(listView.getItems() != inboxModel) {
            listView.setItems(inboxModel);
        }
    }

    public void bindSpecialModel(ListView<Email> listView) {
        refreshSpecials();
        Objects.requireNonNull(listView);
        if(listView.getItems() != specialModel) {
            listView.setItems(specialModel);
        }
    }

    public void bindTrashModel(ListView<Email> listView) {
        refreshTrash();
        Objects.requireNonNull(listView);
        if(listView.getItems() != trashModel) {
            listView.setItems(trashModel);
        }
    }

    public void init() {
        // inboxModel = FXCollections.observableArrayList(new Email(0, "Inbox", new String[]{"Hi!"}, new Date(), "We are in inbox", "Inboxed email!"));
        // sentModel = FXCollections.observableArrayList(new Email(0, "Sent", new String[]{"Hi!"}, new Date(), "We are in sent", "Sent email!"));
        // specialModel = FXCollections.observableArrayList(new Email(0, "Special", new String[]{"Hi!"}, new Date(), "We are in special", "Special email!"));
        // trashModel = FXCollections.observableArrayList(new Email(0, "Trash", new String[]{"Hi!"}, new Date(), "We are in trash", "Trashed email!"));
    }


    private void refreshInbox() {
        Request request = new Request(ApplicationContext.getIdentity().getAddress(), RequestType.INBOX, null);
        RequestThread requestThread = new RequestThread(request, this);
        requestThread.start();
    }

    private void refreshSent() {
        Request request = new Request(ApplicationContext.getIdentity().getAddress(), RequestType.SENT, null);
        RequestThread requestThread = new RequestThread(request, this);
        requestThread.start();
    }

    private void refreshTrash() {
        Request request = new Request(ApplicationContext.getIdentity().getAddress(), RequestType.TRASH, null);
        RequestThread requestThread = new RequestThread(request, this);
        requestThread.start();
    }

    private void refreshSpecials() {
        Request request = new Request(ApplicationContext.getIdentity().getAddress(), RequestType.SPECIALS, null);
        RequestThread requestThread = new RequestThread(request, this);
        requestThread.start();
    }

    void addInbox(Email toAdd) {
        Platform.runLater(() -> inboxModel.add(toAdd));
    }

    void addAllInbox(List<Email> toAdd) {
        Platform.runLater(() -> {
            inboxModel.clear();
            inboxModel.addAll(toAdd);
        });
    }

    void addAllSent(List<Email> toAdd) {
        Platform.runLater(() -> {
            sentModel.clear();
            sentModel.addAll(toAdd);
        });
    }

    void addAllSpecials(List<Email> toAdd) {
        Platform.runLater(() -> {
            specialModel.clear();
            specialModel.addAll(toAdd);
        });
    }

    void addAllTrash(List<Email> toAdd) {
        Platform.runLater(() -> {
            trashModel.clear();
            trashModel.addAll(toAdd);
        });
    }

    void addSent(Email toAdd) {
        Platform.runLater(() -> sentModel.add(toAdd));
    }

    void addTrash(Email toAdd) {
        Platform.runLater(() -> trashModel.add(toAdd));
    }

    void addSpecial(Email toAdd) {
        Platform.runLater(() -> specialModel.add(toAdd));
    }

    public void deleteInbox(Email email) {
        Request request = new Request(ApplicationContext.getIdentity().getAddress(), RequestType.DEL_INBOX, email);
        RequestThread requestThread = new RequestThread(request, this);
        requestThread.start();
    }

    public void deleteSpecial(Email email) {
        Request request = new Request(ApplicationContext.getIdentity().getAddress(), RequestType.DEL_SPECIAL, email);
        RequestThread requestThread = new RequestThread(request, this);
        requestThread.start();
    }

    public void deleteSent(Email email) {
        Request request = new Request(ApplicationContext.getIdentity().getAddress(), RequestType.DEL_SENT, email);
        RequestThread requestThread = new RequestThread(request, this);
        requestThread.start();
    }


}
