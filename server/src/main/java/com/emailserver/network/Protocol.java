package com.emailserver.network;

import com.emailserver.beans.Email;
import com.emailserver.beans.User;
import com.emailserver.core.UsersTable;
import com.emailserver.io.FilesManager;

import java.util.*;

/**
 *  Protocol is a class that processes Requests from the client and always returns Responses.
 *  NOTE: This class SHOULD have an handler function for each RequestType
 *  TODO: Add check identity method
 */
public class Protocol {

    private static final String ERR_IDENTITY_NOT_FOUND = "Unable to retrieve the given user: Wrong identity";
    private static final String ERR_RECIPIENT_NOT_FOUND = "Unable to retrieve some recipients: Wrong address";
    private static final String ERR_MALFORMED_PARAM = "Unable to retrieve sender from email parameter: Malformed email";
    private static final String ERR_INTERNAL = "Unable to process the request: Internal error";

    /**
     * @param incoming
     * @return
     */
    public Response handleDeleteRequest(Request incoming) {
        Response response = new Response();
        Email toDelete = incoming.getEmailParam();
        Optional<User> user = UsersTable.get(toDelete.getSender());
        if(user.isPresent()) {

            switch(incoming.getType()) {
                case DEL_INBOX:
                    FilesManager.trashInboxEmail(user.get(), toDelete);
                    response.setCompletedState();
                    break;

                case DEL_SENT:
                    FilesManager.trashSentEmail(user.get(), toDelete);
                    response.setCompletedState();
                    break;

                case DEL_SPECIAL:
                    FilesManager.trashSpecialEmail(user.get(), toDelete);
                    response.setCompletedState();
                    break;

                default:
                    response.setErrorState(ERR_INTERNAL);
            }
        } else {
            response.setErrorState(ERR_MALFORMED_PARAM);
        }
        return response;
    }

    /**
     * @param incoming
     * @return
     */
    public Response handleSendRequest(Request incoming) {
        Response response = new Response();
        if(checkIdentity(incoming.getIdentity())) {
            Email toSend = incoming.getEmailParam();
            if(checkRecipients(toSend)) {
                FilesManager.saveEmail(toSend);
                response.setCompletedState();;
            } else {
                response.setErrorState(ERR_RECIPIENT_NOT_FOUND);
            }
        } else {
            response.setErrorState(ERR_IDENTITY_NOT_FOUND);
        }
        return response;
    }

    /**
     * @param incoming
     * @return
     */
    public List<Response> handleGetRequest(Request incoming) {
        Response lastResponse = new Response();
        Optional<User> user = UsersTable.get(incoming.getIdentity());           // Check if the identity is right
        List<Response> responseList = new ArrayList<>();
        if(user.isPresent()) {
            List<Email> emailList;

            switch(incoming.getType()) {
                case INBOX:
                    emailList = FilesManager.getInbox(user.get());
                    break;

                case TRASH:
                    emailList = FilesManager.getTrash(user.get());
                    break;

                case SPECIALS:
                    emailList = FilesManager.getSpecials(user.get());
                    break;

                case SENT:
                    emailList = FilesManager.getSent(user.get());
                    break;

                default:
                    lastResponse.setErrorState(ERR_INTERNAL);
                    return Collections.singletonList(lastResponse);
            }

            for(Email email : emailList) {
                Response response = new Response();
                response.setNextState();
                response.setPayload(email);
                responseList.add(response);
            }
            lastResponse.setCompletedState();
            responseList.add(lastResponse);
            return responseList;
        } else {
            lastResponse.setErrorState(ERR_IDENTITY_NOT_FOUND);
            return Collections.singletonList(lastResponse);
        }
    }

    /**
     * Check if the recipients are valid.
     * It has to check for each recipient of the email if it is present into the UsersTable
     * @param email The email to check
     * @return True if recipients are valid, false otherwise
     */
    private boolean checkRecipients(Email email) {
        assert email != null;
        boolean isValid = true;
        for(String recipient : email.getRecipients()) {
            isValid = isValid && UsersTable.contains(recipient);
        }
        return isValid;
    }

    private boolean checkIdentity(String emailAddress) {
        return UsersTable.get(emailAddress).isPresent();
    }

}
