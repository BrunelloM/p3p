package com.emailclient.model;

import com.emailclient.utils.ApplicationContext;
import shared.Request;
import shared.Response;
import shared.ResponseType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class RequestThread extends Thread {

    private static final int PORT_NO = 1055;

    private Socket connection;
    private MainModel model;
    private Request request;
    private ObjectOutputStream ooStream;
    private ObjectInputStream oiStream;

    public RequestThread(Request request, MainModel model) {
        setDaemon(true);
        this.model = model;
        this.request = request;
        try {
            connection = new Socket(ApplicationContext.getServerAddress(), PORT_NO);
            ooStream = new ObjectOutputStream(connection.getOutputStream());
            oiStream = new ObjectInputStream(connection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            this.interrupt();
        }
    }

    @Override
    public void run() {
        try {
            ooStream.writeObject(request);
            Response response;
            switch(request.getType()) {
                // TODO: THIS PIECE OF CODE NEEDS A REFACTOR
                case INBOX:
                    response = (Response) oiStream.readObject();
                    while(response.getType() == ResponseType.NEXT) {
                        model.getInboxModel().add(response.getPayload());
                        response = (Response) oiStream.readObject();
                    }
                break;

                case SENT:
                    response = (Response) oiStream.readObject();
                    while(response.getType() == ResponseType.NEXT) {
                        model.getSentModel().add(response.getPayload());
                        response = (Response) oiStream.readObject();
                    }
                break;

                case SPECIALS:
                    response = (Response) oiStream.readObject();
                    while(response.getType() == ResponseType.NEXT) {
                        model.getSpecialModel().add(response.getPayload());
                        response = (Response) oiStream.readObject();
                    }
                break;

                case TRASH:
                    response = (Response) oiStream.readObject();
                    while(response.getType() == ResponseType.NEXT) {
                        model.getTrashModel().add(response.getPayload());
                        response = (Response) oiStream.readObject();
                    }
                break;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
