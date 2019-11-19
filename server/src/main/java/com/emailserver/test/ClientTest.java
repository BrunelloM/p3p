package com.emailserver.test;

import com.emailserver.beans.Email;
import com.emailserver.network.Request;
import com.emailserver.beans.User;
import com.emailserver.network.RequestType;
import com.emailserver.network.Response;
import com.emailserver.network.ResponseType;

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
        Request req = new Request("ugo_sbrinzi@sbrinzer.com", RequestType.INBOX);
        Request writeReq = new Request(user.getAddress(), RequestType.SEND, new Email(
                user.getAddress(),
                new String[] {user2.getAddress()},
                "Oggetto",
                "testo"
        ));

        Socket conn = new Socket(localHost.getHostName(),10956);

        ObjectOutputStream ooStream = new ObjectOutputStream(conn.getOutputStream());
        ObjectInputStream oiStream = new ObjectInputStream(conn.getInputStream());

    /**
        ooStream.writeObject(writeReq);
        Response resp = (Response) oiStream.readObject();
        if(resp.getType() == ResponseType.DONE) {
            System.out.println("SENT!");
        } else {
            System.out.println("Not sent! " + resp.getMessage());
        }
    **/
        ooStream.writeObject(req);

        Response response = (Response) oiStream.readObject();
        while(response.getType() == ResponseType.NEXT) {
            System.out.println("Email: " + response.getPayload().getId());
            response = (Response) oiStream.readObject();
        }

        ooStream.close();
        oiStream.close();
    }




}
