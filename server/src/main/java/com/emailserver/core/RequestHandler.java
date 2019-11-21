package com.emailserver.core;

import com.emailserver.model.Messages;
import com.emailserver.model.MessagesList;
import com.emailserver.network.Protocol;
import com.emailserver.network.Request;
import com.emailserver.network.Response;
import java.util.List;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


/**
 *
 * @author Matteo Brunello
 */
public class RequestHandler implements Runnable {

    private Socket clientConnection;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Protocol protocol;
    private MessagesList model;

    RequestHandler(Socket clientConnection, MessagesList model ) {
        this.clientConnection = clientConnection;
        this.model = model;
        this.protocol = new Protocol(model);
        try {
            this.outputStream = new ObjectOutputStream(clientConnection.getOutputStream());
            this.inputStream = new ObjectInputStream(clientConnection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            Request incomingRequest = (Request) inputStream.readObject();
            Response response;
            // Handle every method of the request type. Each response is processed by the class that implements the Protocol
            switch(incomingRequest.getType()) {

                case SEND:
                    response = protocol.handleSendRequest(incomingRequest);
                    outputStream.writeObject(response);
                    break;

                case STAR:
                    // TODO: To implement
                    break;

                case DEL_INBOX:
                case DEL_SENT:
                case DEL_SPECIAL:
                    response = protocol.handleDeleteRequest(incomingRequest);
                    outputStream.writeObject(response);
                    break;

                case INBOX:
                case SENT:
                case SPECIALS:
                case TRASH:
                    List<Response> respList = protocol.handleGetRequest(incomingRequest);
                    for(Response resp : respList) {
                        outputStream.writeObject(resp);
                        outputStream.reset();
                    }
                    break;
            }

            outputStream.close();
            inputStream.close();
            clientConnection.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


}
