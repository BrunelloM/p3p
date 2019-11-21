package com.emailserver;

import com.emailserver.beans.Email;
import com.emailserver.network.Request;
import com.emailserver.network.RequestType;
import com.emailserver.network.Response;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class SendRequestTest {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Socket connection = new Socket(InetAddress.getLocalHost().getHostName(), 1055);
        ObjectOutputStream ooStream = new ObjectOutputStream(connection.getOutputStream());
        ObjectInputStream oiStream = new ObjectInputStream(connection.getInputStream());
        Request req = provideRequest();

        ooStream.reset();
        ooStream.writeObject(req);
        Response response = (Response) oiStream.readObject();
        System.out.println(response.getType().toString());
    }


    private static Request provideRequest() {
        Email email = new Email("piermenti.sfracellozzi@mail.com", new String[] {"ugo_sbrinzi@sbrinzer.com"}, "Subject", "Text of the message");

        return new Request(
                "piermenti.sfracellozzi@mail.com",
                RequestType.SEND,
                email
        );
    }

}
