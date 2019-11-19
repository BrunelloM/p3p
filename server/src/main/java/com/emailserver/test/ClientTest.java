package com.emailserver.test;

import com.emailserver.beans.Email;
import com.emailserver.network.Request;
import com.emailserver.beans.User;
import com.emailserver.network.RequestType;
import com.emailserver.network.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ClientTest {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        InetAddress localHost = InetAddress.getLocalHost();

        User user = new User(0, "Matteo", "Brunello", "matteo.brunello@gmail.com");
        User user2 = new User("ugo_sbrinzi@sbrinzer.com");
        Request req = new Request(user, RequestType.SEND, new Email(user, new User[]{user2}, "Ciao", "Ciaonee"));

        Socket conn = new Socket(localHost.getHostName(),10956);
        ObjectOutputStream ooStream = new ObjectOutputStream(conn.getOutputStream());
        ObjectInputStream oiStream = new ObjectInputStream(conn.getInputStream());

        ooStream.writeObject(req);
        Response resp = (Response) oiStream.readObject();

        System.out.println("Received response: ");
        System.out.println(resp);

        ooStream.close();
        oiStream.close();
    }

}
