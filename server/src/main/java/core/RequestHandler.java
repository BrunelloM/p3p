package core;

import shared.User;
import shared.Email;
import java.util.List;
import model.Messages;
import java.net.Socket;
import io.ServerStorage;
import model.MessagesModel;
import java.io.IOException;
import shared.protocol.Request;
import shared.protocol.Response;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * This class will handle a generic request that has been received and has been accepted by the ServerSocket.accept method
 * It implements a Runnable interface since it will be executed concurrently in the ServerDaemon's ThreadPool
 */
public class RequestHandler implements Runnable {

    private Socket connection;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private MessagesModel uiModel;
    private ServerStorage storage;

    /**
     * RequestHandler constructor will try to get the input/output streams from the incoming connection Socket
     * If an error occurs it will add an error message in the model.
     * @param connection Incoming connection created by ServerSocket.accept from the ServerDaemon thread
     * @param uiModel User interface model that holds a messages list
     * @param storage Server storage interface.
     */
    public RequestHandler(Socket connection, MessagesModel uiModel, ServerStorage storage) {
        try {
            this.storage = storage;
            this.uiModel = uiModel;
            this.connection = connection;
            outputStream = new ObjectOutputStream(connection.getOutputStream());
            inputStream = new ObjectInputStream(connection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            uiModel.addMessage(e.getMessage());
        }
    }

    /**
     * Thread method that will be executed concurrently.
     * It gets the request that has been written in the inputStream by the client.
     * Firstly it will check if the identity of the Request is a valid request, then it will
     * handle the request type calling the proper method.
     */
    @Override
    public void run() {
        try {
            Request incomingRequest = (Request) inputStream.readObject();
            uiModel.addMessage(String.format(Messages.RECEIVED_REQUEST, incomingRequest.getType(), incomingRequest.getIdentity()));
            if(checkIdentity(incomingRequest.getIdentity())) {
                switch(incomingRequest.getType()) {
                    case GET:
                        handleGetRequest(incomingRequest);
                    break;

                    case SEND:
                        handleSendRequest(incomingRequest);
                    break;

                    case DELETE:
                        handleDeleteRequest(incomingRequest);
                    break;

                    case STAR:
                        handleStarRequest(incomingRequest);
                    break;
                }
            } else {
                Response response = new Response();
                response.setErrorState(Messages.ERR_IDENTITY_NOT_FOUND);
                outputStream.writeUnshared(response);
            }
            outputStream.close();
            inputStream.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            uiModel.addMessage(String.format(Messages.ERROR_MSG, Messages.ERR_INTERNAL));
        }
    }

    /**
     * Handle a SEND request type.
     * It check if the recipients are valid first, if they are not, it sends an ERROR type response to the client.
     * If they are valid, then it saves the email using the StorageServer interface.
     * @param request Request that has to be handled
     * @throws IOException Can be thrown by the ObjectOutputStream
     */
    private void handleSendRequest(Request request) throws IOException {
        Response response = new Response();
        Email emailParam = request.getEmailParam();
        if(checkRecipients(emailParam)) {
            storage.saveEmail(emailParam);
            response.setCompletedState();
            uiModel.addMessage(String.format(Messages.SERVED_REQUEST, request.getType(), request.getIdentity()));
        } else {
            response.setErrorState(Messages.ERR_RECIPIENT_NOT_FOUND);
            uiModel.addMessage(String.format(Messages.ERROR_MSG, Messages.ERR_RECIPIENT_NOT_FOUND));
        }
        outputStream.writeUnshared(response);
    }

    /**
     * Handle a GET request type.
     * It gets the user's list of emails, then it sends them one by one wrapped in a Response object.
     * Those Responses type are NEXT until there is another one email to read,
     * ERROR if an error occurs, DONE if there aren't emails to read anymore.
     * @param request Request that has to be handled
     * @throws IOException Can be thrown by the ObjectOutputStream
     */
    private void handleGetRequest(Request request) throws IOException {
        User user = UsersTable.get(request.getIdentity());
        List<Email> emailList =  storage.readEmails(user);
        Response lastResponse = new Response();
        for(Email email : emailList) {
            Response response = new Response();
            response.setNextState();
            response.setPayload(email);
            outputStream.writeUnshared(response);
        }
        lastResponse.setCompletedState();
        outputStream.writeUnshared(lastResponse);
        uiModel.addMessage(String.format(Messages.SERVED_REQUEST, request.getType(), request.getIdentity()));
    }

    /**
     * Handle a DELETE request type.
     * If the email has been already signed as TRASH, then it will permanently delete the email.
     * Otherwise it will sign the given Email as TRASH type.
     * @param request Request that has to be handled
     * @throws IOException Can be thrown by the ObjectOutputStream
     */
    private void handleDeleteRequest(Request request) throws IOException {
        User user = UsersTable.get(request.getIdentity());
        Email toDelete = request.getEmailParam();
        Response response = new Response();
        if(toDelete != null) {
            if(toDelete.getType() == Email.Type.TRASH) {
                storage.deleteEmail(user, toDelete);                        // Delete permanently the email if the email has been already trashed
            } else {
                storage.setEmailType(user, toDelete, Email.Type.TRASH);     // Flag the email with a TRASH type
            }
            response.setCompletedState();
            uiModel.addMessage(String.format(Messages.SERVED_REQUEST, request.getType(), request.getIdentity()));
        } else {
            response.setErrorState(Messages.ERR_INTERNAL);
            uiModel.addMessage(String.format(Messages.ERROR_MSG, Messages.ERR_MALFORMED_PARAM));
        }
        outputStream.writeUnshared(response);
    }

    /**
     * Handle a STAR request type.
     * @param request Request that has to be handled
     * @throws IOException Can be thrown by the ObjectOutputStream
     */
    private void handleStarRequest(Request request) throws IOException {
        User user = UsersTable.get(request.getIdentity());
        Email toStar = request.getEmailParam();
        Response response = new Response();
        if(toStar != null) {
            storage.setEmailType(user, toStar, Email.Type.SPECIAL);
            response.setCompletedState();
            uiModel.addMessage(String.format(Messages.SERVED_REQUEST, request.getType(), request.getIdentity()));
        } else {
            response.setErrorState(Messages.ERR_INTERNAL);
            uiModel.addMessage(String.format(Messages.ERROR_MSG, Messages.ERR_INTERNAL));
        }
        outputStream.writeUnshared(response);
    }

    /**
     * Check if a given email address is valid
     * @param identity The email address to check
     * @return True if the email address is valid (registered int UsersTable) false otherwise
     */
    private boolean checkIdentity(String identity) {
        return UsersTable.contains(identity);
    }

    /**
     * Check if the recipients of a given mail are valid
     * @param toCheck Email on which the check will be performed
     * @return True if all the recipients are valid users, false if even only one of them is invalid
     */
    private boolean checkRecipients(Email toCheck) {
        if(toCheck.getRecipients().length > 0) {
            boolean retVal = true;
            for(String address : toCheck.getRecipients())
                retVal = checkIdentity(address) && retVal;
            return retVal;
        } else {
            return false;
        }
    }
}
