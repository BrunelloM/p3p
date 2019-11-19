package com.emailserver.core;

import com.emailserver.beans.Email;
import com.emailserver.beans.User;
import com.emailserver.io.FilesManager;
import com.emailserver.network.Request;
import com.emailserver.network.RequestType;
import com.emailserver.network.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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

            if(UsersList.contains(incomingRequest.getIdentity())) {             // Test if the incoming request comes from a valid user
                switch (incomingRequest.getType()) {                            // Get the request type
                    case SEND:
                        Email toSend = incomingRequest.getEmailParam();
                        if(checkRecipients(toSend)) {
                            System.out.println(toSend.getSender().getAddress());
                            System.out.println(toSend.getReceivers()[0].getAddress());
                            FilesManager.saveEmail(toSend);
                            response.setCompletedState();;
                        } else {
                            response.setErrorState("Unable to reach some recipients: Wrong mail address");
                        }
                    break;

                    case DELETE:
                    break;

                    case INBOX:
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
            }
            outputStream.writeObject(response);
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
        for(User usr : email.getReceivers()) {
            isValid = isValid && UsersList.contains(usr);
        }
        return isValid;
    }

}
