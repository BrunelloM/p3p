package com.emailserver;

import com.emailserver.beans.Email;
import com.emailserver.network.Request;
import com.emailserver.network.RequestType;
import com.emailserver.network.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SendRequestTest {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Socket connection = new Socket(InetAddress.getLocalHost().getHostName(), 1055);
        ObjectInputStream oiStream = new ObjectInputStream(connection.getInputStream());
        ObjectOutputStream ooStream = new ObjectOutputStream(connection.getOutputStream());

        ooStream.writeObject(provideRequest());

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
