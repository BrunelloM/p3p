package com.emailclient.model;

import com.emailclient.utils.ApplicationContext;
import java.util.List;
import shared.Email;
import shared.Request;
import shared.Response;
import shared.ResponseType;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

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
            List<Email> emailList = new LinkedList<>(); // TODO: To remove
            switch(request.getType()) {
                // TODO: This piece of code needs a giant refactor (But it works anyway :P)
                case INBOX:
                    response = (Response) oiStream.readObject();
                    while(response.getType() == ResponseType.NEXT) {
                        emailList.add(response.getPayload());
                        response = (Response) oiStream.readObject();
                    }
                    model.addAllInbox(emailList);
                break;

                case SENT:
                    response = (Response) oiStream.readObject();
                    while(response.getType() == ResponseType.NEXT) {
                        emailList.add(response.getPayload());
                        response = (Response) oiStream.readObject();
                    }
                    model.addAllSent(emailList);
                break;

                case SPECIALS:
                    response = (Response) oiStream.readObject();
                    while(response.getType() == ResponseType.NEXT) {
                        emailList.add(response.getPayload());
                        response = (Response) oiStream.readObject();
                    }
                    model.addAllSpecials(emailList);
                break;

                case TRASH:
                    response = (Response) oiStream.readObject();
                    while(response.getType() == ResponseType.NEXT) {
                        emailList.add(response.getPayload());
                        response = (Response) oiStream.readObject();
                    }
                    model.addAllTrash(emailList);
                break;

                case DEL_SENT:
                case DEL_SPECIAL:
                case DEL_INBOX:
                    response = (Response) oiStream.readObject();
                    if(response.getType() == ResponseType.ERROR)
                        throw new IOException(response.getMessage());
                    break;

            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
