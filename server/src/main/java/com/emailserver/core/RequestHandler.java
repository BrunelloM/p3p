package com.emailserver.core;

import com.emailserver.beans.Email;
import com.emailserver.beans.User;
import com.emailserver.io.FilesManager;
import com.emailserver.network.Request;
import com.emailserver.network.Response;

import java.util.Iterator;
import java.util.List;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Optional;

public class RequestHandler implements Runnable {

    private Socket clientConnection;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public RequestHandler(Socket clientConnection) {
        try {
            this.clientConnection = clientConnection;
            this.inputStream = new ObjectInputStream(clientConnection.getInputStream());
            this.outputStream = new ObjectOutputStream(clientConnection.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            Request incomingRequest = (Request) inputStream.readObject();
            Response response = new Response();

            if(UsersTable.contains(incomingRequest.getIdentity())) {             // Test if the incoming request comes from a valid user
                switch (incomingRequest.getType()) {                             // Get the request type
                    case SEND: {
                        Email toSend = incomingRequest.getEmailParam();
                        if(checkRecipients(toSend)) {
                            FilesManager.saveEmail(toSend);
                            response.setCompletedState();;
                        } else {
                            response.setErrorState("Unable to reach some recipients: Wrong mail address");
                        }
                        outputStream.writeObject(response);
                    }
                    break;

                    case DEL_INBOX: {
                        Email toDelete = incomingRequest.getEmailParam();
                        Optional<User> user = UsersTable.get(toDelete.getSender());
                        if(user.isPresent()) {
                            FilesManager.trashInboxEmail(user.get(), toDelete);
                            response.setCompletedState();
                        } else {
                            response.setErrorState("Unable to get the given user");
                        }
                        outputStream.writeObject(response);
                    }
                    break;

                    case INBOX: {
                        Optional<User> user = UsersTable.get(incomingRequest.getIdentity());
                        if(user.isPresent()) {
                            List<Email> emailList = FilesManager.getInbox(user.get());
                            for(Email email : emailList) {
                                response.setNextState();;
                                response.setPayload(email);
                                outputStream.writeObject(response);
                                outputStream.reset();
                            }
                            response.setCompletedState();
                            outputStream.writeObject(response);
                        } else {
                            response.setErrorState("Unable to get the given user");
                            outputStream.writeObject(response);
                        }
                    }
                    break;

                    case SENT:
                    break;

                    case STAR:
                    break;

                    case TRASH:
                    break;

                    case SPECIALS:
                    break;
                }
            } else {
                response.setErrorState("User not registered");
            }

            outputStream.close();
            inputStream.close();
            clientConnection.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private boolean checkRecipients(Email email) {
        assert email != null;
        boolean isValid = true;
        for(String recipient : email.getRecipients()) {
            isValid = isValid && UsersTable.contains(recipient);
        }
        return isValid;
    }

}
