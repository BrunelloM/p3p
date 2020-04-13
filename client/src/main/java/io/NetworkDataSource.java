package io;

import shared.Email;
import shared.User;
import shared.protocol.Request;
import shared.protocol.Response;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkDataSource implements DataSource {

    private ExecutorService threadPool;         // Holds the workers for the requests
    private User clientIdentity;
    private String serverAddress;
    private int serverPort;

    public NetworkDataSource() {
        int maxThreads = ConfigReader.getThreadsNumber();
        this.threadPool = Executors.newFixedThreadPool(maxThreads);
        this.serverPort = ConfigReader.getPort();
        this.serverAddress = ConfigReader.getAddress();
        this.clientIdentity = ConfigReader.getIdentity();
    }

    private Socket openConnection() throws IOException {
        return new Socket(serverAddress, serverPort);
    }

    /*
     * Creates a new Runnable that sends a specific "binary" request.
     * These type of requests are specific in order to send, delete, and star a specific Email.
     */
    private Runnable sendBinaryRequest(Request request, DataSourceCallback<Response> callback) {
        return () -> {
            try {
                Socket connection = openConnection();
                ObjectOutputStream outputStream = new ObjectOutputStream(connection.getOutputStream());
                ObjectInputStream inputStream = new ObjectInputStream(connection.getInputStream());
                outputStream.writeObject(request);
                Response response = (Response) inputStream.readObject();
                callback.call(response);
                outputStream.close();
                inputStream.close();
                connection.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        };
    }

    @Override
    public synchronized void send(Email email, DataSourceCallback<Response> callback) {
        Request request = new Request(clientIdentity.getAddress(), Request.Type.SEND, email);
        threadPool.execute(sendBinaryRequest(request, callback));
    }

    @Override
    public synchronized void delete(Email email, DataSourceCallback<Response> callback) {
        Request request = new Request(clientIdentity.getAddress(), Request.Type.DELETE, email);
        threadPool.execute(sendBinaryRequest(request, callback));
    }

    @Override
    public synchronized void star(Email email, DataSourceCallback<Response> callback) {
        Request request = new Request(clientIdentity.getAddress(), Request.Type.STAR, email);
        threadPool.execute(sendBinaryRequest(request, callback));
    }

    @Override
    public synchronized void get(DataSourceCallback<List<Email>> callback) {
        Runnable worker = () -> {
            try {
                Request request = new Request(clientIdentity.getAddress(), Request.Type.GET, null);
                Socket connection = openConnection();
                ObjectOutputStream outputStream = new ObjectOutputStream(connection.getOutputStream());
                ObjectInputStream inputStream = new ObjectInputStream(connection.getInputStream());
                outputStream.writeObject(request);
                List<Email> returnList = new ArrayList<>();
                Response response = (Response) inputStream.readObject();
                while (response.getType() == Response.Type.NEXT) {
                    returnList.add(response.getPayload());
                    response = (Response) inputStream.readObject();
                }
                callback.call(returnList);
                inputStream.close();
                outputStream.close();
                connection.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        };
        threadPool.execute(worker);
    }

    @Override
    public void tearDown() {
        threadPool.shutdown();
    }

}
