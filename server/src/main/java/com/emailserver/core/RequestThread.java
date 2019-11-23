package com.emailserver.core;

import com.emailserver.model.Messages;
import com.emailserver.model.MessagesList;
import shared.Request;
import shared.Response;

import java.util.List;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


/**
 * This class represents a thread that will be started on every client request.
 * When it starts it fetch the request message and then will switch between request types.
 * @author Matteo Brunello
 */
public class RequestThread implements Runnable {

    private Socket clientConnection;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private RequestHandler requestHandler;
    private MessagesList model;

    /**
     * @param clientConnection The connection that has been accepted by the ServerDaemon
     * @param model Model class of Model View Controller pattern
     */
    RequestThread(Socket clientConnection, MessagesList model) {
        this.clientConnection = clientConnection;
        this.model = model;
        this.requestHandler = new RequestHandler(model);
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
                    response = requestHandler.handleSendRequest(incomingRequest);
                    outputStream.writeObject(response);
                    break;

                case STAR:
                    response = requestHandler.handleStarRequest(incomingRequest);
                    outputStream.writeObject(response);
                    break;

                case DEL_INBOX:
                case DEL_SENT:
                case DEL_SPECIAL:
                    response = requestHandler.handleDeleteRequest(incomingRequest);
                    outputStream.writeObject(response);
                    break;

                case INBOX:
                case SENT:
                case SPECIALS:
                case TRASH:
                    List<Response> respList = requestHandler.handleGetRequest(incomingRequest);
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
            model.addMessage(String.format(Messages.ERROR_MSG, e.getMessage()));
            throw new RuntimeException(e);
        }
    }


}
