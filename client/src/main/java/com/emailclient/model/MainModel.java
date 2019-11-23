package com.emailclient.model;

import com.emailclient.utils.ApplicationContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import shared.Email;
import shared.Request;
import shared.RequestType;

import java.util.Date;
import java.util.Objects;


public class MainModel {

    private ObservableList<Email> inboxModel;
    private ObservableList<Email> sentModel;
    private ObservableList<Email> specialModel;
    private ObservableList<Email> trashModel;

    public MainModel() {
        init();
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
        inboxModel = FXCollections.observableArrayList(new Email(0, "Inbox", new String[]{"Hi!"}, new Date(), "We are in inbox", "Inboxed email!"));
        sentModel = FXCollections.observableArrayList(new Email(0, "Sent", new String[]{"Hi!"}, new Date(), "We are in sent", "Sent email!"));
        specialModel = FXCollections.observableArrayList(new Email(0, "Special", new String[]{"Hi!"}, new Date(), "We are in special", "Special email!"));
        trashModel = FXCollections.observableArrayList(new Email(0, "Trash", new String[]{"Hi!"}, new Date(), "We are in trash", "Trashed email!"));
    }


    public void refreshInbox() {
        Request request = new Request(ApplicationContext.getIdentity().getAddress(), RequestType.INBOX, null);
        RequestThread requestThread = new RequestThread(request, this);
        requestThread.start();
    }

    public void refreshSent() {
        Request request = new Request(ApplicationContext.getIdentity().getAddress(), RequestType.SENT, null);
        RequestThread requestThread = new RequestThread(request, this);
        requestThread.start();
    }

    public void refreshTrash() {
        Request request = new Request(ApplicationContext.getIdentity().getAddress(), RequestType.TRASH, null);
        RequestThread requestThread = new RequestThread(request, this);
        requestThread.start();
    }

    public void refreshSpecials() {
        Request request = new Request(ApplicationContext.getIdentity().getAddress(), RequestType.SPECIALS, null);
        RequestThread requestThread = new RequestThread(request, this);
        requestThread.start();
    }

    public ObservableList<Email> getInboxModel() {
        return inboxModel;
    }

    public ObservableList<Email> getSentModel() {
        return sentModel;
    }

    public ObservableList<Email> getSpecialModel() {
        return specialModel;
    }

    public ObservableList<Email> getTrashModel() {
        return trashModel;
    }
}
